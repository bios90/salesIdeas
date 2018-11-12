package dimfcompany.com.salesideas.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dimfcompany.com.salesideas.Helpers.Compare_Helper;
import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Helpers.MySqlHelper;
import dimfcompany.com.salesideas.Helpers.VKStruct;
import dimfcompany.com.salesideas.Interfaces.IGetAllMyClients;
import dimfcompany.com.salesideas.Interfaces.ISimpleCallback;
import dimfcompany.com.salesideas.Models.Model_Client;
import dimfcompany.com.salesideas.Models.Model_User_VK;
import dimfcompany.com.salesideas.R;
import dimfcompany.com.salesideas.act_edit_vk_user;
import dimfcompany.com.salesideas.act_main;

public class frag_import_vk extends Fragment
{
    private static final String TAG = "frag_import_client";

    act_main actParent;
    GlobalHelper gh;
    View thisFragmentRootView;
    List<Model_User_VK> allVkFriends = new ArrayList<>();
    List<Model_Client> allCurrentClients = new ArrayList<>();

    List<Integer> allFriendsIds = new ArrayList<>();
    List<Model_User_VK> currentUsersToDisplay = new ArrayList<>();

    Button btnImportVk;
    LinearLayout laForAllVkUsers;
    Model_User_VK lastVkUserToEdit;

    RelativeLayout laAddAllVk;

    private static final int VK_USER_EDIT_CODE = 9000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_import_vk,container,false);
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
        btnImportVk = thisFragmentRootView.findViewById(R.id.btnImportVk);
        laForAllVkUsers = thisFragmentRootView.findViewById(R.id.laForAllClients);

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
    }


    private void resetAll()
    {
        allVkFriends.clear();
        allCurrentClients.clear();
        allFriendsIds.clear();
        currentUsersToDisplay.clear();

        laForAllVkUsers.removeAllViews();

        laAddAllVk.setVisibility(View.INVISIBLE);

        btnImportVk.setText("Загрузить друзей Вконтакте");
    }

    private void vkImport1()
    {
        Log.e(TAG, "vkImport1: Begin vkImport1" );
        String token = (String)gh.getAdminInfo().get(GlobalHelper.SQL_COL_VK_API_TOKEN);
        if(token == null || token.isEmpty())
        {
            gh.showAlerter("Ошибка","Добавьте аккаунт ВК в разделе профиль.",actParent);
            btnImportVk.setEnabled(true);
            return;
        }

        resetAll();

        VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS,"id"));

        request.executeWithListener(new VKRequest.VKRequestListener()
        {
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
            btnImportVk.setEnabled(true);
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
                        btnImportVk.setEnabled(true);
                        return;
                    }

                    loadAllCurrentUsers();
                }
                catch (Exception e)
                {
                    gh.showAlerter("Ошибка","Не удалось загрузить друзей ВК или их нет",actParent);
                    btnImportVk.setEnabled(true);
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
                Log.e(TAG, "Get all clients from Jino ok!" );
                Log.e(TAG, "all clients size is "+allClients.size());
                allCurrentClients = allClients;
                Compare_Helper compare_helper = new Compare_Helper(actParent);
                List<Model_User_VK> newUsers = compare_helper.compareVkAndCurrent(allVkFriends,allClients);

                currentUsersToDisplay = newUsers;

                if (currentUsersToDisplay.size() == 0)
                {
                    gh.showAlerter("Ошибка","Нет новых друзей для добавления",actParent);
                    btnImportVk.setEnabled(true);
                    return;
                }

                displayVkUsers();

            }

            @Override
            public void onError(String strError)
            {
                gh.showAlerter("Ошибка","Не удалось загрузить друзей ВК или их нет",actParent);
                btnImportVk.setEnabled(true);
                Log.e(TAG, "Error on loading current Users!" );
            }
        });
    }


    private void displayVkUsers()
    {
        Collections.sort(currentUsersToDisplay, new Comparator<Model_User_VK>()
        {
            public int compare(Model_User_VK obj1, Model_User_VK obj2)
            {
                return obj1.getFullName().compareToIgnoreCase(obj2.getFullName());
            }
        });

        Log.e(TAG, "displayVkUsers: Begin Displaying" );
        for(final Model_User_VK vkUser : currentUsersToDisplay)
        {
            final View vkCardView = actParent.getLayoutInflater().inflate(R.layout.item_vk_card,laForAllVkUsers,false);
            LinearLayout phoneRow = vkCardView.findViewById(R.id.laPhoneRow);
            LinearLayout birthdayRow = vkCardView.findViewById(R.id.laBirthdayRow);



            vkUser.setViewAsCard(vkCardView);



            TextView tvPhone = vkCardView.findViewById(R.id.tvPhone);
            TextView tvBirthday = vkCardView.findViewById(R.id.tvBirthday);
            TextView tvName = vkCardView.findViewById(R.id.tvName);

            final ExpandableLayout expVkCard = vkCardView.findViewById(R.id.expVkCard);

            Button btnAdd,btnDelete,btnEdit;

            btnDelete = vkCardView.findViewById(R.id.btnDelete);
            btnAdd = vkCardView.findViewById(R.id.btnAdd);
            btnEdit = vkCardView.findViewById(R.id.btnEdit);

            CircleImageView imageView = vkCardView.findViewById(R.id.avatar);

            tvName.setText(vkUser.getFullName());

            if(vkUser.getVkPhone() != null && !vkUser.getVkPhone().isEmpty())
            {
                tvPhone.setText(vkUser.getVkPhone());
            }
            else
                {
                    phoneRow.setVisibility(View.INVISIBLE);
                }


            if(vkUser.getBdate() != null )
            {
                try
                {
                    Date userBDay = GlobalHelper.vkDateFormat.parse(vkUser.getBdate());
                    tvBirthday.setText(GlobalHelper.dateFormatForDisplay.format(userBDay));
                }
                catch (Exception e)
                {
                    birthdayRow.setVisibility(View.INVISIBLE);
                }
            }
            else
                {
                    birthdayRow.setVisibility(View.INVISIBLE);
                }


            try
            {
                Picasso.get()
                        .load(vkUser.getAvaUrl())
                        .into(imageView);
            }
            catch (Exception e)
            {

            }


            btnAdd.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
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
                                gh.showAlerter("Успешно","Новый клиент успешно добавлен",actParent);
                                Animation animSlide = AnimationUtils.loadAnimation(actParent,
                                        R.anim.slide_left);
                                animSlide.setAnimationListener(new Animation.AnimationListener()
                                {
                                    @Override
                                    public void onAnimationStart(Animation animation)
                                    {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation)
                                    {
                                        ExpandableLayout expVkCard = vkUser.getViewAsCard().findViewById(R.id.expVkCard);
                                        expVkCard.collapse();
                                        expVkCard.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener()
                                        {
                                            @Override
                                            public void onExpansionUpdate(float expansionFraction, int state)
                                            {
                                                if(expansionFraction == 0)
                                                {
                                                    currentUsersToDisplay.remove(vkUser);
                                                    laForAllVkUsers.removeView(vkUser.getViewAsCard());
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation)
                                    {

                                    }
                                });

                                vkUser.getViewAsCard().startAnimation(animSlide);
                            }

                            else
                                {
                                    gh.showAlerter("Ошибка","Не удалось добавить нового пользователя, повторите позже.",actParent);
                                }
                        }

                        @Override
                        public void onError(String strError)
                        {
                            gh.showAlerter("Ошибка","Не удалось добавить нового пользователя, повторите позже.",actParent);
                        }
                    });
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    lastVkUserToEdit = vkUser;
                    Model_Client vkToClient = gh.vkUserToClient(vkUser);
                    gh.setCurrentClientFromVKEdit(vkToClient);
                    Intent intent = new Intent(actParent,act_edit_vk_user.class);
                    startActivityForResult(intent,VK_USER_EDIT_CODE);
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Animation animSlide = AnimationUtils.loadAnimation(actParent,
                            R.anim.slide_left);
                    animSlide.setAnimationListener(new Animation.AnimationListener()
                    {
                        @Override
                        public void onAnimationStart(Animation animation)
                        {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation)
                        {
                            expVkCard.collapse();
                            expVkCard.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener()
                            {
                                @Override
                                public void onExpansionUpdate(float expansionFraction, int state)
                                {
                                    if(expansionFraction == 0)
                                    {
                                        currentUsersToDisplay.remove(vkUser);
                                        laForAllVkUsers.removeView(vkCardView);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation)
                        {

                        }
                    });

                    vkCardView.startAnimation(animSlide);

                }
            });

            laForAllVkUsers.addView(vkCardView);
        }


        btnImportVk.setEnabled(true);
        btnImportVk.setText("Обновить");
        if(laForAllVkUsers.getChildCount() > 0)
        {
            laAddAllVk.setVisibility(View.VISIBLE);
        }
    }


    private void addAllNewVK()
    {
        if(currentUsersToDisplay.size() == 0)
        {
            Log.e(TAG, "addAllNewVK: 0 to add will finish");
            return;
        }

        List<Model_Client> clientsToInsert = new ArrayList<>();

        for(Model_User_VK vkUser : currentUsersToDisplay)
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
                }
                catch (Exception e)
                {
                    resetAll();
                    gh.showAlerter("Ошибка","Ошибка при добавлении, повторите позже.",actParent);
                }
            }

            @Override
            public void onError(String strError)
            {
                resetAll();
                gh.showAlerter("Ошибка",strError,actParent);
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
                Animation animSlide = AnimationUtils.loadAnimation(actParent,
                        R.anim.slide_left);
                animSlide.setAnimationListener(new Animation.AnimationListener()
                {
                    @Override
                    public void onAnimationStart(Animation animation)
                    {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        ExpandableLayout expVkCard = lastVkUserToEdit.getViewAsCard().findViewById(R.id.expVkCard);
                        expVkCard.collapse();
                        expVkCard.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener()
                        {
                            @Override
                            public void onExpansionUpdate(float expansionFraction, int state)
                            {
                                if(expansionFraction == 0)
                                {
                                    currentUsersToDisplay.remove(lastVkUserToEdit);
                                    laForAllVkUsers.removeView(lastVkUserToEdit.getViewAsCard());
                                }
                            }
                        });
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation)
                    {

                    }
                });

                lastVkUserToEdit.getViewAsCard().startAnimation(animSlide);
            }
            else if(resultCode == Activity.RESULT_CANCELED)
            {
                if(data != null)
                {
                    int err = data.getIntExtra("error", 777);
                    if (err == 999)
                    {
                        gh.showAlerter("Ошибка","Не удалось добавить нового пользователя, повторите позже.",actParent);
                    }
                }
            }
        }
    }
}
