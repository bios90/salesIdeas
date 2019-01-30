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
import android.widget.RelativeLayout;
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
import dimfcompany.com.salesideas.act_sort_dialog;

public class frag_all_clients2 extends Fragment
{
    private static final String TAG = "frag_all_clients";

    act_main actParent;
    frag_clients parentFragClients;
    GlobalHelper gh;
    View thisFragmentRootView;
    RecyclerView recAllClients;
    EditText etSearch;
    Compare_Helper compare_helper;
    AlertDialog spotsDialog;

    TextView tvSort;

    List<Model_Client> allClients = new ArrayList<>();
    Adapter_AllClients adapter;

    private static final int CARD_CLICKED_INTENT = 9000;
    private static final int SORT_CLICKED = 9100;

    int currentSortInt = 999;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_all_clients2, container, false);
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
        parentFragClients = ((frag_clients) frag_all_clients2.this.getParentFragment());
        gh = (GlobalHelper) actParent.getApplicationContext();
        compare_helper = new Compare_Helper(actParent);
        recAllClients = thisFragmentRootView.findViewById(R.id.recAllClients);
        recAllClients.setItemAnimator(new SlideRightAlphaAnimator());

        tvSort = thisFragmentRootView.findViewById(R.id.tvSort);
        tvSort.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(actParent, act_sort_dialog.class);
                intent.putExtra("selected",currentSortInt);
                startActivityForResult(intent, SORT_CLICKED);
            }
        });

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
                if(adapter != null)
                {
                    adapter.filter(searchText);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }


    private void loadAll()
    {
        Log.e(TAG, "loadAll: Begin load All");
        resetAll();
        spotsDialog = gh.spotsDialogDialog(actParent, "Загрузка");
        spotsDialog.show();
        MySqlHelper mySqlHelper = new MySqlHelper(actParent);
        mySqlHelper.loadAllClients(new IGetAllMyClients()
        {
            @Override
            public void onSuccess(List<Model_Client> allClients)
            {
                Log.e(TAG, "onSuccess: Succes from load All");
                frag_all_clients2.this.allClients = allClients;
                setRecycler();
            }

            @Override
            public void onError(String strError)
            {
                Log.e(TAG, "onError: Error from load all " + strError);
                if (spotsDialog != null)
                {
                    spotsDialog.dismiss();
                }
            }
        });
    }

    private void setRecycler()
    {
        Log.e(TAG, "setRecycler: Begin set Recycler");
        if (allClients.size() == 0)
        {
            gh.showAlerter("Нет клиентов", "Клиентов не найдено, добавьте во вкладке \"Новый\" или в меню \"Импорт\"", actParent);
            if (spotsDialog != null)
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
                Intent intent = new Intent(actParent, act_full_client_info.class);
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
        if (spotsDialog != null)
        {
            spotsDialog.dismiss();
        }

    }

    private void makeSort()
    {
        if (currentSortInt == 999)
        {
            Log.e(TAG, "makeSort: sorrt int is 999 will return");
            return;
        }

        switch (currentSortInt)
        {
            case 0:
                allClients = GlobalHelper.sortCliByName(allClients);
                break;
            case 1:
                allClients = GlobalHelper.sortCliByCateg(allClients);
                break;
            case 2:
                allClients = GlobalHelper.sortCliByPhone(allClients);
                break;
            case 3:
                allClients = GlobalHelper.sortCliByEmail(allClients);
                break;
            case 4:
                allClients = GlobalHelper.sortCliByCity(allClients);
                break;
            case 5:
                allClients = GlobalHelper.sortCliByDate(allClients);
                break;
        }

        recAllClients.removeAllViews();
        recAllClients.setAdapter(null);
        etSearch.setText("");
        setRecycler();
    }


    private void resetAll()
    {
        Log.e(TAG, "resetAll: Begin Reset All");
        allClients.clear();
        recAllClients.removeAllViews();
        recAllClients.setAdapter(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CARD_CLICKED_INTENT && resultCode == Activity.RESULT_OK)
        {
            Log.e(TAG, "onActivityResult: Result Code Ok!!!");
            loadAll();
        }

        if (requestCode == SORT_CLICKED && resultCode == Activity.RESULT_OK)
        {
            int selected = data.getIntExtra("selected", 999);
            if(selected == currentSortInt)
            {
                Log.e(TAG, "onActivityResul ints are equal will retirn");
                return;
            }
            Log.e(TAG, "onActivityResult: Sort int is " + currentSortInt);
            currentSortInt = selected;
            makeSort();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        RelativeLayout laNoInternet = thisFragmentRootView.findViewById(R.id.laNoInternet);
        if(gh.isNetworkAvailable(actParent))
        {
            laNoInternet.setVisibility(View.GONE);
        }else
            {
                laNoInternet.setVisibility(View.VISIBLE);
            }

    }
}
