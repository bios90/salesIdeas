package dimfcompany.com.salesideas.Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dimfcompany.com.salesideas.Helpers.Compare_Helper;
import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Helpers.MySqlHelper;
import dimfcompany.com.salesideas.Interfaces.IGetAllMyClients;
import dimfcompany.com.salesideas.Models.Model_Client;
import dimfcompany.com.salesideas.R;
import dimfcompany.com.salesideas.act_full_client_info;
import dimfcompany.com.salesideas.act_main;

public class frag_all_clients extends Fragment
{
    private static final String TAG = "frag_all_clients";

    act_main parentActMain;
    frag_clients parentFragClients;
    GlobalHelper gh;
    View thisFragmentRootView;
    LinearLayout laForAllClients;
    Drawable drawMale,drawFemale;
    EditText etSearch;
    Compare_Helper compare_helper;

    List<Model_Client> currentClientsToShow = new ArrayList<>();
    List<Model_Client> allClients = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_all_clients,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        thisFragmentRootView = view;
        init();
        loadAll();
    }


    private void init()
    {
        parentActMain = (act_main) getActivity();
        parentFragClients = ((frag_clients)frag_all_clients.this.getParentFragment());
        gh = (GlobalHelper)parentActMain.getApplicationContext();
        compare_helper = new Compare_Helper(parentActMain);
        laForAllClients = thisFragmentRootView.findViewById(R.id.laForAllClients);

        drawMale = getResources().getDrawable(R.drawable.ic_user_male);
        drawFemale = getResources().getDrawable(R.drawable.ic_user_female);

        etSearch = thisFragmentRootView.findViewById(R.id.etSearch);

        etSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String newText = etSearch.getText().toString();

                if(newText.isEmpty())
                {
                    currentClientsToShow = allClients;
                    displayClients();
                    return;
                }
                Log.e(TAG, "onTextChanged: "+newText);

                currentClientsToShow = compare_helper.clietsMatchSearch(allClients,newText);
                displayClients();
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }


    private void loadAll()
    {
        int adminId = (int)gh.getAdminInfo().get(GlobalHelper.SQL_COL_ID);
        MySqlHelper mySqlHelper = new MySqlHelper(parentActMain);
        mySqlHelper.loadAllClients(adminId, new IGetAllMyClients()
        {
            @Override
            public void onSuccess(List<Model_Client> allClients)
            {
                frag_all_clients.this.allClients = allClients;
                currentClientsToShow = allClients;
                displayClients();
            }

            @Override
            public void onError(String strError)
            {

            }
        });
    }

    private void displayClients()
    {
        laForAllClients.removeAllViews();
        for(final Model_Client client : currentClientsToShow)
        {
            final View clientCardView = parentActMain.getLayoutInflater().inflate(R.layout.item_client_card,laForAllClients,false);
            LinearLayout phoneRow = clientCardView.findViewById(R.id.laPhoneRow);
            LinearLayout emailRow = clientCardView.findViewById(R.id.laEmailRow);
            LinearLayout birthdayRow = clientCardView.findViewById(R.id.laBirthdayRow);

            TextView tvPhone = clientCardView.findViewById(R.id.tvPhone);
            TextView tvEmail = clientCardView.findViewById(R.id.tvEmail);
            TextView tvBirthday = clientCardView.findViewById(R.id.tvBirthday);
            TextView tvName = clientCardView.findViewById(R.id.tvName);

            tvName.setText(client.getName());

            if(client.getPhone().isEmpty())
            {
                phoneRow.setVisibility(View.INVISIBLE);
            }
            else
                {
                    tvPhone.setText(client.getPhone());
                }

            if(client.getEmail().isEmpty())
            {
                emailRow.setVisibility(View.INVISIBLE);
            }
            else
                {
                    tvEmail.setText(client.getEmail());
                }

            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
            if(fmt.format(client.getBirthday()).equals(fmt.format(new Date(0))))
            {
                birthdayRow.setVisibility(View.INVISIBLE);
            }
            else
                {
                    String birthAge = GlobalHelper.dateFormatForDisplay.format(client.getBirthday());
                    tvBirthday.setText(birthAge);
                }

            client.setCardView(clientCardView);
            CircleImageView imgAvatart = clientCardView.findViewById(R.id.avatar);
            if(client.getGender() == 0 )
            {
                imgAvatart.setImageDrawable(drawMale);
            }
            else
                {
                    imgAvatart.setImageDrawable(drawFemale);
                }


            phoneRow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                            "tel", client.getPhone(), null));
                    startActivity(intent);
                }
            });

            clientCardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    gh.setCurrentClientToDisplay(client);
                    Intent intent = new Intent(parentActMain,act_full_client_info.class);
                    startActivity(intent);
                }
            });

            laForAllClients.addView(clientCardView);
        }
    }
}
