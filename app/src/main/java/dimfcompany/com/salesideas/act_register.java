package dimfcompany.com.salesideas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Helpers.MySqlHelper;
import dimfcompany.com.salesideas.Interfaces.ISimpleCallback;

public class act_register extends AppCompatActivity
{
    private static final String TAG = "act_register";
    GlobalHelper gh;
    EditText etName,etEmail,etPass1,etPass2;
    Button btnOk,btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_register);

        init();
    }

    private void init()
    {
        gh = (GlobalHelper)getApplicationContext();
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPass1 = findViewById(R.id.etPass1);
        etPass2 = findViewById(R.id.etPass2);

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
                makeRegister();
            }
        });
    }

    private void makeRegister()
    {
        if(!gh.isNetworkAvailable(act_register.this))
        {
            Toast.makeText(gh, "Для регистрации необходимо соединение с сетью", Toast.LENGTH_SHORT).show();
            return;
        }

        final String name = etName.getText().toString();
        final String email = etEmail.getText().toString();
        final String pass1 = etPass1.getText().toString();
        String pass2 = etPass2.getText().toString();

        if(name == null || name.isEmpty())
        {
            Toast.makeText(gh, "Заполните поле Имя", Toast.LENGTH_SHORT).show();
            return;
        }

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

        if(pass1 == null || pass1.isEmpty())
        {
            Toast.makeText(gh, "Заполните поле Пароль", Toast.LENGTH_SHORT).show();
            return;
        }

        if(pass1.length() < 8)
        {
            Toast.makeText(gh, "Пароль должен сожержать минимум 8 символов", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!pass1.equals(pass2))
        {
            Toast.makeText(gh, "Пароли не совпадают.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.e(TAG, "makeRegister: password is "+pass1 );
        MySqlHelper mySqlHelper = new MySqlHelper(act_register.this);
        mySqlHelper.checkEmailIfInUse(email, new ISimpleCallback()
        {
            @Override
            public void onSuccess(String response)
            {
                Log.e(TAG, "onSuccess: Response is "+response);
                if(response.equals("error"))
                {
                    Toast.makeText(gh, "Возникла ошибка при регистрации, повторите позже.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(response.equals("true"))
                {
                    Toast.makeText(gh, "Данный email уже занят.", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(response.equals("false"))
                {
                    makeRegister2(email,pass1,name);
                }
            }

            @Override
            public void onError(String strError)
            {
                Log.e(TAG, "onError: "+strError );
                Toast.makeText(gh, "Возникла ошибка при регистрации, повторите позже.", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    private void makeRegister2(final String email, String pass1, String name)
    {
        MySqlHelper mySqlHelper = new MySqlHelper(act_register.this);
        mySqlHelper.registerNewAdmin(email, pass1, name, new ISimpleCallback()
        {
            @Override
            public void onSuccess(String response)
            {
                Log.e(TAG, "onSuccess: "+response );
                if(response.equals("true"))
                {
                    Toast.makeText(gh, "На почту "+email+" отправлено пиьсмо для активации аккаунта. Пройдите по ссылке в письме для завершения активации.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(act_register.this,act_login.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onError(String strError)
            {
                Log.e(TAG, "onError: "+strError );
            }
        });
    }


}
