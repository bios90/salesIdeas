package dimfcompany.com.salesideas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Map;

import dimfcompany.com.salesideas.Helpers.GlobalHelper;

public class act_first extends AppCompatActivity
{
    GlobalHelper gh;
    RelativeLayout laRegister,laLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_first);

        init();
        checkForLogin();
    }


    private void init()
    {
        gh=(GlobalHelper)getApplicationContext();
        laRegister = findViewById(R.id.laRegister);
        laLogin = findViewById(R.id.laEnter);


        laRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent  = new Intent(act_first.this,act_register.class);
                startActivity(intent);
            }
        });

        laLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(act_first.this,act_login.class);
                startActivity(intent);
            }
        });
    }


    private void checkForLogin()
    {
        Map<String,Object> userInfo = gh.getAdminInfo();
        int id = (int)userInfo.get(GlobalHelper.SQL_COL_ID);
        String name = (String) userInfo.get(GlobalHelper.SQL_COL_NAME);
        String email = (String) userInfo.get(GlobalHelper.SQL_COL_EMAIL);
        if(id != 99999999 && name != null && email != null)
        {
            Intent intent = new Intent(act_first.this,act_main.class);
            startActivity(intent);
        }
    }
}
