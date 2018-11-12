package dimfcompany.com.salesideas.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mikepenz.itemanimators.SlideRightAlphaAnimator;

import java.util.ArrayList;
import java.util.List;

import dimfcompany.com.salesideas.Adapters.Adapter_Filters;
import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Helpers.MySqlHelper;
import dimfcompany.com.salesideas.Interfaces.IFilterCardClicked;
import dimfcompany.com.salesideas.Interfaces.IGetAllFiltersCallback;
import dimfcompany.com.salesideas.Interfaces.IGetAllMyClients;
import dimfcompany.com.salesideas.Interfaces.IGetUserInterestsCallback;
import dimfcompany.com.salesideas.Interfaces.IGetUserProductsCallback;
import dimfcompany.com.salesideas.Models.Model_Client;
import dimfcompany.com.salesideas.Models.Model_Filter;
import dimfcompany.com.salesideas.Models.Model_Interest;
import dimfcompany.com.salesideas.Models.Model_Product;
import dimfcompany.com.salesideas.R;
import dimfcompany.com.salesideas.act_edit_filter;
import dimfcompany.com.salesideas.act_main;
import dimfcompany.com.salesideas.act_new_filter;
import dimfcompany.com.salesideas.act_show_filtered_clients;

public class frag_analitics extends Fragment
{
    private static final String TAG = "frag_export";

    GlobalHelper gh;
    act_main actParent;
    View thisFragmentRootView;
    Button btnNewFilter;
    RecyclerView recAnalitics;
    MySqlHelper mySqlHelper;
    List<Model_Filter> allFilters = new ArrayList<>();
    List<Model_Product> allProducts;
    List<Model_Interest> allInterests;
    Adapter_Filters adapter;


    private FragmentManager fragManager;
    private static final int NEW_FILTER_CODE = 9009;
    private static final int FILTER_EDIT_CODE = 9099;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_analitics, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        thisFragmentRootView = view;
        init();
        loadFilters();
    }

    private void init()
    {
        actParent = (act_main)getActivity();
        gh =(GlobalHelper) actParent.getApplicationContext();
        mySqlHelper = new MySqlHelper(actParent);
        fragManager = getChildFragmentManager();

        recAnalitics = thisFragmentRootView.findViewById(R.id.recAnalitics);
        recAnalitics.setItemAnimator(new SlideRightAlphaAnimator());
        DividerItemDecoration itemDecorator = new DividerItemDecoration(actParent, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(getResources().getDrawable(R.drawable.divider_vert));
        recAnalitics.addItemDecoration(itemDecorator);

        btnNewFilter = thisFragmentRootView.findViewById(R.id.btnNewFilter);

        btnNewFilter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(actParent,act_new_filter.class);
                startActivityForResult(intent, NEW_FILTER_CODE);
            }
        });
    }



    //region Loading All Data
    private void loadFilters()
    {
        Log.e(TAG, "loadFilters: Begin Load Filters" );
        mySqlHelper.getAllFilters(new IGetAllFiltersCallback()
        {
            @Override
            public void onSuccess(List<Model_Filter> allFilters)
            {
                frag_analitics.this.allFilters = allFilters;
                loadAllProducts();
            }

            @Override
            public void onError(String strError)
            {

            }
        });
    }

    private void loadAllProducts()
    {
        Log.e(TAG, "loadAllProducts: Begin load all Products" );
        mySqlHelper.loadProducts( new IGetUserProductsCallback()
        {
            @Override
            public void onSuccess(List<Model_Product> userProducts)
            {
                allProducts = userProducts;
                loadAllInterests();
            }

            @Override
            public void onError(String strError)
            {
                Log.e(TAG, "onError: "+strError );
                gh.showAlerter("Ошибка","Не удалось загрузить список продуктов, повторите позже.",actParent);
            }
        });
    }

    private void loadAllInterests()
    {
        Log.e(TAG, "loadAllInterests: Load all interests" );
        mySqlHelper.loadInterests( new IGetUserInterestsCallback()
        {
            @Override
            public void onSuccess(List<Model_Interest> userInterests)
            {
                allInterests = userInterests;
                setRecycler();
            }

            @Override
            public void onError(String strError)
            {
                gh.showAlerter("Ошибка","Не удалось загрузить список интересов, повторите позже.",actParent);
            }
        });
    }
    //endregion


    private void setRecycler()
    {
        Log.e(TAG, "setRecycler: Begin Setting Recycler");
        if(allFilters.size() == 0)
        {
            Log.e(TAG, "size of all filters is 0 will return");
            return;
        }

        adapter = new Adapter_Filters(actParent, allFilters, allProducts, allInterests, new IFilterCardClicked()
        {
            @Override
            public void filterClicked(Model_Filter filter)
            {
                makeFilter(filter);
            }

            @Override
            public void editClicked(Model_Filter filter)
            {
                makeEdit(filter);
            }
        });
        recAnalitics.setLayoutManager(new LinearLayoutManager(actParent));
        recAnalitics.setAdapter(adapter);
    }


    private void makeFilter(Model_Filter filter)
    {
        mySqlHelper.filterClients(filter.getId(), new IGetAllMyClients()
        {
            @Override
            public void onSuccess(List<Model_Client> allClients)
            {
                if(allClients.size() == 0)
                {
                    gh.showAlerter("Ошибка","Не найдено клиентов по данному фильтру",actParent);
                    return;
                }

                gh.setCurrentSorteredClients(allClients);
                Intent intent = new Intent(actParent,act_show_filtered_clients.class);
                startActivity(intent);
            }

            @Override
            public void onError(String strError)
            {

            }
        });
    }

    private void makeEdit(Model_Filter filter)
    {
        gh.setCurrentFilterToEdit(filter);
        Intent intent = new Intent(actParent,act_edit_filter.class);
        startActivityForResult(intent,FILTER_EDIT_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NEW_FILTER_CODE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                gh.showAlerter("Успешно","Новый фильтр успешно создана",actParent);
                resetAll();
                loadFilters();
            }
        }

        if(requestCode == FILTER_EDIT_CODE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                gh.showAlerter("Успешно","Фильтр успешно отредактирован",actParent);
                resetAll();
                loadFilters();
            }
        }
    }

    private void resetAll()
    {
        allFilters.clear();
        allProducts.clear();
        allInterests.clear();;
        recAnalitics.setAdapter(null);
        recAnalitics.removeAllViews();
    }
}
