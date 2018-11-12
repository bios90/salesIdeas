package dimfcompany.com.salesideas.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mikepenz.itemanimators.SlideRightAlphaAnimator;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dimfcompany.com.salesideas.Adapters.Adapter_VKfriends;
import dimfcompany.com.salesideas.Helpers.Compare_Helper;
import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Helpers.MySqlHelper;
import dimfcompany.com.salesideas.Helpers.VKStruct;
import dimfcompany.com.salesideas.Interfaces.IGetAllMyClients;
import dimfcompany.com.salesideas.Interfaces.ISimpleCallback;
import dimfcompany.com.salesideas.Interfaces.IVKCardClicked;
import dimfcompany.com.salesideas.Models.Model_Client;
import dimfcompany.com.salesideas.Models.Model_User_VK;
import dimfcompany.com.salesideas.R;
import dimfcompany.com.salesideas.act_edit_vk_user;
import dimfcompany.com.salesideas.act_main;

public class frag_import_vk2 extends Fragment
{
    private static final String TAG = "frag_import_client";

    act_main actParent;
    GlobalHelper gh;
    View thisFragmentRootView;
    List<Model_User_VK> allVkFriends = new ArrayList<>();
    List<Model_Client> allCurrentClients = new ArrayList<>();
    List<Integer> allFriendsIds = new ArrayList<>();
    Adapter_VKfriends adapter;
    Compare_Helper compare_helper;
    int clientsAdded = 0;


    Button btnImportVk;
    RecyclerView recVkUsers;
    Model_User_VK lastVkUserToEdit;
    AlertDialog spotsDialog;
    EditText etSearch;
    ExpandableLayout expBtnAddAll;
    RelativeLayout laFinish;

    RelativeLayout laAddAllVk;

    private static final int VK_USER_EDIT_CODE = 9000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_import_vk2,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        thisFragmentRootView = view;
        init();
    }


    private void init()
    {
        actParent = (act_main) getActivity();
        gh = (GlobalHelper) actParent.getApplicationContext();
        compare_helper = new Compare_Helper(actParent);
        btnImportVk = thisFragmentRootView.findViewById(R.id.btnImportVk);
        recVkUsers = thisFragmentRootView.findViewById(R.id.recVkUsers);
        recVkUsers.setItemAnimator(new SlideRightAlphaAnimator());
        etSearch = thisFragmentRootView.findViewById(R.id.etSearch);
        expBtnAddAll = thisFragmentRootView.findViewById(R.id.expBtnAddAll);
        laFinish = thisFragmentRootView.findViewById(R.id.laFinish);

        laFinish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                gh.setShowVkAdded(true);
                gh.setVkAddedNum(clientsAdded);
                actParent.showAllAfterVkAdd();
            }
        });


        laAddAllVk = thisFragmentRootView.findViewById(R.id.laAddAllVk);

        laAddAllVk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addAllNewVK();
            }
        });

        btnImportVk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                btnImportVk.setEnabled(false);
                vkImport1();
            }
        });

        etSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String text = etSearch.getText().toString();
                if(adapter != null)
                {
                    adapter.filter(text);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }




    private void vkImport1()
    {
        Log.e(TAG, "vkImport1: Begin vkImport1" );
        String token = (String)gh.getAdminInfo().get(GlobalHelper.SQL_COL_VK_API_TOKEN);
        if(token == null || token.isEmpty())
        {
            gh.showAlerter("Ошибка","Добавьте аккаунт ВК в разделе профиль.",actParent);
            resetButtonDialog();
            return;
        }


        resetAll();

        spotsDialog = gh.spotsDialogDialog(actParent,"Загрузка");
        spotsDialog.show();

        VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS,"id"));

        request.executeWithListener(new VKRequest.VKRequestListener()
        {
            @Override
            public void onError(VKError error)
            {
                super.onError(error);
                resetButtonDialog();
                Log.e(TAG, "VKimport1 request onError");
                gh.showAlerter("Ошибка", "Не удалось заггрузить друзей Вконтакте, повторите позже",actParent);
            }

            @Override
            public void onComplete(VKResponse response)
            {
                super.onComplete(response);
                try
                {
                    JSONArray allIdsArray = response.json.getJSONObject("response").getJSONArray("items");
                    for(int a = 0; a<allIdsArray.length();a++)
                    {
                        JSONObject currentFriend = allIdsArray.getJSONObject(a);
                        int id = currentFriend.getInt(VKStruct.VK_ID);
                        allFriendsIds.add(id);
                    }

                    vkImport2();

                }
                catch (Exception c)
                {
                    resetButtonDialog();
                    Log.e(TAG, "VKImport 1 catch error" );
                    gh.showAlerter("Ошибка", "Не удалось заггрузить друзей Вконтакте, повторите позже",actParent);
                }

            }
        });

    }

    private void vkImport2()
    {
        Log.e(TAG, "vkImport2: Begin Import2");

        if(allFriendsIds.size() == 0)
        {
            gh.showAlerter("Ошибка","Не удалось загрузить друзей ВК или их нет",actParent);
            resetButtonDialog();
            return;
        }

        String allIds = "";
        for(Integer id : allFriendsIds)
        {
            allIds += id;
            if(!(allFriendsIds.indexOf(id) == allFriendsIds.size()-1))
            {
                allIds += ",";
            }
        }



        VKRequest request2 = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS,allIds,VKApiConst.FIELDS,"nickname,domain,city,sex,bdate,photo_100,contacts"));
        request2.executeWithListener(new VKRequest.VKRequestListener()
        {
            @Override
            public void onComplete(VKResponse response)
            {
                super.onComplete(response);
                Log.e(TAG, "Got response with All friends in VK" );
                try
                {
                    Log.e(TAG, "Response "+response.responseString );
                    JSONArray allFriendsResponse = response.json.getJSONArray("response");

                    for(int a = 0; a<allFriendsResponse.length();a++)
                    {
                        JSONObject currentFriend = allFriendsResponse.getJSONObject(a);

                        Model_User_VK newUserVK = new Model_User_VK();
                        String firstName = currentFriend.getString(VKStruct.VK_FIRST_NAME);
                        if(firstName.equals("DELETED"))
                        {
                            continue;
                        }

                        newUserVK.setFirstName(currentFriend.getString(VKStruct.VK_FIRST_NAME));
                        newUserVK.setLastName(currentFriend.getString(VKStruct.VK_LAST_NAME));


                        newUserVK.setVk_id(String.valueOf(currentFriend.getInt(VKStruct.VK_ID)));
                        if(currentFriend.has(VKStruct.VK_SEX))
                        {
                            newUserVK.setGender(currentFriend.getInt(VKStruct.VK_SEX));
                        }
                        if(currentFriend.has(VKStruct.VK_NICK_NAME))
                        {
                            newUserVK.setNick(currentFriend.getString(VKStruct.VK_NICK_NAME));
                        }
                        if(currentFriend.has(VKStruct.VK_DOMAIN))
                        {
                            newUserVK.setVkDomain(currentFriend.getString(VKStruct.VK_DOMAIN));
                        }
                        if(currentFriend.has(VKStruct.VK_BIRTH_DAY))
                        {
                            newUserVK.setBdate(currentFriend.getString(VKStruct.VK_BIRTH_DAY));
                        }
                        if(currentFriend.has(VKStruct.VK_PHOTO_100))
                        {
                            newUserVK.setAvaUrl(currentFriend.getString(VKStruct.VK_PHOTO_100));
                        }
                        if(currentFriend.has(VKStruct.VK_PHONE))
                        {
                            Log.e(TAG, currentFriend.getString(VKStruct.VK_PHONE) );
                            newUserVK.setVkPhone(currentFriend.getString(VKStruct.VK_PHONE));
                        }

                        if(currentFriend.has(VKStruct.VK_CITY))
                        {
                            newUserVK.setCity(currentFriend.getJSONObject(VKStruct.VK_CITY).getString(VKStruct.VK_TITLE));
                        }

                        allVkFriends.add(newUserVK);
                    }

                    if(allVkFriends.size() == 0)
                    {
                        gh.showAlerter("Ошибка","Не удалось загрузить друзей ВК или их нет",actParent);
                        resetButtonDialog();
                        return;
                    }

                    loadAllCurrentUsers();
                }
                catch (Exception e)
                {
                    gh.showAlerter("Ошибка","Не удалось загрузить друзей ВК или их нет",actParent);
                    resetButtonDialog();
                    Log.e(TAG, "Catch "+e.getMessage() );
                }
            }
        });
    }

    private void loadAllCurrentUsers()
    {
        Log.e(TAG, "Begin loadAllCurrentUsers" );
        MySqlHelper mySqlHelper = new MySqlHelper(actParent);
        int adminId = (int)gh.getAdminInfo().get(GlobalHelper.SQL_COL_ID);
        mySqlHelper.loadAllClients(adminId, new IGetAllMyClients()
        {
            @Override
            public void onSuccess(List<Model_Client> allClients)
            {
                allCurrentClients = allClients;
                makeFinalImport();
            }

            @Override
            public void onError(String strError)
            {
                gh.showAlerter("Ошибка","Не удалось загрузить друзей ВК или их нет",actParent);
                resetButtonDialog();
                Log.e(TAG, "Error on loading current Users!" );
            }
        });
    }

    private void makeFinalImport()
    {
        Log.e(TAG, "Get all clients from Jino ok!" );
        Log.e(TAG, "all clients size is "+allCurrentClients.size());
        Log.e(TAG, "All vk frineds size is "+allVkFriends.size() );

        new LoadAllInBG().execute("");
    }

    private class LoadAllInBG extends AsyncTask<String,Void,String>
    {

        @Override
        protected void onPostExecute(String s)
        {
            Log.e(TAG, "onPostExecute: caleeedddd" );
            super.onPostExecute(s);
            setRecycler();
        }

        @Override
        protected String doInBackground(String... strings)
        {
            allVkFriends = compare_helper.compareVkAndCurrent(allVkFriends,allCurrentClients);

            if(allVkFriends.size() == 0)
            {
                Log.e(TAG, "makeFinalImport: after sort size is 0" );
                gh.showAlerter("Нет новых друзей","Не найдено новых друзей",actParent);
            }
            return null;
        }

    }





    private void setRecycler()
    {
        Log.e(TAG, "setRecycler: Begin SEt Recycler");
        if(allVkFriends.size() == 0)
        {
            Log.e(TAG, "setRecycler: no friends to show");
            resetButtonDialog();
            return;
        }
        
        adapter = new Adapter_VKfriends(actParent, allVkFriends, new IVKCardClicked()
        {
            @Override
            public void addClicked(Model_User_VK vkUser)
            {
                makeAdd(vkUser);
            }

            @Override
            public void editClicked(Model_User_VK vkUser)
            {
                makeEdit(vkUser);
            }
        });

        recVkUsers.setLayoutManager(new LinearLayoutManager(actParent));
        recVkUsers.setAdapter(adapter);
        resetButtonDialog();
        expBtnAddAll.expand();
        
    }

    private void makeEdit(Model_User_VK vkUser)
    {
        lastVkUserToEdit = vkUser;
        Model_Client vkToClient = gh.vkUserToClient(vkUser);
        vkToClient.initializeForAdd(actParent);
        gh.setCurrentClientFromVKEdit(vkToClient);
        Intent intent = new Intent(actParent,act_edit_vk_user.class);
        startActivityForResult(intent,VK_USER_EDIT_CODE);
    }

    private void makeAdd(final Model_User_VK vkUser)
    {
        Model_Client clientFromVk = gh.vkUserToClient(vkUser);
        clientFromVk.initializeForAdd(actParent);
        MySqlHelper mySqlHelper = new MySqlHelper(actParent);
        mySqlHelper.addNewClient(clientFromVk, new ISimpleCallback()
        {
            @Override
            public void onSuccess(String response)
            {
                if(response.equals("success"))
                {
                    gh.showAlerter("Успешно", "Новый клиент добавлен", actParent);
                    adapter.remove(vkUser);
                    clientsAdded++;
                }
                else
                    {
                        gh.showAlerter("Ошибка", "Не удалось добавить нового клиента, повторите позже", actParent);
                    }
            }

            @Override
            public void onError(String strError)
            {

            }
        });
    }

    private void addAllNewVK()
    {
        List<Model_User_VK> listToAdd = adapter.getCurrentListToShow();
        if(listToAdd.size() == 0)
        {
            gh.showAlerter("","Нет друзей для добавления",actParent);
            return;
        }

        spotsDialog = gh.spotsDialogDialog(actParent);
        List<Model_Client> clientsToInsert = new ArrayList<>();

        for(Model_User_VK vkUser : listToAdd)
        {
            Model_Client clientFromVk = gh.vkUserToClient(vkUser);
            clientFromVk.initializeForAdd(actParent);
            clientsToInsert.add(clientFromVk);
        }

        MySqlHelper mySqlHelper = new MySqlHelper(actParent);
        mySqlHelper.addMultiClients(clientsToInsert, new ISimpleCallback()
        {
            @Override
            public void onSuccess(String response)
            {
                try
                {
                    resetAll();
                    int addedNum = Integer.parseInt(response);
                    gh.showAlerter("Успешно","Добавлено "+addedNum+" новых клиента",actParent);
                    if(spotsDialog != null)
                    {
                        spotsDialog.dismiss();
                    }
                    clientsAdded+=addedNum;
                }
                catch (Exception e)
                {
                    resetAll();
                    gh.showAlerter("Ошибка","Ошибка при добавлении, повторите позже.",actParent);
                    if(spotsDialog != null)
                    {
                        spotsDialog.dismiss();
                    }
                }
            }

            @Override
            public void onError(String strError)
            {
                resetAll();
                gh.showAlerter("Ошибка",strError,actParent);
                if(spotsDialog != null)
                {
                    spotsDialog.dismiss();
                }
            }
        });
    }


//    private void addAllNewVK()
//    {
//        if(currentUsersToDisplay.size() == 0)
//        {
//            Log.e(TAG, "addAllNewVK: 0 to add will finish");
//            return;
//        }
//
//        List<Model_Client> clientsToInsert = new ArrayList<>();
//
//        for(Model_User_VK vkUser : currentUsersToDisplay)
//        {
//            Model_Client clientFromVk = gh.vkUserToClient(vkUser);
//            clientFromVk.initializeForAdd();
//            clientsToInsert.add(clientFromVk);
//        }
//
//        MySqlHelper mySqlHelper = new MySqlHelper(actParent);
//        mySqlHelper.addMultiClients(clientsToInsert, new ISimpleCallback()
//        {
//            @Override
//            public void onSuccess(String response)
//            {
//                try
//                {
//                    resetAll();
//                    int addedNum = Integer.parseInt(response);
//                    gh.showAlerter("Успешно","Добавлено "+addedNum+" новых клиента",actParent);
//                }
//                catch (Exception e)
//                {
//                    resetAll();
//                    gh.showAlerter("Ошибка","Ошибка при добавлении, повторите позже.",actParent);
//                }
//            }
//
//            @Override
//            public void onError(String strError)
//            {
//                resetAll();
//                gh.showAlerter("Ошибка",strError,actParent);
//            }
//        });
//    }

    private void resetAll()
    {
        actParent.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                allVkFriends.clear();
                allCurrentClients.clear();
                allFriendsIds.clear();
                recVkUsers.removeAllViews();
                recVkUsers.setAdapter(null);

                btnImportVk.setText("Загрузить друзей Вконтакте");
                expBtnAddAll.collapse();
            }
        });
    }

    void resetButtonDialog()
    {
        Log.e(TAG, "resetButtonDialog: calllllllledd!!" );
        actParent.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if(spotsDialog != null)
                {
                    spotsDialog.dismiss();
                }
                btnImportVk.setEnabled(true);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == VK_USER_EDIT_CODE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                gh.showAlerter("Успешно","Новый клиент успешно добавлен",actParent);
                adapter.remove(lastVkUserToEdit);
                clientsAdded++;
            }
            else if(resultCode == Activity.RESULT_CANCELED)
            {
                if(data != null)
                {
                    int err = data.getIntExtra("error", 777);
                    if (err == 999)
                    {
                        gh.showAlerter("Ошибка","Не удалось добавить нового клиента, повторите позже.",actParent);
                    }
                }
            }
        }
    }
}
