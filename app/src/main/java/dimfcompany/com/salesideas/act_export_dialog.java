package dimfcompany.com.salesideas;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.itemanimators.SlideRightAlphaAnimator;

import java.util.ArrayList;
import java.util.List;

import dimfcompany.com.salesideas.Adapters.Adapter_Clients_Names;
import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Helpers.MyDivider;
import dimfcompany.com.salesideas.Helpers.MySqlHelper;
import dimfcompany.com.salesideas.Interfaces.IGetAllMyClients;
import dimfcompany.com.salesideas.Interfaces.INamesListCallback;
import dimfcompany.com.salesideas.Interfaces.ISimpleCallback;
import dimfcompany.com.salesideas.Models.Model_Client;

public class act_export_dialog extends AppCompatActivity
{
    private static final String TAG = "act_export_dialog";
    GlobalHelper gh;
    MySqlHelper mySqlHelper;
    Adapter_Clients_Names adapter;

    RecyclerView recExportNamesList;
    Button btnSelectAll,btnExport;
    TextView tvCount;
    List<Model_Client> allClients = new ArrayList<>();
    boolean clearMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_export_dialog);
        init();
        loadAllClients();
    }

    private void init()
    {
        Log.e(TAG, "init: Begin Init" );
        gh = (GlobalHelper)getApplicationContext();
        mySqlHelper = new MySqlHelper(this);

        recExportNamesList = findViewById(R.id.recExportNamesList);
        recExportNamesList.setItemAnimator(new SlideRightAlphaAnimator());
        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(getResources().getDrawable(R.drawable.divider_vert));
        recExportNamesList.addItemDecoration(itemDecorator);
        btnSelectAll = findViewById(R.id.btnSelectAll);
        btnExport = findViewById(R.id.btnExport);
        tvCount = findViewById(R.id.tvCount);

        btnSelectAll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(clearMode)
                {
                    adapter.ClearAll();
                    btnSelectAll.setText("Отметить всех");
                    clearMode = false;
                }
                else
                    {
                        adapter.selectAll();
                        btnSelectAll.setText("Убрать всех");
                        clearMode = true;
                    }


            }
        });

        btnExport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                makeExport();
            }
        });
    }

    private void loadAllClients()
    {
        Log.e(TAG, "loadAllClients: Begin Load clients");
        int adminId = (int)gh.getAdminInfo().get(GlobalHelper.SQL_COL_ID);
        mySqlHelper.loadAllClients(adminId, new IGetAllMyClients()
        {
            @Override
            public void onSuccess(List<Model_Client> allClients)
            {
                Log.e(TAG, "onSuccess: Get Response from all Jino");
                act_export_dialog.this.allClients = allClients;
                setRecycler();
            }

            @Override
            public void onError(String strError)
            {
                Log.e(TAG, "onError: Error on response from jino" );
            }
        });
    }

    private void setRecycler()
    {
        Log.e(TAG, "setRecycler: Begin Setting Recycler");
        if(allClients.size() == 0)
        {
            Log.e(TAG, "setRecycler: All clients size = 0" );
            return;
        }
        adapter = new Adapter_Clients_Names(act_export_dialog.this, allClients, new INamesListCallback()
        {
            @Override
            public void nameClicked()
            {
                String text = "Выбрано : "+adapter.getAllSelected().size();
                tvCount.setText(text);
            }
        });
        recExportNamesList.setLayoutManager(new LinearLayoutManager(act_export_dialog.this));
        recExportNamesList.setAdapter(adapter);
    }

    private void makeExport()
    {
        List<Model_Client> selected = adapter.getAllSelected();
        if(selected.size() == 0)
        {
            gh.showAlerter("Ошибка","Выберите клиентов для экспорта",this);
            return;
        }

        mySqlHelper.makeExcel(selected, new ISimpleCallback()
        {
            @Override
            public void onSuccess(String response)
            {
                Log.e(TAG, "onSuccess: "+response );
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }

            @Override
            public void onError(String strError)
            {
                gh.showAlerter("Ошибка", "Не удалось создать таблицу Excel, повторите позже",act_export_dialog.this);
            }
        });
    }
}
