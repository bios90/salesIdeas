package dimfcompany.com.salesideas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Helpers.MySqlHelper;
import dimfcompany.com.salesideas.Interfaces.ILoginCallback;
import dimfcompany.com.salesideas.Models.Model_Admin;

public class act_login extends AppCompatActivity
{
    private static final String TAG = "act_login";
    GlobalHelper gh;
    EditText etEmail,etPass;
    Button btnOk,btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

        init();
    }

    private void init()
    {
        gh = (GlobalHelper)getApplicationContext();
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass1);

        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                makeLogin();
            }
        });
    }

    private void makeLogin()
    {
        if(!gh.isNetworkAvailable(act_login.this))
        {
            Toast.makeText(gh, "Для входа необходимо соединение с сетью", Toast.LENGTH_SHORT).show();
            return;
        }

        final String email = etEmail.getText().toString();
        final String pass = etPass.getText().toString();


        if(email == null || email.isEmpty())
        {
            Toast.makeText(gh, "Заполните поле Email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!GlobalHelper.isEmailValid(email))
        {
            Toast.makeText(gh, "Введите корректный Email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(pass == null || pass.isEmpty())
        {
            Toast.makeText(gh, "Заполните поле Пароль", Toast.LENGTH_SHORT).show();
            return;
        }


        MySqlHelper mySqlHelper = new MySqlHelper(act_login.this);
        mySqlHelper.makeLogin(email, pass, new ILoginCallback()
        {
            @Override
            public void onSuccess(Model_Admin admin)
            {
                SharedPreferences.Editor editor = getSharedPreferences(GlobalHelper.ADMIN_INFO_PREFS, MODE_PRIVATE).edit();
                editor.putString(GlobalHelper.SQL_COL_NAME, admin.getName());
                editor.putString(GlobalHelper.SQL_COL_EMAIL, admin.getEmail());
                editor.putInt(GlobalHelper.SQL_COL_ID, admin.getId());
                editor.apply();
                editor.commit();

                Intent intent = new Intent(act_login.this,act_main.class);
                startActivity(intent);
            }

            @Override
            public void onError(String strError)
            {

                if(strError.equals("baseError"))
                {
                    Toast.makeText(gh, "Ошибка базы, повторите вход позже.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(strError.equals("postError"))
                {
                    Toast.makeText(gh, "Ошибка входа, проверьте введенные данные", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(strError.equals("passError"))
                {
                    Toast.makeText(gh, "Ошибка входа, проверьте введенные данные", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(strError.equals("notActivatedError"))
                {
                    Toast.makeText(gh, "Необходимо завершить регистрацию, открыв ссылку в письме.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.e(TAG, "onError: "+strError );
                Toast.makeText(gh, "Ошибка входа.", Toast.LENGTH_SHORT).show();
                return;
            }


        });
    }
}
