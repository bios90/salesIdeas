package dimfcompany.com.salesideas.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKServiceActivity;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Helpers.MySqlHelper;
import dimfcompany.com.salesideas.Models.Model_Interest;
import dimfcompany.com.salesideas.R;
import dimfcompany.com.salesideas.act_main;

import static android.content.Context.MODE_PRIVATE;

public class frag_my_accaunt extends Fragment
{
    private static final String TAG = "frag_my_accaunt";

    act_main parentActMain;
    GlobalHelper gh;
    View thisFragmentRootView;
    MySqlHelper mySqlHelper;
    frag_my_profile parentFrag;

    Button btnAddVK;
    TextView tvAdminName,tvAdminEmail,tvVkName;
    Map<String,Object> adminInfo;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_my_accaunt,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        thisFragmentRootView = view;
        init();
    }

    private void init()
    {
        parentActMain = (act_main)getActivity();
        gh = (GlobalHelper)parentActMain.getApplicationContext();
        parentFrag = (frag_my_profile)frag_my_accaunt.this.getParentFragment();

        tvAdminName = thisFragmentRootView.findViewById(R.id.tvAdminName);
        tvAdminEmail = thisFragmentRootView.findViewById(R.id.tvAdminEmail);
        tvVkName = thisFragmentRootView.findViewById(R.id.tvVkName);

        btnAddVK = thisFragmentRootView.findViewById(R.id.btnVkAdd);

        adminInfo = gh.getAdminInfo();

        tvAdminName.setText((String)adminInfo.get(GlobalHelper.SQL_COL_NAME));
        tvAdminEmail.setText((String)adminInfo.get(GlobalHelper.SQL_COL_EMAIL));

        String vkName = (String)adminInfo.get(GlobalHelper.SQL_COL_VK_NAME);
        String vkId = (String)adminInfo.get(GlobalHelper.SQL_COL_VK_ID);

        if(vkName == null || vkId == null)
        {
            btnAddVK.setVisibility(View.VISIBLE);
            tvVkName.setVisibility(View.GONE);
        }
        else
            {
                tvVkName.setVisibility(View.VISIBLE);
                tvVkName.setText(vkName);
                btnAddVK.setVisibility(View.GONE);
            }


        btnAddVK.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addVkProfile();
            }
        });

    }

    private void addVkProfile()
    {
        Intent intent = new Intent(getActivity(), VKServiceActivity.class);
        intent.putExtra("arg1", "Authorization");
        ArrayList scopes = new ArrayList<>();
        scopes.add(VKScope.OFFLINE);
        scopes.add(VKScope.FRIENDS);
        scopes.add(VKScope.PHOTOS);
        intent.putStringArrayListExtra("arg2", scopes);
        intent.putExtra("arg4", VKSdk.isCustomInitialize());
        startActivityForResult(intent, VKServiceActivity.VKServiceType.Authorization.getOuterCode());
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>()
        {
            @Override
            public void onResult(VKAccessToken res)
            {
                GlobalHelper.setVkApiToken(res.accessToken);
                SharedPreferences.Editor editor = parentActMain.getSharedPreferences(GlobalHelper.ADMIN_INFO_PREFS, MODE_PRIVATE).edit();
                editor.putString(GlobalHelper.SQL_COL_VK_API_TOKEN, res.accessToken);
                editor.apply();
                editor.commit();
                makeAdminInfoUpdate();
            }
            @Override
            public void onError(VKError error)
            {

            }
        }))
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void makeAdminInfoUpdate()
    {
        String token = GlobalHelper.getVkApiToken();
        VKParameters parameters = VKParameters.from(VKApiConst.ACCESS_TOKEN, token);

        VKRequest request = new VKRequest("users.get", parameters);


        request.executeWithListener(new VKRequest.VKRequestListener()
        {
            @Override
            public void onComplete(VKResponse response)
            {
                super.onComplete(response);

                Log.e(TAG, "VK RESPONSE "+response.responseString );
                try
                {
                    JSONArray jsonArray = response.json.getJSONArray("response");
                    JSONObject userObject = jsonArray.getJSONObject(0);

                    String first_name = userObject.getString("first_name");
                    String last_name = userObject.getString("last_name");
                    String id = String.valueOf(userObject.getInt("id"));
                    SharedPreferences.Editor editor = parentActMain.getSharedPreferences(GlobalHelper.ADMIN_INFO_PREFS, MODE_PRIVATE).edit();
                    editor.putString(GlobalHelper.SQL_COL_VK_NAME, first_name+" "+last_name);
                    editor.putString(GlobalHelper.SQL_COL_VK_ID, id);
                    editor.apply();
                    editor.commit();

                    parentFrag.resetMyAccaunt();

                } catch (JSONException e)
                {
                    Log.e(TAG, "onComplete: "+e.getMessage() );;
                }
            }

            @Override
            public void onError(VKError error)
            {
                super.onError(error);
                Log.e(TAG, "onError: "+error.toString() );
            }
        });
//        request.executeWithListener(new VKRequestListener(MainActivity.this)
//        {
//            @Override
//            public void onComplete(VKResponse response) {
//                super.onComplete(response);
//
//                String status = "";
//
//                try {
//
//                    JSONObject jsonObject = response.json.getJSONObject("response");
//
//                    String first_name = jsonObject.getString("first_name");
//                    String last_name = jsonObject.getString("last_name");
//                    String screen_name = jsonObject.getString("screen_name");
//                    status = jsonObject.getString("status");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
    }
}
