package dimfcompany.com.salesideas;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

public class act_categ_type extends AppCompatActivity
{
    RelativeLayout laAny,laNew,laPostoyan,laVip;
    ToggleButton toggAny,toggNew,toggPostoyan,toggVip;

    RelativeLayout laRoot;

    int cat_any = 0;
    int cat_new = 0;
    int cat_postoyan = 0;
    int cat_vip = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_categ_type);
        init();
    }

    private void init()
    {
        laAny = findViewById(R.id.laAny);
        laNew = findViewById(R.id.laNew);
        laPostoyan = findViewById(R.id.laPostoyan);
        laVip = findViewById(R.id.laVip);

        toggAny = findViewById(R.id.toggAny);
        toggNew = findViewById(R.id.toggNew);
        toggPostoyan = findViewById(R.id.toggPostoyan);
        toggVip = findViewById(R.id.toggVip);

        laRoot = findViewById(R.id.laRoot);
        laRoot.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        laAny.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                toggAny.toggle();
                if(toggAny.isChecked())
                {
                    disableCategs();
                }
            }
        });

        laNew.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                toggNew.toggle();
                if(toggNew.isChecked())
                {
                    toggAny.setChecked(false);
                }
            }
        });

        laPostoyan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                toggPostoyan.toggle();
                if(toggPostoyan.isChecked())
                {
                    toggAny.setChecked(false);
                }
            }
        });

        laVip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                toggVip.toggle();
                if(toggVip.isChecked())
                {
                    toggAny.setChecked(false);
                }
            }
        });
    }

    private void disableCategs()
    {
        toggNew.setChecked(false);
        toggPostoyan.setChecked(false);
        toggVip.setChecked(false);
    }

    @Override
    public void onBackPressed()
    {
        if(toggAny.isChecked())
        {
            cat_any = 1;
        }
        else
            {
                if(toggNew.isChecked())
                {
                    cat_new = 1;
                }
                if(toggPostoyan.isChecked())
                {
                    cat_postoyan = 1;
                }
                if(toggVip.isChecked())
                {
                    cat_vip = 1;
                }
            }


        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        returnIntent.putExtra("cat_any", cat_any);
        returnIntent.putExtra("cat_new", cat_new);
        returnIntent.putExtra("cat_postoyan", cat_postoyan);
        returnIntent.putExtra("cat_vip", cat_vip);
        finish();
    }
}
