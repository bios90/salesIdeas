package dimfcompany.com.salesideas.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import dimfcompany.com.salesideas.Adapters.Adapter_Clients_From_Excel;
import dimfcompany.com.salesideas.Adapters.Adapter_VKfriends;
import dimfcompany.com.salesideas.Helpers.Compare_Helper;
import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Helpers.MultiPartRequest;
import dimfcompany.com.salesideas.Helpers.MySqlHelper;
import dimfcompany.com.salesideas.Helpers.UploadExcelAndGetClients;
import dimfcompany.com.salesideas.Helpers.VKStruct;
import dimfcompany.com.salesideas.Interfaces.IClientExcelClicked;
import dimfcompany.com.salesideas.Interfaces.IExcelCardClickCallback;
import dimfcompany.com.salesideas.Interfaces.IGetAllMyClients;
import dimfcompany.com.salesideas.Interfaces.ISimpleCallback;
import dimfcompany.com.salesideas.Interfaces.IVKCardClicked;
import dimfcompany.com.salesideas.Models.Model_Client;
import dimfcompany.com.salesideas.Models.Model_Excel_Item;
import dimfcompany.com.salesideas.Models.Model_User_VK;
import dimfcompany.com.salesideas.R;
import dimfcompany.com.salesideas.act_edit_vk_user;
import dimfcompany.com.salesideas.act_main;

public class frag_import_excel extends Fragment
{
    private static final String TAG = "frag_import_excel";

    act_main actParent;
    GlobalHelper gh;
    View thisFragmentRootView;
    List<Model_Client> clientsFromExcel = new ArrayList<>();
    List<Model_Client> allClients = new ArrayList<>();
    Adapter_Clients_From_Excel adapter;
    Model_Client lastClientToEdit;

    Button btnSelectFile;
    RecyclerView recExcelClients;
    AlertDialog spotsDialog;
    EditText etSearch;
    ExpandableLayout expBtnAddAll;


    private static final int FILE_PICK_CODE = 9099;
    private static final int CLIENT_EDIT_CODE = 9100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_import_excel,container,false);
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
        btnSelectFile = thisFragmentRootView.findViewById(R.id.btnSelectFile);
        recExcelClients = thisFragmentRootView.findViewById(R.id.recExcelClients);
        recExcelClients.setItemAnimator(new SlideRightAlphaAnimator());
        etSearch = thisFragmentRootView.findViewById(R.id.etSearch);
        expBtnAddAll = thisFragmentRootView.findViewById(R.id.expBtnAddAll);



        btnSelectFile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Выберите файл xls/xlsx для ипорта клиентов.");
                startActivityForResult(chooseFile, FILE_PICK_CODE);
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
                adapter.filter(text);
            }

            @Override
            public void afterTextChanged(Editable s)
            {

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
                Log.e(TAG, "onSuccess: got response from all clients");
                frag_import_excel.this.allClients = allClients;
                Compare_Helper ch = new Compare_Helper(actParent);
                clientsFromExcel = ch.compareExcleToCurrent(clientsFromExcel,allClients);
                setRecycler();
            }

            @Override
            public void onError(String strError)
            {
                gh.showAlerter("Ошибка","Не удалось завершить сравнение с текущей базы",actParent);
                Log.e(TAG, "Error on loading current Users!" );
                if(spotsDialog != null)
                {
                    spotsDialog.dismiss();
                }
            }
        });
    }





    private void setRecycler()
    {
        Log.e(TAG, "setRecycler: Begin SEt Recycler");

        if(clientsFromExcel.size() == 0)
        {
            gh.showAlerter("Ошибка","В нет новых клиентов для добавления",actParent);
            if(spotsDialog != null)
            {
                spotsDialog.dismiss();
            }
            return;
        }


        adapter = new Adapter_Clients_From_Excel(actParent, clientsFromExcel, new IClientExcelClicked()
        {
            @Override
            public void addClicked(Model_Client client)
            {
                makeAdd(client);
            }

            @Override
            public void editClicked(Model_Client client)
            {
                makeEdit(client);
            }
        });


        recExcelClients.setLayoutManager(new LinearLayoutManager(actParent));
        recExcelClients.setAdapter(adapter);
        expBtnAddAll.expand();
        if(spotsDialog != null)
        {
            spotsDialog.dismiss();
        }
        
    }

    private void makeAdd(final Model_Client client)
    {
        client.initializeForAdd(actParent);
        MySqlHelper mySqlHelper = new MySqlHelper(actParent);
        mySqlHelper.addNewClient(client, new ISimpleCallback()
        {
            @Override
            public void onSuccess(String response)
            {
                if(response.equals("success"))
                {
                    gh.showAlerter("Успешно", "Новый клиент добавлен", actParent);
                    adapter.remove(client);
                }
                else
                {
                    gh.showAlerter("Ошибка", "Не удалось добавить нового клиента, повторите позже", actParent);
                }
            }

            @Override
            public void onError(String strError)
            {
                Log.e(TAG, "onError: "+strError );
            }
        });
    }

    private void makeEdit(Model_Client client)
    {
        lastClientToEdit = client;
        client.initializeForAdd(actParent);
        gh.setCurrentClientFromVKEdit(client);
        Intent intent = new Intent(actParent,act_edit_vk_user.class);
        startActivityForResult(intent,CLIENT_EDIT_CODE);
    }



    private void resetAll()
    {
        clientsFromExcel.clear();
        recExcelClients.removeAllViews();
        recExcelClients.setAdapter(null);
        expBtnAddAll.collapse();
    }


    private void makeUpload(File file)
    {
        resetAll();
        spotsDialog = gh.spotsDialogDialog(actParent);
        spotsDialog.show();
        UploadExcelAndGetClients upgc = new UploadExcelAndGetClients();
        upgc.uploadAndGet(file, actParent, new IGetAllMyClients()
        {
            @Override
            public void onSuccess(List<Model_Client> allClients)
            {
                clientsFromExcel = allClients;
                loadAllCurrentUsers();
            }

            @Override
            public void onError(String strError)
            {
                if(strError.equals("got error"))
                {
                    gh.showAlerter("Ошибка","Ошибка связи с сервером, повторите позже",actParent);
                }
                if(strError.equals("over size"))
                {
                    gh.showAlerter("Ошибка","Первышен максимальный размер файла 10мб",actParent);
                }
                if(strError.equals("wrong extension"))
                {
                    gh.showAlerter("Ошибка","Неверный формат файла, доступны файлы xls/xlsx",actParent);
                }
                if(strError.equals("parse error"))
                {
                    gh.showAlerter("Ошибка","Неверный формат таблицы",actParent);
                }
                if(spotsDialog != null)
                {
                    spotsDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //region Excel File Pick
        if(requestCode == FILE_PICK_CODE && resultCode == Activity.RESULT_OK)
        {
            File file = null;
            try
            {
                try
                {
                    file = gh.createTempExcelFile(actParent,data.getDataString());
                    if(file == null)
                    {
                        Log.e(TAG, "Error on temp File creating" );
                        return;
                    }
                }
                catch (IOException ex)
                {
                    Log.e(TAG, "Error occurred while creating the file");
                    return;
                }



                InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                gh.copyStream(inputStream, fileOutputStream);
                fileOutputStream.close();
                inputStream.close();

                if(file.length() == 0)
                {
                    Log.e(TAG, "File is empty!");
                    return;
                }

                makeUpload(file);

            } catch (Exception e)
            {
                Log.e(TAG, "Catch error " + e.toString());
            }
        }
        //endregion

        if(requestCode == CLIENT_EDIT_CODE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                gh.showAlerter("Успешно","Новый клиент успешно добавлен",actParent);
                adapter.remove(lastClientToEdit);
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
