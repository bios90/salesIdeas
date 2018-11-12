package dimfcompany.com.salesideas.Fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dimfcompany.com.salesideas.Adapters.Adapter_Clients_Names;
import dimfcompany.com.salesideas.Adapters.Adapter_Finished_Excel;
import dimfcompany.com.salesideas.Helpers.DownloadHelper;
import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Helpers.MySqlHelper;
import dimfcompany.com.salesideas.Interfaces.IExcelCardClickCallback;
import dimfcompany.com.salesideas.Interfaces.IGetAllExcelsCallback;
import dimfcompany.com.salesideas.Interfaces.INamesListCallback;
import dimfcompany.com.salesideas.Interfaces.ISimpleCallback;
import dimfcompany.com.salesideas.Models.Model_Excel_Item;
import dimfcompany.com.salesideas.R;
import dimfcompany.com.salesideas.act_export_dialog;
import dimfcompany.com.salesideas.act_main;

public class frag_export extends Fragment
{
    private static final String TAG = "frag_export";

    GlobalHelper gh;
    act_main actParent;
    View thisFragmentRootView;
    Button btnNewExport;
    RecyclerView recExport;
    MySqlHelper mySqlHelper;
    List<Model_Excel_Item> allExcels = new ArrayList<>();
    Adapter_Finished_Excel adapter;


    private FragmentManager fragManager;
    private static final int NEW_EXPORT_CODE = 9009;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_export, container,false);
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
        actParent = (act_main)getActivity();
        gh =(GlobalHelper) actParent.getApplicationContext();
        mySqlHelper = new MySqlHelper(actParent);
        fragManager = getChildFragmentManager();
        recExport = thisFragmentRootView.findViewById(R.id.recExport);
        recExport.setItemAnimator(new SlideRightAlphaAnimator());
        DividerItemDecoration itemDecorator = new DividerItemDecoration(actParent, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(getResources().getDrawable(R.drawable.divider_vert));
        recExport.addItemDecoration(itemDecorator);

        btnNewExport = thisFragmentRootView.findViewById(R.id.btnNewExport);

        btnNewExport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(actParent,act_export_dialog.class);
                startActivityForResult(intent,NEW_EXPORT_CODE);

            }
        });

        mySqlHelper.loadAllExcel(new IGetAllExcelsCallback()
        {
            @Override
            public void onSuccess(List<Model_Excel_Item> allClients)
            {
                allExcels = allClients;
                setRecycler();
            }

            @Override
            public void onError(String strError)
            {
                Log.e(TAG, "onError: "+strError );
            }
        });

    }

    private void setRecycler()
    {
        Log.e(TAG, "setRecycler: Begin Setting Recycler");
        if(allExcels.size() == 0)
        {
            Log.e(TAG, "setRecycler: All Excels size = 0" );
            return;
        }
        adapter = new Adapter_Finished_Excel(actParent, allExcels, new IExcelCardClickCallback()
        {
            @Override
            public void cardClicked(final Model_Excel_Item item)
            {
                URL url = gh.excelUrlFromFileName(item.getFilename());
                final File file = gh.mkFileForExcel(actParent,item.getFilename());

                if(url == null || file == null)
                {
                    Log.e(TAG, " URL or file NULL wil return" );
                    return;
                }

                DownloadHelper downloadHelper = new DownloadHelper(url, file, new ISimpleCallback()
                {
                    @Override
                    public void onSuccess(String response)
                    {
                        item.setLocalFile(file);
                        showIntent(item);
                    }

                    @Override
                    public void onError(String strError)
                    {

                    }
                });

                downloadHelper.execute();
            }

            @Override
            public void shareClicked(final Model_Excel_Item item)
            {
                URL url = gh.excelUrlFromFileName(item.getFilename());
                final File file = gh.mkFileForExcel(actParent,item.getFilename());

                if(url == null || file == null)
                {
                    Log.e(TAG, " URL or file NULL wil return" );
                    return;
                }

                DownloadHelper downloadHelper = new DownloadHelper(url, file, new ISimpleCallback()
                {
                    @Override
                    public void onSuccess(String response)
                    {
                        item.setLocalFile(file);
                        shareIntent(item);
                    }

                    @Override
                    public void onError(String strError)
                    {

                    }
                });

                downloadHelper.execute();
            }
        });
        recExport.setLayoutManager(new LinearLayoutManager(actParent));
        recExport.setAdapter(adapter);
    }


    private void showIntent(Model_Excel_Item item)
    {
        try
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(item.getLocalFile()), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {
            gh.showAlerter("Ошибка","Не найдено программ для открытия файлов Excel",actParent);
        }

    }


    private void shareIntent(Model_Excel_Item item)
    {
        try
        {
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            File file = item.getLocalFile();

            if (file.exists())
            {
                intentShareFile.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file.getAbsolutePath()));

                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Поделиться файлом");
                String text = "Таблица клиентов от "+GlobalHelper.dateFormatForDisplay.format(item.getMake_date())+" .В таблице "+item.getClient_num()+" клиентов";
                intentShareFile.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(intentShareFile, "Поделиться файлом"));
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "shareIntent: "+e.getMessage() );
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NEW_EXPORT_CODE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                gh.showAlerter("Успешно","Новая таблица успешно создана",actParent);
                resetAll();
            }
        }
    }

    private void resetAll()
    {
        allExcels.clear();
        recExport.setAdapter(null);
        recExport.removeAllViews();

        mySqlHelper.loadAllExcel(new IGetAllExcelsCallback()
        {
            @Override
            public void onSuccess(List<Model_Excel_Item> allClients)
            {
                allExcels = allClients;
                setRecycler();
            }

            @Override
            public void onError(String strError)
            {
                Log.e(TAG, "onError: "+strError );
            }
        });
    }
}
