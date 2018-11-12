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
import dimfcompany.com.salesideas.Interfaces.IGetUserProductsCallback;
import dimfcompany.com.salesideas.Interfaces.ISimpleCallback;
import dimfcompany.com.salesideas.Models.Model_Product;
import dimfcompany.com.salesideas.R;
import dimfcompany.com.salesideas.act_edit_prod_dialog;
import dimfcompany.com.salesideas.act_main;

public class frag_product extends Fragment
{
    private static final String TAG = "frag_product";

    act_main parentActMain;
    GlobalHelper gh;
    View thisFragmentRootView;
    MySqlHelper mySqlHelper;
    frag_my_profile parentFrag;

    List<Model_Product> currentProducts = new ArrayList<>();



    EditText etAddProduct;
    Button btnAddProduct;
    LinearLayout laForProducts;
    ExpandableLayout expProducts;
    RelativeLayout laExpandProducts;

    private static int PRODUCT_EDIT_RESULT_CODE = 9000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_products,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        thisFragmentRootView = view;
        init();
        loadProducts();
    }

    private void init()
    {
        parentActMain = (act_main)getActivity();
        gh = (GlobalHelper)parentActMain.getApplicationContext();
        mySqlHelper = new MySqlHelper(parentActMain);
        parentFrag = (frag_my_profile)frag_product.this.getParentFragment();


        laForProducts = thisFragmentRootView.findViewById(R.id.laForProducts);
        btnAddProduct = thisFragmentRootView.findViewById(R.id.btnAddProduct);
        etAddProduct = thisFragmentRootView.findViewById(R.id.etAddProduct);
        laExpandProducts = thisFragmentRootView.findViewById(R.id.laExpandProducts);
        expProducts = thisFragmentRootView.findViewById(R.id.expProducts);

        btnAddProduct.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String newProduct = etAddProduct.getText().toString();
                if(newProduct == null || newProduct.isEmpty())
                {
                    gh.showAlerter("Ошибка","Введите название нового продукта",parentActMain);
                    return;
                }

                Map<String,Object> userInfo = gh.getAdminInfo();
                int id = (int)userInfo.get(GlobalHelper.SQL_COL_ID);
                MySqlHelper mySqlHelper = new MySqlHelper(parentActMain);
                mySqlHelper.addProduct(newProduct, id, new ISimpleCallback()
                {
                    @Override
                    public void onSuccess(String response)
                    {
                        if(response.equals("error"))
                        {
                            gh.showAlerter("Ошибка","Не удалось добавить продукт, повторите позже",parentActMain);
                            return;
                        }

                        if(response.equals("true"))
                        {
                            etAddProduct.setText("");
                            gh.showAlerter("Ура!", "Новый продукт успешно добавлен", parentActMain);
                            loadProducts();
                            return;
                        }
                    }

                    @Override
                    public void onError(String strError)
                    {
                        gh.showAlerter("Ошибка","Не удалось добавить подукт, повторите позже",parentActMain);
                        Log.e(TAG, "onError: "+ strError);
                    }
                });
            }
        });

        laExpandProducts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                expProducts.toggle();
            }
        });
    }

    private void loadProducts()
    {
        MySqlHelper mySqlHelper = new MySqlHelper(parentActMain);
        int id = (int)gh.getAdminInfo().get(GlobalHelper.SQL_COL_ID);
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
                gh.showAlerter("Ошибка","Не удалось загрузить список продуктов, повторите позже.",parentActMain);
            }
        });
    }

    private void displayProducts()
    {
        laForProducts.removeAllViews();
        for(final Model_Product product : currentProducts)
        {
            final View productView = parentActMain.getLayoutInflater().inflate(R.layout.item_oneline,laForProducts,false);
            TextView tvText = productView.findViewById(R.id.tvText);
            tvText.setText(product.getText());

            TextView tvEdit = productView.findViewById(R.id.tvEdit);
            TextView tvDelete = productView.findViewById(R.id.tvDelete);

            tvEdit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    gh.setCurrentProductToEdit(product);
                    Intent intent = new Intent(parentActMain,act_edit_prod_dialog.class);
                    startActivityForResult(intent,PRODUCT_EDIT_RESULT_CODE);
                }
            });

            tvDelete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    mySqlHelper.deleteProduct(product.getId(), new ISimpleCallback()
                    {
                        @Override
                        public void onSuccess(String response)
                        {
                            if(response.equals("true"))
                            {
                                gh.showAlerter("Успешно","Продукт удален",parentActMain);
                                parentFrag.resetProductsFrag();
                            }
                        }

                        @Override
                        public void onError(String strError)
                        {

                        }
                    });
                }
            });


            laForProducts.addView(productView);
            Log.e(TAG, "displayProducts: added view" );
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.e(TAG, "onActivityResult: result called");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PRODUCT_EDIT_RESULT_CODE && resultCode == Activity.RESULT_OK)
        {
            gh.showAlerter("Успешно","Продукт успешно изменен",parentActMain);
            parentFrag.resetProductsFrag();
        }
    }
}
