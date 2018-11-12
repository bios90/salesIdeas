package dimfcompany.com.salesideas.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Helpers.MySqlHelper;
import dimfcompany.com.salesideas.Interfaces.IGetUserInterestsCallback;
import dimfcompany.com.salesideas.Interfaces.ISimpleCallback;
import dimfcompany.com.salesideas.Models.Model_Interest;
import dimfcompany.com.salesideas.R;
import dimfcompany.com.salesideas.act_edit_interest_dialog;
import dimfcompany.com.salesideas.act_edit_prod_dialog;
import dimfcompany.com.salesideas.act_main;

public class frag_interest extends Fragment
{
    private static final String TAG = "frag_interest";

    act_main parentActMain;
    GlobalHelper gh;
    View thisFragmentRootView;
    List<Model_Interest> currentInterests = new ArrayList<>();
    MySqlHelper mySqlHelper;
    frag_my_profile parentFrag;

    private static int INTEREST_EDIT_RESULT_CODE = 9001;


    EditText etAddInterest;
    Button btnAddInterest;
    LinearLayout laForInterests;
    RelativeLayout laExpandInterests;
    ExpandableLayout expInterests;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_interests,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        thisFragmentRootView = view;
        init();
        loadInterests();
    }



    private void init()
    {
        parentActMain = (act_main)getActivity();
        gh = (GlobalHelper)parentActMain.getApplicationContext();
        mySqlHelper = new MySqlHelper(parentActMain);
        parentFrag = (frag_my_profile)frag_interest.this.getParentFragment();

        btnAddInterest = thisFragmentRootView.findViewById(R.id.btnAddInterest);
        etAddInterest = thisFragmentRootView.findViewById(R.id.etAddInterest);
        laForInterests = thisFragmentRootView.findViewById(R.id.laForInterests);
        laExpandInterests = thisFragmentRootView.findViewById(R.id.laExpandInterests);
        expInterests = thisFragmentRootView.findViewById(R.id.expInterests);


        btnAddInterest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String newInterest = etAddInterest.getText().toString();
                if(newInterest == null || newInterest.isEmpty())
                {
                    gh.showAlerter("Ошибка","Введите название нового интереса",parentActMain);
                    return;
                }

                Map<String,Object> userInfo = gh.getAdminInfo();
                int id = (int)userInfo.get(GlobalHelper.SQL_COL_ID);
                MySqlHelper mySqlHelper = new MySqlHelper(parentActMain);
                mySqlHelper.addInterest(newInterest, id, new ISimpleCallback()
                {
                    @Override
                    public void onSuccess(String response)
                    {
                        if(response.equals("error"))
                        {
                            gh.showAlerter("Ошибка","Не удалось добавить интерес, повторите позже",parentActMain);
                            return;
                        }

                        if(response.equals("true"))
                        {
                            etAddInterest.setText("");
                            gh.showAlerter("Ура!", "Новый интерес успешно добавлен", parentActMain);
                            loadInterests();
                            return;
                        }
                    }

                    @Override
                    public void onError(String strError)
                    {
                        gh.showAlerter("Ошибка","Не удалось добавить интерес, повторите позже",parentActMain);
                        Log.e(TAG, "onError: "+ strError);
                    }
                });

            }
        });



        laExpandInterests.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                expInterests.toggle();
            }
        });
    }


    private void loadInterests()
    {
        MySqlHelper mySqlHelper = new MySqlHelper(parentActMain);
        int id = (int)gh.getAdminInfo().get(GlobalHelper.SQL_COL_ID);
        mySqlHelper.loadInterests( new IGetUserInterestsCallback()
        {
            @Override
            public void onSuccess(List<Model_Interest> userInterests)
            {
                currentInterests = userInterests;
                displayInterests();
            }

            @Override
            public void onError(String strError)
            {
                gh.showAlerter("Ошибка","Не удалось загрузить список интересов, повторите позже.",parentActMain);
            }
        });
    }

    private void displayInterests()
    {
        laForInterests.removeAllViews();
        for(final Model_Interest interest : currentInterests)
        {
            final View productView = parentActMain.getLayoutInflater().inflate(R.layout.item_oneline,laForInterests,false);
            TextView tvText = productView.findViewById(R.id.tvText);
            tvText.setText(interest.getText());

            TextView tvEdit = productView.findViewById(R.id.tvEdit);
            TextView tvDelete = productView.findViewById(R.id.tvDelete);

            tvDelete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    mySqlHelper.deleteInterest(interest.getId(), new ISimpleCallback()
                    {
                        @Override
                        public void onSuccess(String response)
                        {
                            if(response.equals("true"))
                            {
                                gh.showAlerter("Успешно","Интерес удален",parentActMain);
                                parentFrag.resetInterestFrag();
                            }
                        }

                        @Override
                        public void onError(String strError)
                        {

                        }
                    });
                }
            });

            tvEdit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    gh.setCurrentInterestToEdit(interest);
                    Intent intent = new Intent(parentActMain,act_edit_interest_dialog.class);
                    startActivityForResult(intent,INTEREST_EDIT_RESULT_CODE);
                }
            });

            laForInterests.addView(productView);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == INTEREST_EDIT_RESULT_CODE && resultCode == Activity.RESULT_OK)
        {
            gh.showAlerter("Успешно","Интерес успешно изменен",parentActMain);
            parentFrag.resetInterestFrag();
        }
    }
}
