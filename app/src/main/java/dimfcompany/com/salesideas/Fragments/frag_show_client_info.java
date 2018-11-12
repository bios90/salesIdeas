package dimfcompany.com.salesideas.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.FlowLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.flexbox.FlexboxLayout;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Helpers.MySqlHelper;
import dimfcompany.com.salesideas.Interfaces.IGetUserInterestsCallback;
import dimfcompany.com.salesideas.Interfaces.IGetUserProductsCallback;
import dimfcompany.com.salesideas.Models.Model_Client;
import dimfcompany.com.salesideas.Models.Model_Interest;
import dimfcompany.com.salesideas.Models.Model_Product;
import dimfcompany.com.salesideas.R;
import dimfcompany.com.salesideas.act_full_client_dialog;
import dimfcompany.com.salesideas.act_full_client_info;

public class frag_show_client_info extends Fragment
{
    private static final String TAG = "frag_show_client_info";
    Model_Client currentClient;
    GlobalHelper gh;
    View thisFragmentRootView;
    act_full_client_info actParent;
    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
    String[] arrClientCategs;
    List<Model_Product> currentProducts = new ArrayList<>();
    List<Model_Interest> currentInterests = new ArrayList<>();


    LinearLayout laPhoneRow,laMailRow,laBdayRow,laAgeRow,laCategRow,laFirstVisit,laLastVisitRow,laSaleRow,laCommentRow,laProductsRow,laInterestsRow,laVkRow,laInstagramRow,laCityRow,laDistrictRow;
    TextView tvPhone,tvMail,tvBday,tvAge,tvCateg,tvFirstVisit,tvLastVisit,tvSale,tvComment,tvVk,tvInstagram,tvCity,tvDistrict;
    TextView tvName;
    FlexboxLayout flexInterests,flexProducts;
    ExpandableLayout expProducts, expInterests;
    RelativeLayout laOpenProducts,laOpenInterests;
    RelativeLayout laEditClient;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_show_user_info, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        thisFragmentRootView = view;
        init();
        displayClient();
        loadProducts();
        loadInterests();
    }



    private void init()
    {
        actParent = (act_full_client_info)getActivity();
        gh = (GlobalHelper)actParent.getApplicationContext();
        currentClient = gh.getCurrentClientToDisplay();
        arrClientCategs = getResources().getStringArray(R.array.ClientCategory);

        laPhoneRow = thisFragmentRootView.findViewById(R.id.laPhoneRow);
        laMailRow = thisFragmentRootView.findViewById(R.id.laMailRow);
        laBdayRow = thisFragmentRootView.findViewById(R.id.laBdayRow);
        laAgeRow = thisFragmentRootView.findViewById(R.id.laAgeRow);
        laCategRow = thisFragmentRootView.findViewById(R.id.laCategRow);
        laFirstVisit = thisFragmentRootView.findViewById(R.id.laFirstVisit);
        laLastVisitRow = thisFragmentRootView.findViewById(R.id.laLastVisitRow);
        laSaleRow = thisFragmentRootView.findViewById(R.id.laSaleRow);
        laCommentRow = thisFragmentRootView.findViewById(R.id.laCommentRow);
        laProductsRow = thisFragmentRootView.findViewById(R.id.laProductsRow);
        laInterestsRow = thisFragmentRootView.findViewById(R.id.laInterestsRow);


        laVkRow = thisFragmentRootView.findViewById(R.id.laVKRow);
        laInstagramRow = thisFragmentRootView.findViewById(R.id.laInstagramRow);
        laCityRow = thisFragmentRootView.findViewById(R.id.laCityRow);
        laDistrictRow = thisFragmentRootView.findViewById(R.id.laDistrictRow);



        tvName = thisFragmentRootView.findViewById(R.id.tvName);
        tvPhone = thisFragmentRootView.findViewById(R.id.tvPhone);
        tvMail = thisFragmentRootView.findViewById(R.id.tvMail);
        tvBday = thisFragmentRootView.findViewById(R.id.tvBday);
        tvAge = thisFragmentRootView.findViewById(R.id.tvAge);
        tvCateg = thisFragmentRootView.findViewById(R.id.tvCateg);
        tvFirstVisit = thisFragmentRootView.findViewById(R.id.tvFirstVisit);
        tvLastVisit = thisFragmentRootView.findViewById(R.id.tvLastVisit);
        tvSale = thisFragmentRootView.findViewById(R.id.tvSale);
        tvComment = thisFragmentRootView.findViewById(R.id.tvComment);


        tvVk = thisFragmentRootView.findViewById(R.id.tvVkId);
        tvInstagram = thisFragmentRootView.findViewById(R.id.tvInstagram);
        tvCity = thisFragmentRootView.findViewById(R.id.tvCity);
        tvDistrict = thisFragmentRootView.findViewById(R.id.tvDistrict);


        flexInterests = thisFragmentRootView.findViewById(R.id.flexInterests);
        flexProducts = thisFragmentRootView.findViewById(R.id.flexProducts);

        laOpenInterests = thisFragmentRootView.findViewById(R.id.laOpenInterests);
        laOpenProducts = thisFragmentRootView.findViewById(R.id.laOpenProducts);

        expInterests = thisFragmentRootView.findViewById(R.id.expInterests);
        expProducts = thisFragmentRootView.findViewById(R.id.expProducts);


        laEditClient = thisFragmentRootView.findViewById(R.id.laEditClient);

        laEditClient.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                actParent.loadEditClient();
            }
        });

        laOpenProducts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                expProducts.toggle();
            }
        });
        laOpenInterests.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                expInterests.toggle();
            }
        });
    }


    private void displayClient()
    {
        tvName.setText(currentClient.getName());

        if(currentClient.getPhone().isEmpty())
        {
            laPhoneRow.setVisibility(View.GONE);
        }
        else
            {
                tvPhone.setText(currentClient.getPhone());
            }

        if(currentClient.getEmail().isEmpty())
        {
            laMailRow.setVisibility(View.GONE);
        }
        else
        {
            tvMail.setText(currentClient.getEmail());
        }

        if(currentClient.getEmail().isEmpty())
        {
            laMailRow.setVisibility(View.GONE);
        }
        else
        {
            tvMail.setText(currentClient.getEmail());
        }


        if(fmt.format(currentClient.getBirthday()).equals(fmt.format(new Date(0))))
        {
            laBdayRow.setVisibility(View.GONE);
            laAgeRow.setVisibility(View.GONE);
        }
        else
        {
            String birthAge = GlobalHelper.dateFormatForDisplay.format(currentClient.getBirthday());
            tvBday.setText(birthAge);

            String ageStr = gh.getAge(currentClient.getBirthday());
            tvAge.setText(ageStr);
        }

        tvCateg.setText(arrClientCategs[currentClient.getCategory()]);


        if(fmt.format(currentClient.getFirstVisit()).equals(fmt.format(new Date(0))))
        {
            laFirstVisit.setVisibility(View.GONE);
        }
        else
        {
            String firstVisit = GlobalHelper.dateFormatForDisplay.format(currentClient.getFirstVisit());
            tvFirstVisit.setText(firstVisit);
        }




        if(fmt.format(currentClient.getLastVisit()).equals(fmt.format(new Date(0))))
        {
            laLastVisitRow.setVisibility(View.GONE);
        }
        else
        {
            String lastVisit = GlobalHelper.dateFormatForDisplay.format(currentClient.getLastVisit());
            tvFirstVisit.setText(lastVisit);
        }

        tvSale.setText(currentClient.getSale()+"%");

        if(currentClient.getComment().isEmpty())
        {
            laCommentRow.setVisibility(View.GONE);
        }
        else
            {
                tvComment.setText(currentClient.getComment());
            }




        if(currentClient.getVkId().isEmpty())
        {
            laVkRow.setVisibility(View.GONE);
        }
        else
            {
                tvVk.setText("id"+currentClient.getVkId());
            }


        if(currentClient.getNick().isEmpty())
        {
            laInstagramRow.setVisibility(View.GONE);
        }
        else
            {
                tvInstagram.setText(currentClient.getNick());
            }


        if(currentClient.getCity().isEmpty())
        {
            laCityRow.setVisibility(View.GONE);
        }
        else
        {
            tvCity.setText(currentClient.getCity());
        }

        if(currentClient.getDistrict().isEmpty())
        {
            laDistrictRow.setVisibility(View.GONE);
        }
        else
        {
            tvDistrict.setText(currentClient.getDistrict());
        }
    }


    private void loadProducts()
    {
        MySqlHelper mySqlHelper = new MySqlHelper(actParent);
        mySqlHelper.loadProducts(new IGetUserProductsCallback()
        {
            @Override
            public void onSuccess(List<Model_Product> userProducts)
            {
                currentProducts = userProducts;
                displayProducts();
            }

            @Override
            public void onError(String strError)
            {

            }
        });
    }

    private void displayProducts()
    {

        flexProducts.removeAllViews();
        for(Model_Product product : currentProducts)
        {
            int id = product.getId();
            if(!currentClient.getProductIds().contains(id))
            {
                continue;
            }
            final View productView = actParent.getLayoutInflater().inflate(R.layout.item_prodinter,flexProducts,false);
            TextView tvText = productView.findViewById(R.id.tvText);

            tvText.setText(product.getText());
            flexProducts.addView(productView);
        }

        if(flexProducts.getChildCount() == 0)
        {
            laProductsRow.setVisibility(View.GONE);
            return;
        }
    }


    private void loadInterests()
    {
        MySqlHelper mySqlHelper = new MySqlHelper(actParent);
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

            }
        });
    }


    private void displayInterests()
    {
        flexInterests.removeAllViews();
        for(Model_Interest interest : currentInterests)
        {
            int id = interest.getId();
            if(!currentClient.getInterestIds().contains(id))
            {
                continue;
            }
            final View productView = actParent.getLayoutInflater().inflate(R.layout.item_prodinter,flexInterests,false);
            TextView tvText = productView.findViewById(R.id.tvText);

            tvText.setText(interest.getText());
            flexInterests.addView(productView);
        }
        if(flexInterests.getChildCount() == 0)
        {
            laInterestsRow.setVisibility(View.GONE);
            return;
        }

    }


}
