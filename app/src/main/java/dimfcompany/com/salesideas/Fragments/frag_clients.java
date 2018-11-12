package dimfcompany.com.salesideas.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.R;
import dimfcompany.com.salesideas.act_main;


public class frag_clients extends Fragment
{
    GlobalHelper gh;
    act_main parentAct_Main;
    View thisFragmentRootView;
    FrameLayout frameClients;
    LinearLayout laAllClients,laAddClient,laImportCleints;
    List<LinearLayout> listOfBottomLinks = new ArrayList<>();



    private FragmentManager fragManager;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_clients, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        thisFragmentRootView = view;
        init();

        if(gh.isShowVkAdded())
        {
            fragManager = getChildFragmentManager();
            gh.makeFragChange(fragManager,new frag_all_clients2(),frameClients);
            changeColors(laAllClients);
            gh.setShowVkAdded(false);

            gh.showAlerter("Успешно","Добавлено "+gh.getVkAddedNum()+" новых клиентов",parentAct_Main);
        }
    }

    private void init()
    {

        parentAct_Main = (act_main)getActivity();
        gh =(GlobalHelper)parentAct_Main.getApplicationContext();
        fragManager = getChildFragmentManager();

        frameClients = thisFragmentRootView.findViewById(R.id.frameClients);

        laAllClients = thisFragmentRootView.findViewById(R.id.laAllClients);
        laAddClient = thisFragmentRootView.findViewById(R.id.laAddClient);
        laImportCleints = thisFragmentRootView.findViewById(R.id.laImportClients);

        listOfBottomLinks.add(laAllClients);
        listOfBottomLinks.add(laAddClient);
        listOfBottomLinks.add(laImportCleints);

        laAddClient.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fragManager = getChildFragmentManager();
                gh.makeFragChange(fragManager,new frag_add_client(),frameClients);
                changeColors((LinearLayout) view);
            }
        });

        laAllClients.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fragManager = getChildFragmentManager();
                gh.makeFragChange(fragManager,new frag_all_clients2(),frameClients);
                changeColors((LinearLayout) view);
            }
        });

        laImportCleints.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fragManager = getChildFragmentManager();
                gh.makeFragChange(fragManager,new frag_import_vk(),frameClients);
                changeColors((LinearLayout) view);
            }
        });
    }

    private void changeColors(LinearLayout clickedLa)
    {
        for(LinearLayout navLa : listOfBottomLinks)
        {
            TextView tvIcon = (TextView)navLa.getChildAt(1);
            TextView link = (TextView)navLa.getChildAt(2);
            if(navLa == clickedLa)
            {
                tvIcon.setTextColor(getResources().getColor(R.color.myPink));
                link.setTextColor(getResources().getColor(R.color.myPink));
                continue;
            }

            tvIcon.setTextColor(getResources().getColor(R.color.myGray));
            link.setTextColor(getResources().getColor(R.color.myGray));
        }
    }

    public void resetAddClientFrag()
    {
        fragManager = getChildFragmentManager();
        Fragment frag = new frag_add_client();
        fragManager.beginTransaction()
                .replace(frameClients.getId(), frag, frag.getClass().getName())
                .addToBackStack(frag.getClass().getName())
                .commit();
    }
}
