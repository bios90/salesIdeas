package dimfcompany.com.salesideas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ramotion.fluidslider.FluidSlider;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Helpers.MySqlHelper;
import dimfcompany.com.salesideas.Interfaces.IGetUserInterestsCallback;
import dimfcompany.com.salesideas.Interfaces.IGetUserProductsCallback;
import dimfcompany.com.salesideas.Models.Model_Client;
import dimfcompany.com.salesideas.Models.Model_Interest;
import dimfcompany.com.salesideas.Models.Model_Product;

public class act_full_client_dialog extends AppCompatActivity
{
    private static final String TAG = "act_full_client_dialog";

    GlobalHelper gh;
    Model_Client currentClient;

    ArrayAdapter<String> adapterClientType;
    ArrayAdapter<String> adapterGender;

    List<String> listGender;
    List<String> listClientType;
    List<Model_Product> currentProducts = new ArrayList<>();
    List<Model_Interest> currentInterests = new ArrayList<>();
    Calendar currentBirthDay, currentFirstVisit, currentLastVisit;

    Boolean birthDaySetted = false;

    Spinner spinnerGender;
    Spinner spinnerClientType;
    TextView tvDate;
    TextView tvAge;
    TextView tvFirstVisit, tvLastVisit;
    TextView tvForSale;

    FluidSlider saleSlider;

    EditText etName,etNick,etPhone,etComment,etEmail;
    RelativeLayout laAddClient;

    GridLayout gridInterests;
    ExpandableLayout expInterests;
    RelativeLayout laOpenInterests;

    GridLayout gridProducts;
    ExpandableLayout expProducts;
    RelativeLayout laOpenProducts;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_full_client_dialog);
        init();
        loadData();
        loadProducts();
        loadInterests();
        disableElements();
    }

    private void init()
    {
        gh = (GlobalHelper)getApplicationContext();
        currentClient = gh.getCurrentClientToDisplay();

        tvForSale = findViewById(R.id.tvForSale);

        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerClientType = findViewById(R.id.spinnerClientType);
        tvDate = findViewById(R.id.tvDate);
        tvAge = findViewById(R.id.tvAge);

        gridProducts = findViewById(R.id.gridProducts);
        laOpenProducts = findViewById(R.id.laOpenProducts);
        expProducts = findViewById(R.id.expProducts);

        gridInterests = findViewById(R.id.gridInterests);
        laOpenInterests = findViewById(R.id.laOpenInterests);
        expInterests = findViewById(R.id.expInterests);

        tvFirstVisit = findViewById(R.id.tvFirstVisit);
        tvLastVisit = findViewById(R.id.tvLastVisit);

        etName = findViewById(R.id.etName);
        etNick = findViewById(R.id.etNick);
        etPhone = findViewById(R.id.etPhone);
        etComment = findViewById(R.id.etComment);
        etEmail = findViewById(R.id.etEmail);

        saleSlider = findViewById(R.id.sale_Slider);
        laAddClient = findViewById(R.id.laBtnAddClient);

        listGender = Arrays.asList(getResources().getStringArray(R.array.Gender));
        listClientType = Arrays.asList(getResources().getStringArray(R.array.ClientCategory));

        adapterGender = new ArrayAdapter<String>(act_full_client_dialog.this,R.layout.my_spinner,listGender);
        spinnerGender.setAdapter(adapterGender);

        adapterClientType = new ArrayAdapter<String>(act_full_client_dialog.this,R.layout.my_spinner,listClientType);
        spinnerClientType.setAdapter(adapterClientType);

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

    private void loadData()
    {
        etName.setText(currentClient.getName());
        etNick.setText(currentClient.getNick());
        spinnerGender.setSelection(currentClient.getGender());
        tvDate.setText(GlobalHelper.dateFormatForDisplay.format(currentClient.getBirthday()));
        tvAge.setText(gh.getAge(currentClient.getBirthday()));
        spinnerClientType.setSelection(currentClient.getCategory());
        etPhone.setText(currentClient.getPhone());
        etEmail.setText(currentClient.getEmail());
        tvFirstVisit.setText(GlobalHelper.dateFormatForDisplay.format(currentClient.getFirstVisit()));
        tvLastVisit.setText(GlobalHelper.dateFormatForDisplay.format(currentClient.getLastVisit()));
        tvForSale.setText(currentClient.getSale()+"%");
        etComment.setText(currentClient.getComment());
    }

    private void disableElements()
    {
        etName.setEnabled(false);
        etNick.setEnabled(false);
        spinnerGender.setEnabled(false);
        spinnerClientType.setEnabled(false);
        etPhone.setEnabled(false);
        etEmail.setEnabled(false);
        etComment.setEnabled(false);

//        Log.e(TAG, "disableElements: User Size of interests "+currentClient.getInterestIds().size() );
//        for(int  interestId:currentClient.getInterestIds())
//        {
//            Log.e(TAG, "disableElements: id of interest is"+interestId );
//        }


    }


    private void disableInterests()
    {
        for(Model_Interest interest : currentInterests)
        {
            Log.e(TAG, "Current INTEREST id "+interest.getId() );

            int id = interest.getId();
            if(!currentClient.getInterestIds().contains(id))
            {
                RelativeLayout laForTogg = (RelativeLayout)(interest.getConnectedTogg().getParent());
                int viewId = gridInterests.indexOfChild(laForTogg);
                gridInterests.removeViewAt(viewId);
            }else
            {
                interest.getConnectedTogg().setChecked(true);
                RelativeLayout laForTogg = (RelativeLayout)(interest.getConnectedTogg().getParent());
                laForTogg.setEnabled(false);
            }
        }
    }


    private void disableProducts()
    {
        for(Model_Product product : currentProducts)
        {
            int id = product.getId();
            if(!currentClient.getProductIds().contains(id))
            {
                RelativeLayout laForTogg = (RelativeLayout)(product.getConnectedTogg().getParent());
                int viewId = gridProducts.indexOfChild(laForTogg);
                gridProducts.removeViewAt(viewId);
            }
            else
            {
                product.getConnectedTogg().setChecked(true);
                RelativeLayout laForTogg = (RelativeLayout)(product.getConnectedTogg().getParent());
                laForTogg.setEnabled(false);
            }
        }
    }

    private void loadProducts()
    {
        MySqlHelper mySqlHelper = new MySqlHelper(act_full_client_dialog.this);
        mySqlHelper.loadProducts( new IGetUserProductsCallback()
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


    private void loadInterests()
    {
        MySqlHelper mySqlHelper = new MySqlHelper(act_full_client_dialog.this);
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

            }
        });
    }

    private void displayInterests()
    {
        gridInterests.removeAllViews();
        for(Model_Interest interest : currentInterests)
        {
            final View productView = getLayoutInflater().inflate(R.layout.item_with_checkbox,gridInterests,false);
            TextView tvText = productView.findViewById(R.id.tvText);
            final ToggleButton togg = productView.findViewById(R.id.togg);

            interest.setConnectedTogg(togg);

            productView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    togg.toggle();
                }
            });
            tvText.setText(interest.getText());
            gridInterests.addView(productView);
        }

        disableInterests();
    }

    private void displayProducts()
    {
        gridProducts.removeAllViews();
        for(Model_Product product : currentProducts)
        {
            final View productView = getLayoutInflater().inflate(R.layout.item_with_checkbox,gridProducts,false);
            TextView tvText = productView.findViewById(R.id.tvText);
            final ToggleButton togg = productView.findViewById(R.id.togg);

            product.setConnectedTogg(togg);

            productView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    togg.toggle();
                }
            });
            tvText.setText(product.getText());
            gridProducts.addView(productView);
        }

        disableProducts();
    }
}
