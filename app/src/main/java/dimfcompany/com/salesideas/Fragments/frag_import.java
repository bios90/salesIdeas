package dimfcompany.com.salesideas.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.R;
import dimfcompany.com.salesideas.act_main;

public class frag_import extends Fragment
{
    GlobalHelper gh;
    act_main parentAct_Main;
    View thisFragmentRootView;
    FrameLayout frameImport;
    LinearLayout laImportVk,laImportContacts,laImportExcel;
    List<LinearLayout> listOfBottomLinks = new ArrayList<>();



    private FragmentManager fragManager;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_import, container,false);
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
        parentAct_Main = (act_main)getActivity();
        gh =(GlobalHelper)parentAct_Main.getApplicationContext();
        fragManager = getChildFragmentManager();

        frameImport = thisFragmentRootView.findViewById(R.id.frameImport);

        laImportVk = thisFragmentRootView.findViewById(R.id.laImportVk);
        laImportContacts = thisFragmentRootView.findViewById(R.id.laImportContacts);
        laImportExcel = thisFragmentRootView.findViewById(R.id.laImportExcel);

        listOfBottomLinks.add(laImportVk);
        listOfBottomLinks.add(laImportContacts);
        listOfBottomLinks.add(laImportExcel);

        laImportVk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                gh.makeFragChange(fragManager,new frag_import_vk2(),frameImport);
                gh.changeLinkColors((LinearLayout)view,listOfBottomLinks);
            }
        });

        laImportContacts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                gh.makeFragChange(fragManager,new frag_import_contacts(),frameImport);
                gh.changeLinkColors((LinearLayout)view,listOfBottomLinks);
            }
        });

        laImportExcel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                gh.makeFragChange(fragManager,new frag_import_excel(),frameImport);
                gh.changeLinkColors((LinearLayout)v,listOfBottomLinks);
            }
        });
    }
}
