package dimfcompany.com.salesideas.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.itemanimators.SlideRightAlphaAnimator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dimfcompany.com.salesideas.Adapters.Adapter_AllClients;
import dimfcompany.com.salesideas.Helpers.Compare_Helper;
import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Helpers.MySqlHelper;
import dimfcompany.com.salesideas.Interfaces.IClientCardClicked;
import dimfcompany.com.salesideas.Interfaces.IGetAllMyClients;
import dimfcompany.com.salesideas.Models.Model_Client;
import dimfcompany.com.salesideas.R;
import dimfcompany.com.salesideas.act_full_client_info;
import dimfcompany.com.salesideas.act_main;

public class frag_all_clients2 extends Fragment
{
    private static final String TAG = "frag_all_clients";

    act_main actParent;
    frag_clients parentFragClients;
    GlobalHelper gh;
    View thisFragmentRootView;
    RecyclerView recAllClients;
    Drawable drawMale,drawFemale;
    EditText etSearch;
    Compare_Helper compare_helper;
    AlertDialog spotsDialog;

    List<Model_Client> allClients = new ArrayList<>();
    Adapter_AllClients adapter;

    private static final int CARD_CLICKED_INTENT = 9000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_all_clients2,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        thisFragmentRootView = view;
        init();
        loadAll();


    }


    private void init()
    {
        actParent = (act_main) getActivity();
        parentFragClients = ((frag_clients)frag_all_clients2.this.getParentFragment());
        gh = (GlobalHelper) actParent.getApplicationContext();
        compare_helper = new Compare_Helper(actParent);
        recAllClients = thisFragmentRootView.findViewById(R.id.recAllClients);
        recAllClients.setItemAnimator(new SlideRightAlphaAnimator());

        drawMale = getResources().getDrawable(R.drawable.ic_user_male);
        drawFemale = getResources().getDrawable(R.drawable.ic_user_female);

        etSearch = thisFragmentRootView.findViewById(R.id.etSearch);

        etSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String searchText = etSearch.getText().toString();
                adapter.filter(searchText);
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }


    private void loadAll()
    {
        Log.e(TAG, "loadAll: Begin load All" );
        resetAll();
        spotsDialog = gh.spotsDialogDialog(actParent,"Загрузка");
        spotsDialog.show();
        int adminId = (int)gh.getAdminInfo().get(GlobalHelper.SQL_COL_ID);
        MySqlHelper mySqlHelper = new MySqlHelper(actParent);
        mySqlHelper.loadAllClients(adminId, new IGetAllMyClients()
        {
            @Override
            public void onSuccess(List<Model_Client> allClients)
            {
                Log.e(TAG, "onSuccess: Succes from load All" );
                frag_all_clients2.this.allClients = allClients;
                setRecycler();
            }

            @Override
            public void onError(String strError)
            {
                Log.e(TAG, "onError: Error from load all "+strError );
                if(spotsDialog != null)
                {
                    spotsDialog.dismiss();
                }
            }
        });
    }

    private void setRecycler()
    {
        Log.e(TAG, "setRecycler: Begin set Recycler" );
        if(allClients.size() == 0)
        {
            gh.showAlerter("Нет клиентов","Клиентов не найдено, добавьте во вкладке \"Новый\" или в меню \"Импорт\"",actParent);
            if(spotsDialog != null)
            {
                spotsDialog.dismiss();
            }
            return;
        }
        
        adapter = new Adapter_AllClients(actParent, allClients, new IClientCardClicked()
        {
            @Override
            public void cardClicked(Model_Client client)
            {
                gh.setCurrentClientToDisplay(client);
                Intent intent = new Intent(actParent,act_full_client_info.class);
                startActivityForResult(intent, CARD_CLICKED_INTENT);
            }

            @Override
            public void phoneClicked(Model_Client client)
            {
                Toast.makeText(actParent, "Phone clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void emailClicked(Model_Client client)
            {

            }
        });

        recAllClients.setLayoutManager(new LinearLayoutManager(actParent));
        recAllClients.setAdapter(adapter);
        if(spotsDialog != null)
        {
            spotsDialog.dismiss();
        }
        
    }
    
    private void resetAll()
    {
        Log.e(TAG, "resetAll: Begin Reset All" );
        allClients.clear();
        recAllClients.removeAllViews();
        recAllClients.setAdapter(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CARD_CLICKED_INTENT && resultCode == Activity.RESULT_OK)
        {
            Log.e(TAG, "onActivityResult: Result Code Ok!!!" );
            loadAll();
        }
    }
}
