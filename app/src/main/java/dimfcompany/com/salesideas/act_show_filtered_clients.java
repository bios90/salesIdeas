package dimfcompany.com.salesideas;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.mikepenz.itemanimators.SlideRightAlphaAnimator;

import java.util.ArrayList;
import java.util.List;

import dimfcompany.com.salesideas.Adapters.Adapter_AllClients;
import dimfcompany.com.salesideas.Helpers.Compare_Helper;
import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Interfaces.IClientCardClicked;
import dimfcompany.com.salesideas.Models.Model_Client;

public class act_show_filtered_clients extends AppCompatActivity
{
    private static final String TAG = "act_show_filtered_clien";
    GlobalHelper gh;
    List<Model_Client> clientsToShow;
    RecyclerView recAllClients;
    Drawable drawMale,drawFemale;
    EditText etSearch;
    Compare_Helper compare_helper;
    AlertDialog spotsDialog;

    Adapter_AllClients adapter;

    private static final int CARD_CLICKED_INTENT = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_show_filtered_clients);
        init();
        setRecycler();
    }

    private void init()
    {
        gh = (GlobalHelper)getApplicationContext();
        clientsToShow = gh.getCurrentSorteredClients();
        compare_helper = new Compare_Helper(this);
        recAllClients = findViewById(R.id.recAllClients);
        recAllClients.setItemAnimator(new SlideRightAlphaAnimator());

        drawMale = getResources().getDrawable(R.drawable.ic_user_male);
        drawFemale = getResources().getDrawable(R.drawable.ic_user_female);

        etSearch = findViewById(R.id.etSearch);

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

    private void setRecycler()
    {
        Log.e(TAG, "setRecycler: Begin set Recycler" );

        adapter = new Adapter_AllClients(this, clientsToShow, new IClientCardClicked()
        {
            @Override
            public void cardClicked(Model_Client client)
            {
                gh.setCurrentClientToDisplay(client);
                Intent intent = new Intent(act_show_filtered_clients.this,act_full_client_info.class);
                startActivityForResult(intent, CARD_CLICKED_INTENT);
            }

            @Override
            public void phoneClicked(Model_Client client)
            {
                Toast.makeText(act_show_filtered_clients.this, "Phone clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void emailClicked(Model_Client client)
            {

            }
        });

        recAllClients.setLayoutManager(new LinearLayoutManager(this));
        recAllClients.setAdapter(adapter);
        if(spotsDialog != null)
        {
            spotsDialog.dismiss();
        }
    }
}
