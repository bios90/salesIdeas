package dimfcompany.com.salesideas.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

public class frag_my_profile extends Fragment
{
    private static final String TAG = "frag_my_profile";

    act_main parentActMain;
    GlobalHelper gh;
    View thisFragmentRootView;


    private FrameLayout frameMyProfile;
    private FragmentManager fragManager;
    private FragmentTransaction fragTransaction;
    private Fragment fragProduct;



    LinearLayout laProfile,laProduct,laInterest;
    List<LinearLayout> listOfBottomLinks = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        Log.e(TAG, "onCreateView: profile on CreateView" );
        return inflater.inflate(R.layout.frag_my_profile,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        Log.e(TAG, "onViewCreated: Profile created" );
        super.onViewCreated(view, savedInstanceState);
        thisFragmentRootView = view;
        init();
    }

    private void init()
    {
        parentActMain = (act_main)getActivity();
        gh = (GlobalHelper) parentActMain.getApplicationContext();


        frameMyProfile = thisFragmentRootView.findViewById(R.id.frameMyProfile);
        laProfile = thisFragmentRootView.findViewById(R.id.laProfile);
        laProduct = thisFragmentRootView.findViewById(R.id.laProduct);
        laInterest = thisFragmentRootView.findViewById(R.id.laInterest);
        listOfBottomLinks.add(laProfile);
        listOfBottomLinks.add(laProduct);
        listOfBottomLinks.add(laInterest);

        laProduct.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                frameMyProfile = thisFragmentRootView.findViewById(R.id.frameMyProfile);
                fragManager = getChildFragmentManager();
                gh.makeFragChange(fragManager,new frag_product(),frameMyProfile);
                changeColors((LinearLayout) view);
            }
        });

        laInterest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                frameMyProfile = thisFragmentRootView.findViewById(R.id.frameMyProfile);
                fragManager = getChildFragmentManager();
                gh.makeFragChange(fragManager,new frag_interest(),frameMyProfile);
                changeColors((LinearLayout) view);
            }
        });

        laProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                frameMyProfile = thisFragmentRootView.findViewById(R.id.frameMyProfile);
                fragManager = getChildFragmentManager();
                gh.makeFragChange(fragManager,new frag_my_accaunt(),frameMyProfile);
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

    public void resetProductsFrag()
    {
        fragManager = getChildFragmentManager();
        Fragment frag = new frag_product();
        fragManager.beginTransaction()
                .replace(frameMyProfile.getId(), frag, frag.getClass().getName())
                .addToBackStack(frag.getClass().getName())
                .commit();
    }

    public void resetInterestFrag()
    {
        fragManager = getChildFragmentManager();
        Fragment frag = new frag_interest();
        fragManager.beginTransaction()
                .replace(frameMyProfile.getId(), frag, frag.getClass().getName())
                .addToBackStack(frag.getClass().getName())
                .commit();
    }

    public void resetMyAccaunt()
    {
        fragManager = getChildFragmentManager();
        Fragment frag = new frag_my_accaunt();
        fragManager.beginTransaction()
                .replace(frameMyProfile.getId(), frag, frag.getClass().getName())
                .addToBackStack(frag.getClass().getName())
                .commit();
    }
}
