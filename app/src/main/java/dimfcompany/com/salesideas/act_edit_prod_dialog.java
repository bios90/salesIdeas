package dimfcompany.com.salesideas;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Helpers.MySqlHelper;
import dimfcompany.com.salesideas.Interfaces.ISimpleCallback;
import dimfcompany.com.salesideas.Models.Model_Product;

public class act_edit_prod_dialog extends AppCompatActivity
{
    GlobalHelper gh;
    TextView tvTitle;
    EditText etText;
    Button btnOk,btnCancel;
    Model_Product productToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit_prod_interest_dialog);
        init();
    }

    private void init()
    {
        gh = (GlobalHelper)getApplicationContext();
        tvTitle = findViewById(R.id.tvTitle);
        etText = findViewById(R.id.etText);
        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);
        productToEdit = gh.getCurrentProductToEdit();

        tvTitle.setText("Изменить продукт");
        etText.setText(productToEdit.getText());

        btnOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String newText = etText.getText().toString();
                if(newText.isEmpty() || newText.equals(productToEdit.getText()))
                {
                    gh.showAlerter("Ошибка","Введите новое название продукта",act_edit_prod_dialog.this);
                    return;
                }

                MySqlHelper mySqlHelper = new MySqlHelper(act_edit_prod_dialog.this);
                mySqlHelper.editProduct(newText, productToEdit.getId(), new ISimpleCallback()
                {
                    @Override
                    public void onSuccess(String response)
                    {
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }

                    @Override
                    public void onError(String strError)
                    {

                    }
                });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }
}
