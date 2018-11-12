package dimfcompany.com.salesideas;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.ArrayList;

import dimfcompany.com.salesideas.Fragments.frag_analitics;
import dimfcompany.com.salesideas.Fragments.frag_clients;
import dimfcompany.com.salesideas.Fragments.frag_export;
import dimfcompany.com.salesideas.Fragments.frag_import;
import dimfcompany.com.salesideas.Fragments.frag_my_profile;
import dimfcompany.com.salesideas.Helpers.GlobalHelper;

public class act_main extends AppCompatActivity
{
    private static final String TAG = "act_main";

    private GlobalHelper gh;
    Toolbar toolbar;
    DrawerLayout laDrawerRoot;
    TextView tvTitle,tvHamburger;
    FrameLayout laMainFrame;

    LinearLayout laMyProfile,laClients,laAnalitics,laExport,laMailing,laImport;
    ArrayList<LinearLayout> listOfNavLinks = new ArrayList<>();

    TextView tvAdminName,tvAdminEmail;

    private FragmentManager fragManager;
    private FragmentTransaction fragTransaction;
    private Fragment fragClients;
    private Fragment fragMyProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        init();
    }

    private void init()
    {
        gh=(GlobalHelper)getApplicationContext();
        fragManager = act_main.this.getSupportFragmentManager();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        laDrawerRoot = findViewById(R.id.laDrawerRoot);
        tvTitle = findViewById(R.id.tvBarTitle);
        tvHamburger = findViewById(R.id.tvHamburger);
        laMainFrame = findViewById(R.id.frameMain);

        laMyProfile = findViewById(R.id.laMyProfile);
        laClients = findViewById(R.id.laClients);
        laAnalitics = findViewById(R.id.laAnalitics);
        laMailing = findViewById(R.id.laMailing);
        laExport = findViewById(R.id.laExport);
        laImport = findViewById(R.id.laImport);

        listOfNavLinks.add(laMyProfile);
        listOfNavLinks.add(laClients);
        listOfNavLinks.add(laAnalitics);
        listOfNavLinks.add(laMailing);
        listOfNavLinks.add(laExport);
        listOfNavLinks.add(laImport);

        tvAdminName = findViewById(R.id.tvAdminName);
        tvAdminEmail = findViewById(R.id.tvAdminEmail);

        tvAdminName.setText((String)gh.getAdminInfo().get(GlobalHelper.SQL_COL_NAME));
        tvAdminEmail.setText((String)gh.getAdminInfo().get(GlobalHelper.SQL_COL_EMAIL));


        tvHamburger.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                laDrawerRoot.openDrawer(Gravity.START,true);
            }
        });


        laMyProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fragManager = getSupportFragmentManager();

                gh.makeFragChange(fragManager,new frag_my_profile(),laMainFrame);
                changeLinksColor((LinearLayout)view);
                laDrawerRoot.closeDrawer(Gravity.LEFT,true);
            }
        });

        laClients.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fragManager = getSupportFragmentManager();
                gh.makeFragChange(fragManager,new frag_clients(),laMainFrame);
                changeLinksColor((LinearLayout)view);
                laDrawerRoot.closeDrawer(Gravity.LEFT,true);
            }
        });

        laImport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fragManager = getSupportFragmentManager();
                gh.makeFragChange(fragManager,new frag_import(),laMainFrame);
                changeLinksColor((LinearLayout)view);
                laDrawerRoot.closeDrawer(Gravity.LEFT,true);
            }
        });

        laExport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fragManager = getSupportFragmentManager();
                gh.makeFragChange(fragManager,new frag_export(),laMainFrame);
                changeLinksColor((LinearLayout)view);
                laDrawerRoot.closeDrawer(Gravity.LEFT,true);
            }
        });

        laAnalitics.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fragManager = getSupportFragmentManager();
                gh.makeFragChange(fragManager,new frag_analitics(),laMainFrame);
                changeLinksColor((LinearLayout)view);
                laDrawerRoot.closeDrawer(Gravity.LEFT,true);
            }
        });

    }

    public void showAllAfterVkAdd()
    {
        fragManager = getSupportFragmentManager();
        gh.makeFragChange(fragManager,new frag_clients(),laMainFrame);
        changeLinksColor(laClients);
        laDrawerRoot.closeDrawer(Gravity.LEFT,true);
    }

    private void changeLinksColor(LinearLayout clickedLa)
    {
        for(LinearLayout navLa : listOfNavLinks)
        {
            TextView tvIcon = (TextView)navLa.getChildAt(0);
            TextView link = (TextView)navLa.getChildAt(1);
            if(navLa == clickedLa)
            {

                tvIcon.setTextColor(getResources().getColor(R.color.myPink));
                link.setTextColor(getResources().getColor(R.color.myPink));
                continue;
            }

            tvIcon.setTextColor(getResources().getColor(R.color.myLightGray));
            link.setTextColor(getResources().getColor(R.color.myLightGray));
        }
    }

    @Override
    public void onBackPressed()
    {
        if(laDrawerRoot.isDrawerOpen(Gravity.START))
        {
            laDrawerRoot.closeDrawer(Gravity.START,true);
            return;
        }
        super.onBackPressed();
    }

}
