package dimfcompany.com.salesideas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Helpers.MySqlHelper;
import dimfcompany.com.salesideas.Interfaces.IGetUserInterestsCallback;
import dimfcompany.com.salesideas.Interfaces.IGetUserProductsCallback;
import dimfcompany.com.salesideas.Interfaces.ISimpleCallback;
import dimfcompany.com.salesideas.Models.Model_Filter;
import dimfcompany.com.salesideas.Models.Model_Interest;
import dimfcompany.com.salesideas.Models.Model_Product;

public class act_new_filter extends AppCompatActivity
{
    private static final String TAG = "act_new_filter";
    GlobalHelper gh;

    EditText etName;
    TextView tvCateg;
    Spinner spinnerGender;
    EditText etCity,etDistrict;
    EditText etAgeOt,etAgeDo;
    EditText etSaleOt,etSaleDo;
    Spinner spinnerPhone;
    Spinner spinnerEmail;
    RelativeLayout laOpenInterests;
    RelativeLayout laOpenProducts;
    ExpandableLayout expInterests;
    ExpandableLayout expProducts;
    GridLayout gridInterests;
    GridLayout gridProducts;

    List<Model_Product> allProducts = new ArrayList<>();
    List<Model_Interest> allInterests = new ArrayList<>();
    Button btnOk,btnCancel;

    int cat_any = 0;
    int cat_new = 0;
    int cat_postoyan = 0;
    int cat_vip = 0;

    private static final int CATEG_CHOOSE_CODE = 7777;
    AlertDialog spotsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_new_filter);
        init();
        loadAllProducts();
        loadInterests();
    }

    private void init()
    {
        gh = (GlobalHelper)getApplicationContext();
        etName = findViewById(R.id.etName);
        tvCateg = findViewById(R.id.tvCateg);
        spinnerGender = findViewById(R.id.spinnerGender);
        etCity = findViewById(R.id.etCity);
        etDistrict = findViewById(R.id.etDistrict);
        etAgeOt = findViewById(R.id.etAgeOt);
        etAgeDo = findViewById(R.id.etAgeDo);
        etSaleOt = findViewById(R.id.etSaleOt);
        etSaleDo = findViewById(R.id.etSaleDo);
        spinnerPhone = findViewById(R.id.spinnerPhone);
        spinnerEmail = findViewById(R.id.spinnerEmail);
        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);
        gridInterests = findViewById(R.id.gridInterests);
        gridProducts = findViewById(R.id.gridProducts);
        expProducts = findViewById(R.id.expProducts);
        expInterests = findViewById(R.id.expInterests);
        laOpenInterests = findViewById(R.id.laOpenInterests);
        laOpenProducts = findViewById(R.id.laOpenProducts);

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

        ArrayAdapter adapterGender = new ArrayAdapter<String>(this,R.layout.my_spinner,new String[]{"Мужской","Женский","Неважно"});
        spinnerGender.setAdapter(adapterGender);
        spinnerGender.setSelection(2);

        ArrayAdapter adapterPhoneMail = new ArrayAdapter<String>(this,R.layout.my_spinner,new String[]{"Неважно","Указан","Не указан"});
        spinnerPhone.setAdapter(adapterPhoneMail);
        spinnerEmail.setAdapter(adapterPhoneMail);

        tvCateg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(act_new_filter.this,act_categ_type.class);
                startActivityForResult(intent,CATEG_CHOOSE_CODE);
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                makeAdd();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });
    }


    //region Load and Display Products
    private void loadAllProducts()
    {
        MySqlHelper mySqlHelper = new MySqlHelper(this);
        mySqlHelper.loadProducts( new IGetUserProductsCallback()
        {
            @Override
            public void onSuccess(List<Model_Product> userProducts)
            {
                allProducts = userProducts;
                displayProducts();
            }

            @Override
            public void onError(String strError)
            {
                Log.e(TAG, "onError: "+strError );
                gh.showAlerter("Ошибка","Не удалось загрузить список продуктов, повторите позже.",act_new_filter.this);
            }
        });
    }


    private void displayProducts()
    {
        gridProducts.removeAllViews();
        for(Model_Product product : allProducts)
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
    }
    //endregion

    //region Load and Display Interests
    private void loadInterests()
    {
        MySqlHelper mySqlHelper = new MySqlHelper(this);
        int id = (int)gh.getAdminInfo().get(GlobalHelper.SQL_COL_ID);
        mySqlHelper.loadInterests( new IGetUserInterestsCallback()
        {
            @Override
            public void onSuccess(List<Model_Interest> userInterests)
            {
                allInterests = userInterests;
                displayInterests();
            }

            @Override
            public void onError(String strError)
            {
                gh.showAlerter("Ошибка","Не удалось загрузить список интересов, повторите позже.",act_new_filter.this);
            }
        });
    }

    private void displayInterests()
    {
        gridInterests.removeAllViews();
        for(Model_Interest interest : allInterests)
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
    }
    //endregion

    private void setCategTextView()
    {
        tvCateg.setText(GlobalHelper.textOfCategs(cat_any,cat_new,cat_postoyan,cat_vip));
    }

    private void makeAdd()
    {
        String name = etName.getText().toString();
        if(name.isEmpty())
        {
            gh.showAlerter("Ошибка","Заполните обязательное поле Имя",this);
            return;
        }

        spotsDialog = gh.spotsDialogDialog(this);
        spotsDialog.show();

        if(cat_any == 0 && cat_new == 0 && cat_postoyan == 0 && cat_vip == 0)
        {
            cat_any = 1;
        }
        int gender = spinnerGender.getSelectedItemPosition();
        String city = etCity.getText().toString();
        String district = etDistrict.getText().toString();

        int age_ot = 9999;
        int age_do = 9999;
        int sale_ot = 9999;
        int sale_do = 9999;

        try
        {
            age_ot = Integer.parseInt(etAgeOt.getText().toString());
        }
        catch (Exception e){}
        try
        {
            age_do = Integer.parseInt(etAgeDo.getText().toString());
        }
        catch (Exception e) {}
        try
        {
            sale_ot = Integer.parseInt(etSaleOt.getText().toString());
        }
        catch (Exception e) {}
        try
        {
            sale_do = Integer.parseInt(etSaleDo.getText().toString());
        }
        catch (Exception e) {}

        int phoneInt = spinnerPhone.getSelectedItemPosition();
        int mailInt = spinnerEmail.getSelectedItemPosition();

        List<Integer> selectedProductIds = gh.getSelectedIds((List<Object>)(List<?>)allProducts);
        List<Integer> selectedInterestIds = gh.getSelectedIds((List<Object>)(List<?>)allInterests);


        Model_Filter filter = new Model_Filter();
        filter.setName(name);
        filter.setCat_any(cat_any);
        filter.setCat_new(cat_new);
        filter.setCat_postoyan(cat_postoyan);
        filter.setCat_vip(cat_vip);
        filter.setGender(gender);
        filter.setCity(city);
        filter.setDistrict(district);
        filter.setAge_ot(age_ot);
        filter.setAge_do(age_do);
        filter.setSale_ot(sale_ot);
        filter.setSale_do(sale_do);
        filter.setSearchPhone(phoneInt);
        filter.setSearchEmail(mailInt);
        filter.setListInterestIds(selectedInterestIds);
        filter.setListProductIds(selectedProductIds);

        Log.e(TAG, "makeAdd: Filter Created!" );

        MySqlHelper mySqlHelper = new MySqlHelper(act_new_filter.this);
        mySqlHelper.InsertNewFilter(filter, new ISimpleCallback()
        {
            @Override
            public void onSuccess(String response)
            {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK,intent);
                finish();
            }

            @Override
            public void onError(String strError)
            {
                gh.showAlerter("Ошибка","Не удалось отредактировать фильтр, повторите позже",act_new_filter.this);
                if(spotsDialog != null)
                {
                    spotsDialog.dismiss();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CATEG_CHOOSE_CODE && data!=null)
        {
            cat_any = data.getIntExtra("cat_any",1);
            cat_new = data.getIntExtra("cat_new",0);
            cat_postoyan = data.getIntExtra("cat_postoyan",0);
            cat_vip = data.getIntExtra("cat_vip",0);
            setCategTextView();
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED,returnIntent);
        finish();
    }
}
