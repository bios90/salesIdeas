package dimfcompany.com.salesideas;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import dimfcompany.com.salesideas.Fragments.frag_clients;
import dimfcompany.com.salesideas.Fragments.frag_edit_client;
import dimfcompany.com.salesideas.Fragments.frag_show_client_info;
import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Models.Model_Client;

public class act_full_client_info extends AppCompatActivity
{
    private static final String TAG = "act_full_client_info";
    Model_Client currentClient;
    FrameLayout frameFullClientInfo;
    GlobalHelper gh;
    FragmentManager fragManager;

    boolean changesMade;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_full_client_info);


        init();
        loadShowInfo();
    }

    private void init()
    {
        gh = (GlobalHelper)getApplicationContext();
        currentClient = gh.getCurrentClientToDisplay();
        frameFullClientInfo = findViewById(R.id.frameFullClientInfo);
        fragManager = getSupportFragmentManager();
        changesMade = getIntent().getBooleanExtra("changesMade",false);
    }


    public void loadShowInfo()
    {
        gh.makeFragChangeWithoutBackStack(fragManager,new frag_show_client_info(),frameFullClientInfo);
    }

    public void loadEditClient()
    {
        gh.makeFragChangeWithoutBackStack(fragManager,new frag_edit_client(),frameFullClientInfo);
    }

    public void restartActivity()
    {
        loadShowInfo();
//        Intent intent = getIntent();
//        intent.putExtra("changesMade",changesMade);
//        finish();
//        startActivity(intent);
    }

    public boolean isChangesMade()
    {
        return changesMade;
    }

    public void setChangesMade(boolean changesMade)
    {
        this.changesMade = changesMade;
    }

    @Override
    public void onBackPressed()
    {
        Intent returnIntent = new Intent();
        if(changesMade)
        {
            Log.e(TAG, "onBackPressed: Chanhes made true" );
            setResult(Activity.RESULT_OK,returnIntent);
        }
        else
            {
                Log.e(TAG, "onBackPressed: Changes made false" );
                setResult(Activity.RESULT_CANCELED,returnIntent);
            }
        finish();

    }
}
