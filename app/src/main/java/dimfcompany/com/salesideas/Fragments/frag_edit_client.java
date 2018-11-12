package dimfcompany.com.salesideas.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ramotion.fluidslider.FluidSlider;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Helpers.MySqlHelper;
import dimfcompany.com.salesideas.Interfaces.IGetClientByID;
import dimfcompany.com.salesideas.Interfaces.IGetUserInterestsCallback;
import dimfcompany.com.salesideas.Interfaces.IGetUserProductsCallback;
import dimfcompany.com.salesideas.Interfaces.ISimpleCallback;
import dimfcompany.com.salesideas.Models.Model_Client;
import dimfcompany.com.salesideas.Models.Model_Interest;
import dimfcompany.com.salesideas.Models.Model_Product;
import dimfcompany.com.salesideas.R;
import dimfcompany.com.salesideas.act_full_client_info;
import dimfcompany.com.salesideas.act_main;
import kotlin.Unit;

public class frag_edit_client extends Fragment
{
    private static final String TAG = "frag_edit_client";

    act_full_client_info actParent;
    GlobalHelper gh;
    View thisFragmentRootView;
    ArrayAdapter<String> adapterGender;
    ArrayAdapter<String> adapterClientType;


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

    FluidSlider saleSlider;

    EditText etName,etNick,etPhone,etComment,etEmail,etVkID,etCity,etDistrict;
    RelativeLayout laAddClient;

    GridLayout gridInterests;
    ExpandableLayout expInterests;
    RelativeLayout laOpenInterests;

    GridLayout gridProducts;
    ExpandableLayout expProducts;
    RelativeLayout laOpenProducts;


    Model_Client currentClient;
    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
    LinearLayout laOkCancel;
    RelativeLayout laBtnAdd;
    Button btnOk,btnCancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_add_client,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        thisFragmentRootView = view;
        init();
        loadProducts();
        loadInterests();
        loadCurrentInfo();
    }




    private void init()
    {
        actParent = (act_full_client_info) getActivity();
        gh = (GlobalHelper) actParent.getApplicationContext();
        currentClient = gh.getCurrentClientToDisplay();

        spinnerGender = thisFragmentRootView.findViewById(R.id.spinnerGender);
        spinnerClientType = thisFragmentRootView.findViewById(R.id.spinnerClientType);
        tvDate = thisFragmentRootView.findViewById(R.id.tvDate);
        tvAge = thisFragmentRootView.findViewById(R.id.tvAge);

        gridProducts = thisFragmentRootView.findViewById(R.id.gridProducts);
        laOpenProducts = thisFragmentRootView.findViewById(R.id.laOpenProducts);
        expProducts = thisFragmentRootView.findViewById(R.id.expProducts);

        gridInterests = thisFragmentRootView.findViewById(R.id.gridInterests);
        laOpenInterests = thisFragmentRootView.findViewById(R.id.laOpenInterests);
        expInterests = thisFragmentRootView.findViewById(R.id.expInterests);

        tvFirstVisit = thisFragmentRootView.findViewById(R.id.tvFirstVisit);
        tvLastVisit = thisFragmentRootView.findViewById(R.id.tvLastVisit);

        etName = thisFragmentRootView.findViewById(R.id.etName);
        etNick = thisFragmentRootView.findViewById(R.id.etNick);
        etPhone = thisFragmentRootView.findViewById(R.id.etPhone);
        etComment = thisFragmentRootView.findViewById(R.id.etComment);
        etEmail = thisFragmentRootView.findViewById(R.id.etEmail);

        etVkID = thisFragmentRootView.findViewById(R.id.etVkId);
        etCity = thisFragmentRootView.findViewById(R.id.etCity);
        etDistrict = thisFragmentRootView.findViewById(R.id.etDistrict);

        saleSlider = thisFragmentRootView.findViewById(R.id.sale_Slider);
        laAddClient = thisFragmentRootView.findViewById(R.id.laBtnAddClient);

        listGender = Arrays.asList(getResources().getStringArray(R.array.Gender));
        listClientType = Arrays.asList(getResources().getStringArray(R.array.ClientCategory));

        adapterGender = new ArrayAdapter<String>(actParent,R.layout.my_spinner,listGender);
        spinnerGender.setAdapter(adapterGender);

        adapterClientType = new ArrayAdapter<String>(actParent,R.layout.my_spinner,listClientType);
        spinnerClientType.setAdapter(adapterClientType);

        laOkCancel = thisFragmentRootView.findViewById(R.id.laOkCancel);
        laOkCancel.setVisibility(View.VISIBLE);

        btnOk = thisFragmentRootView.findViewById(R.id.btnOk);
        btnCancel = thisFragmentRootView.findViewById(R.id.btnCancel);


//        laAddClient.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                Toast.makeText(actParent, saleSlider.getPosition()+"", Toast.LENGTH_SHORT).show();
//            }
//        });

        laBtnAdd = thisFragmentRootView.findViewById(R.id.laBtnAdd);
        laBtnAdd.setVisibility(View.GONE);

        //region Dates and Calendars
        currentBirthDay = Calendar.getInstance();
        currentFirstVisit = Calendar.getInstance();
        currentLastVisit = Calendar.getInstance();

        Date dateToday = Calendar.getInstance().getTime();
        long secconds = gh.dateToLong(dateToday);
        tvFirstVisit.setText(gh.dateToStr(secconds));
        tvLastVisit.setText(gh.dateToStr(secconds));

        tvFirstVisit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View viewTV)
            {
                Calendar calendar = currentFirstVisit;
                int yearToday = calendar.get(Calendar.YEAR);
                final int monthToday = calendar.get(Calendar.MONTH);
                final int dayToday = calendar.get(Calendar.DAY_OF_MONTH);

                new SpinnerDatePickerDialogBuilder()
                        .context(actParent)
                        .callback(new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                            {
                                Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                                currentFirstVisit = calendar;
                                Date date = calendar.getTime();
                                long secconds = gh.dateToLong(date);
                                ((TextView)viewTV).setText(gh.dateToStr(secconds));
                            }
                        })
                        .spinnerTheme(R.style.DatePickerSpinner)
                        .defaultDate(yearToday, monthToday, dayToday)
                        .build()
                        .show();
            }
        });

        tvLastVisit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View viewTV)
            {
                Calendar calendar = currentLastVisit;
                int yearToday = calendar.get(Calendar.YEAR);
                final int monthToday = calendar.get(Calendar.MONTH);
                final int dayToday = calendar.get(Calendar.DAY_OF_MONTH);

                new SpinnerDatePickerDialogBuilder()
                        .context(actParent)
                        .callback(new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                            {
                                Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                                currentLastVisit = calendar;
                                Date date = calendar.getTime();
                                long secconds = gh.dateToLong(date);
                                ((TextView)viewTV).setText(gh.dateToStr(secconds));

                            }
                        })
                        .spinnerTheme(R.style.DatePickerSpinner)
                        .defaultDate(yearToday, monthToday, dayToday)
                        .build()
                        .show();
            }
        });


        tvDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar calendar = currentBirthDay;
                int yearToday = calendar.get(Calendar.YEAR);
                final int monthToday = calendar.get(Calendar.MONTH);
                final int dayToday = calendar.get(Calendar.DAY_OF_MONTH);

                new SpinnerDatePickerDialogBuilder()
                        .context(actParent)
                        .callback(new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                            {
                                Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                                currentBirthDay = calendar;
                                Date date = calendar.getTime();
                                long secconds = gh.dateToLong(date);
                                tvDate.setText(gh.dateToStr(secconds));
                                String ageStr = gh.getAge(year,monthOfYear,dayOfMonth);
                                tvAge.setText(ageStr);

                                birthDaySetted = true;
                            }
                        })
                        .spinnerTheme(R.style.DatePickerSpinner)
                        .defaultDate(yearToday, monthToday, dayToday)
                        .build()
                        .show();
            }
        });

        //endregion

        btnOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                makeEdit();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                actParent.restartActivity();
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

    private void loadProducts()
    {
        MySqlHelper mySqlHelper = new MySqlHelper(actParent);
        int id = (int)gh.getAdminInfo().get(GlobalHelper.SQL_COL_ID);
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
                gh.showAlerter("Ошибка","Не удалось загрузить список продуктов, повторите позже.", actParent);
            }
        });
    }


    private void loadInterests()
    {
        MySqlHelper mySqlHelper = new MySqlHelper(actParent);
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
                gh.showAlerter("Ошибка","Не удалось загрузить список интересов, повторите позже.", actParent);
            }
        });
    }

    private void displayInterests()
    {
        gridInterests.removeAllViews();
        for(Model_Interest interest : currentInterests)
        {
            final View productView = actParent.getLayoutInflater().inflate(R.layout.item_with_checkbox,gridInterests,false);
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


        for(Model_Interest interest : currentInterests)
        {
            int id = interest.getId();
            if(currentClient.getInterestIds().contains(id))
            {
                interest.getConnectedTogg().setChecked(true);
            }
        }
    }

    private void displayProducts()
    {
        gridProducts.removeAllViews();
        for(Model_Product product : currentProducts)
        {
            final View productView = actParent.getLayoutInflater().inflate(R.layout.item_with_checkbox,gridProducts,false);
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

        for(Model_Product prod : currentProducts)
        {
            int id = prod.getId();
            if(currentClient.getProductIds().contains(id))
            {
                prod.getConnectedTogg().setChecked(true);
            }
        }
    }



    void makeEdit()
    {
        int id = currentClient.getId();
        String name = etName.getText().toString();
        String nick = etNick.getText().toString();
        String vkId = etVkID.getText().toString();
        String city = etCity.getText().toString();
        String district = etDistrict.getText().toString();


        int gender = spinnerGender.getSelectedItemPosition();
        Date birthday = new Date(0);
        if(birthDaySetted)
        {
            birthday = currentBirthDay.getTime();
        }
        int category = spinnerClientType.getSelectedItemPosition();
        String phone = etPhone.getText().toString();
        String email = etEmail.getText().toString();
        Date firstContact = currentFirstVisit.getTime();
        Date lastContact = currentLastVisit.getTime();
        int sale = (int)(saleSlider.getPosition()*100);
        String comment = etComment.getText().toString();

        List<Integer> selectedProductIds = gh.getSelectedIds((List<Object>)(List<?>)currentProducts);
        List<Integer> selectedInterestIds = gh.getSelectedIds((List<Object>)(List<?>)currentInterests);

        if(name.isEmpty())
        {
            gh.showAlerter("Ошибка","Заполните обязательное поле Имя.", actParent);
            return;
        }

        Model_Client client = new Model_Client();

        int adminId = (int)gh.getAdminInfo().get(GlobalHelper.SQL_COL_ID);
        client.setAdmin_id(adminId);
        client.setId(id);


        client.setVkId(vkId);
        client.setCity(city);
        client.setDistrict(district);



        client.setName(name);
        client.setNick(nick);
        client.setGender(gender);
        client.setBirthday(birthday);
        client.setCategory(category);
        client.setPhone(phone);
        client.setEmail(email);
        client.setFirstVisit(firstContact);
        client.setLastVisit(lastContact);
        client.setSale(sale);
        client.setComment(comment);
        client.setInterestIds(selectedInterestIds);
        client.setProductIds(selectedProductIds);

        MySqlHelper mySqlHelper = new MySqlHelper(actParent);
        mySqlHelper.editClient(client, new ISimpleCallback()
        {
            @Override
            public void onSuccess(String response)
            {
                if(response.equals("success"))
                {
                    gh.showAlerter("Успешно","Данные клиента изменены.", actParent);
                    actParent.setChangesMade(true);
                    reloadUser();
                    return;
                }
                else
                {
                    gh.showAlerter("Ошибка","Не удалось изменить данные, повторите позже.", actParent);
                }
            }

            @Override
            public void onError(String strError)
            {
                gh.showAlerter("Ошибка","Не удалось изменить данные, повторите позже.", actParent);
            }
        });
    }

    private void reloadUser()
    {
        MySqlHelper mySqlHelper = new MySqlHelper(actParent);
        mySqlHelper.getClientByID(currentClient.getId(), new IGetClientByID()
        {
            @Override
            public void onSuccess(Model_Client client)
            {
                gh.setCurrentClientToDisplay(client);
                actParent.restartActivity();
            }

            @Override
            public void onError(String strError)
            {

            }
        });
    }


    private void loadCurrentInfo()
    {
        etName.setText(currentClient.getName());
        etNick.setText(currentClient.getNick());
        spinnerGender.setSelection(currentClient.getGender());
        if(!fmt.format(currentClient.getBirthday()).equals(fmt.format(new Date(0))))
        {
            String birthAge = GlobalHelper.dateFormatForDisplay.format(currentClient.getBirthday());
            tvDate.setText(birthAge);

            String ageStr = gh.getAge(currentClient.getBirthday());
            tvAge.setText(ageStr);

            currentBirthDay.setTime(currentClient.getBirthday());
            birthDaySetted=true;
        }
        spinnerClientType.setSelection(currentClient.getCategory());
        etPhone.setText(currentClient.getPhone());
        etEmail.setText(currentClient.getEmail());

        if(!fmt.format(currentClient.getFirstVisit()).equals(fmt.format(new Date(0))))
        {
            String birthAge = GlobalHelper.dateFormatForDisplay.format(currentClient.getFirstVisit());
            tvFirstVisit.setText(birthAge);

            currentFirstVisit.setTime(currentClient.getFirstVisit());
        }

        if(!fmt.format(currentClient.getLastVisit()).equals(fmt.format(new Date(0))))
        {
            String birthAge = GlobalHelper.dateFormatForDisplay.format(currentClient.getFirstVisit());
            tvLastVisit.setText(birthAge);

            currentLastVisit.setTime(currentClient.getLastVisit());
        }

        int saleSize = currentClient.getSale();
        float rightSale = (float) saleSize/100;
        saleSlider.setPosition(rightSale);
        etComment.setText(currentClient.getComment());


        etVkID.setText(currentClient.getVkId());
        etCity.setText(currentClient.getCity());
        etDistrict.setText(currentClient.getDistrict());

    }


}
