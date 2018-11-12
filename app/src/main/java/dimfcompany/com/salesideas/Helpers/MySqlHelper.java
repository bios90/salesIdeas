package dimfcompany.com.salesideas.Helpers;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL;

import dimfcompany.com.salesideas.Interfaces.IGetAllExcelsCallback;
import dimfcompany.com.salesideas.Interfaces.IGetAllFiltersCallback;
import dimfcompany.com.salesideas.Interfaces.IGetAllMyClients;
import dimfcompany.com.salesideas.Interfaces.IGetClientByID;
import dimfcompany.com.salesideas.Interfaces.IGetUserInterestsCallback;
import dimfcompany.com.salesideas.Interfaces.IGetUserProductsCallback;
import dimfcompany.com.salesideas.Interfaces.ILoginCallback;
import dimfcompany.com.salesideas.Interfaces.ISimpleCallback;
import dimfcompany.com.salesideas.Models.Model_Admin;
import dimfcompany.com.salesideas.Models.Model_Client;
import dimfcompany.com.salesideas.Models.Model_Excel_Item;
import dimfcompany.com.salesideas.Models.Model_Filter;
import dimfcompany.com.salesideas.Models.Model_Interest;
import dimfcompany.com.salesideas.Models.Model_Product;

public class MySqlHelper
{
    private static final String TAG = "MySqlHelper";
    Context ctx;
    GlobalHelper gh;
    ISimpleCallback currentAddClientCallback;
    ISimpleCallback currentEditClientCallBack;
    ISimpleCallback currentAddFilterCallBack;
    ISimpleCallback currentEditFilterCallBack;
    Model_Client currentClientToInsert;
    Model_Client currentClientToEdit;
    Model_Filter currentFilterToInsert;
    Model_Filter currentFilterToEdit;
    int resSend;
    int resGet;


    public MySqlHelper(Context ctx)
    {
        this.ctx = ctx;
        gh = (GlobalHelper) ctx.getApplicationContext();
    }

    //region Check if Mail registered
    public void checkEmailIfInUse(final String email, final ISimpleCallback callback)
    {
        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                callback.onSuccess(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError(error.getMessage());
            }
        };


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalHelper.CHECK_IF_EMAIL_IN_USE_URL, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();
                myParams.put(GlobalHelper.SQL_COL_EMAIL, email);
                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion


    //region register New Admin
    public void registerNewAdmin(final String email, final String pass, final String name, final ISimpleCallback callback)
    {
        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                callback.onSuccess(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError(error.getMessage());
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalHelper.REGISTER_URL, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();
                myParams.put(GlobalHelper.SQL_COL_EMAIL, email);
                myParams.put(GlobalHelper.SQL_COL_PASSWORD, pass);
                myParams.put(GlobalHelper.SQL_COL_NAME, name);
                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion


    //region add Product
    public void addProduct(final String text, final int userId, final ISimpleCallback callback)
    {
        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                callback.onSuccess(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError(error.getMessage());
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalHelper.ADD_PRODUCT_URL, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();
                myParams.put(GlobalHelper.SQL_COL_USER_ID, String.valueOf(userId));
                myParams.put(GlobalHelper.SQL_COL_TEXT, text);
                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion


    //region Add Interest
    public void addInterest(final String text, final int userId, final ISimpleCallback callback)
    {
        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                callback.onSuccess(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError(error.getMessage());
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalHelper.ADD_INTEREST_URL, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();
                myParams.put(GlobalHelper.SQL_COL_USER_ID, String.valueOf(userId));
                myParams.put(GlobalHelper.SQL_COL_TEXT, text);
                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion


    //region make Login
    public void makeLogin(final String email, final String password, final ILoginCallback callback)
    {
        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.e(TAG, "onResponse: "+response );
                try
                {
                    JSONObject object = new JSONObject(response);
                    Model_Admin admin = new Model_Admin();
                    admin.setId(object.getInt(GlobalHelper.SQL_COL_ID));
                    admin.setName(object.getString(GlobalHelper.SQL_COL_NAME));
                    admin.setEmail(object.getString(GlobalHelper.SQL_COL_EMAIL));
                    callback.onSuccess(admin);
                }
                catch (Exception e)
                {
                    Log.e(TAG, "onCatch: "+e.getMessage() );
                    callback.onError(response);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "onErrorResponse: "+error.getMessage() );
                callback.onError(error.getMessage());
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalHelper.LOGIN_URL, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();
                myParams.put(GlobalHelper.SQL_COL_EMAIL, email);
                myParams.put(GlobalHelper.SQL_COL_PASSWORD, password);
                return myParams;
            }
        };


        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion


    //region loadProducts
    public void loadProducts(final IGetUserProductsCallback callback)
    {
        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONArray responseArray = new JSONArray(response);
                    List<Model_Product> userProducts = new ArrayList<>();

                    for(int a=0;a<responseArray.length();a++)
                    {
                        JSONObject productObject = responseArray.getJSONObject(a);
                        int id = productObject.getInt(GlobalHelper.SQL_COL_ID);
                        String text = productObject.getString(GlobalHelper.SQL_COL_TEXT);

                        Model_Product product = new Model_Product();
                        product.setText(text);
                        product.setId(id);

                        userProducts.add(product);
                    }

                    callback.onSuccess(userProducts);
                }
                catch (Exception e)
                {
                    Log.e(TAG, "onResponse: "+e.getMessage() );
                    if(response.equals("error"))
                    {
                        callback.onError("error");
                        return;
                    }
                    else if(response.equals("empty"))
                    {
                        callback.onError("empty");
                    }
                    else
                        {
                            callback.onError(e.getMessage());
                        }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "onErrorResponse: "+ error );
            }
        };


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalHelper.GET_USER_PRODUCTS_URL, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();
                int id = (int)gh.getAdminInfo().get(GlobalHelper.SQL_COL_ID);
                myParams.put(GlobalHelper.SQL_COL_USER_ID, String.valueOf(id));
                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion


    //region load Interests
    public void loadInterests(final IGetUserInterestsCallback callback)
    {
        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.e(TAG, "onResponse: "+response );
                try
                {
                    JSONArray responseArray = new JSONArray(response);
                    List<Model_Interest> userInterests = new ArrayList<>();

                    for(int a=0;a<responseArray.length();a++)
                    {
                        JSONObject productObject = responseArray.getJSONObject(a);
                        int id = productObject.getInt(GlobalHelper.SQL_COL_ID);
                        String text = productObject.getString(GlobalHelper.SQL_COL_TEXT);

                        Model_Interest product = new Model_Interest();
                        product.setText(text);
                        product.setId(id);

                        userInterests.add(product);
                    }

                    callback.onSuccess(userInterests);
                }
                catch (Exception e)
                {
                    Log.e(TAG, "onResponse: "+e.getMessage() );
                    if(response.equals("error"))
                    {
                        callback.onError("error");
                        return;
                    }
                    else if(response.equals("empty"))
                    {
                        callback.onError("empty");
                    }
                    else
                    {
                        callback.onError(e.getMessage());
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "onErrorResponse: "+ error );
            }
        };


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalHelper.GET_USER_INTERESTS_URL, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();
                int id = (int)gh.getAdminInfo().get(GlobalHelper.SQL_COL_ID);
                myParams.put(GlobalHelper.SQL_COL_USER_ID, String.valueOf(id));
                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion


    //region Add new Client
    public void addNewClient(final Model_Client client, final ISimpleCallback callback)
    {
        currentAddClientCallback = callback;
        currentClientToInsert = client;

        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.e(TAG, response );
                try
                {
                    int newId = Integer.parseInt(response);
                    if(client.getProductIds().size()>0 || client.getInterestIds().size()>0)
                    {
                        insertClientProductAndInterests(newId);
                    }
                    else
                        {
                            currentAddClientCallback.onSuccess("success");
                        }
                }
                catch (Exception e)
                {
                    Log.e(TAG, "onResponse: "+ "got not a number from php" );
                    Log.e(TAG, "Add client error "+e.getMessage() );
                    currentAddClientCallback.onError(e.getMessage());
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "onErrorResponse: "+ error );
                currentAddClientCallback.onError("Connnection error");
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST,GlobalHelper.INSERT_CLIENT_URL, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();
                myParams.put(GlobalHelper.SQL_COL_ADMIN_ID, client.getAdmin_id()+"");
                myParams.put(GlobalHelper.SQL_COL_NAME, client.getName());
                myParams.put(GlobalHelper.SQL_COL_NICK, client.getNick());
                myParams.put(GlobalHelper.SQL_COL_GENDER, client.getGender()+"");
                myParams.put(GlobalHelper.SQL_COL_BIRTHDAY, GlobalHelper.dateFormatForMySQL.format(client.getBirthday()));
                myParams.put(GlobalHelper.SQL_COL_CATEGORY, client.getCategory()+"");
                myParams.put(GlobalHelper.SQL_COL_PHONE, client.getPhone());
                myParams.put(GlobalHelper.SQL_COL_EMAIL, client.getEmail());
                myParams.put(GlobalHelper.SQL_COL_FIRST_VISIT, GlobalHelper.dateFormatForMySQL.format(client.getFirstVisit()));
                myParams.put(GlobalHelper.SQL_COL_LAST_VISIT, GlobalHelper.dateFormatForMySQL.format(client.getLastVisit()));
                myParams.put(GlobalHelper.SQL_COL_SALE, client.getSale()+"");
                myParams.put(GlobalHelper.SQL_COL_COMMENT, client.getComment());

                myParams.put(GlobalHelper.SQL_COL_VK_ID,client.getVkId());
                myParams.put(GlobalHelper.SQL_COL_CITY,client.getCity());
                myParams.put(GlobalHelper.SQL_COL_DISTRICT,client.getDistrict());

                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion


    //region EditClient
    public void editClient(final Model_Client client, final ISimpleCallback callback)
    {
        Log.e(TAG, "editClient: Begin Client Edit");
        currentEditClientCallBack=callback;
        currentClientToEdit = client;
        Log.e(TAG, "editClient: Client id to edit is "+client.getId() );

        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.e(TAG, "onResponse: get Response on Client Edit");
                Log.e(TAG, "onResponse: "+response );

                int id = client.getId();
                deleteClientProductsInterests(id);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "onErrorResponse: Error on editing Client" );
                Log.e(TAG, "onErrorResponse: "+ error );
                currentEditClientCallBack.onError("Connnection error");
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST,GlobalHelper.EDIT_CLIENT_URL, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();
                myParams.put(GlobalHelper.SQL_COL_ID, client.getId()+"");
                myParams.put(GlobalHelper.SQL_COL_ADMIN_ID, client.getAdmin_id()+"");
                myParams.put(GlobalHelper.SQL_COL_NAME, client.getName());
                myParams.put(GlobalHelper.SQL_COL_NICK, client.getNick());
                myParams.put(GlobalHelper.SQL_COL_GENDER, client.getGender()+"");
                myParams.put(GlobalHelper.SQL_COL_BIRTHDAY, GlobalHelper.dateFormatForMySQL.format(client.getBirthday()));
                myParams.put(GlobalHelper.SQL_COL_CATEGORY, client.getCategory()+"");
                myParams.put(GlobalHelper.SQL_COL_PHONE, client.getPhone());
                myParams.put(GlobalHelper.SQL_COL_EMAIL, client.getEmail());
                myParams.put(GlobalHelper.SQL_COL_FIRST_VISIT, GlobalHelper.dateFormatForMySQL.format(client.getFirstVisit()));
                myParams.put(GlobalHelper.SQL_COL_LAST_VISIT, GlobalHelper.dateFormatForMySQL.format(client.getLastVisit()));
                myParams.put(GlobalHelper.SQL_COL_SALE, client.getSale()+"");
                myParams.put(GlobalHelper.SQL_COL_COMMENT, client.getComment());


                myParams.put(GlobalHelper.SQL_COL_VK_ID,client.getVkId());
                myParams.put(GlobalHelper.SQL_COL_CITY,client.getCity());
                myParams.put(GlobalHelper.SQL_COL_DISTRICT,client.getDistrict());


                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }



    private void deleteClientProductsInterests(final int clientId)
    {
        Log.e(TAG, "deleteClientProductsInterests: Begin Deleting Client Products and Interests" );
        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                currentAddClientCallback = currentEditClientCallBack;
                currentClientToInsert= currentClientToEdit;

                if(currentClientToEdit.getProductIds().size()>0 || currentClientToEdit.getInterestIds().size()>0)
                {
                    Log.e(TAG, "onResponse: Deleting Succesfull, new prod and interests more 0 will add" );
                    insertClientProductAndInterests(clientId);
                }
                else
                {
                    Log.e(TAG, "onResponse: Deleting succesfull no more prodcts and interests, will make callback" );
                    currentEditClientCallBack.onSuccess("success");
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "onErrorResponse: Error on deleting products and interests");
                Log.e(TAG, "onErrorResponse: "+ error );
                currentEditClientCallBack.onError(error.getMessage());
            }
        };


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalHelper.DELETE_CLIENT_PROUDCT_INTERESTS_URL, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();
                myParams.put(GlobalHelper.SQL_COL_CLIENT_ID, String.valueOf(clientId));
                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion


    //region insert Client Products and Interests
    private void insertClientProductAndInterests(final int newId)
    {
        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.e(TAG, "onResponse: "+response );
                currentAddClientCallback.onSuccess(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "onErrorResponse: "+ error );
                currentAddClientCallback.onError(error.getMessage());
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST,GlobalHelper.INSERT_CLIENT_PRODUCTS_AND_INTERESTS_URL, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();

                if(currentClientToInsert.getProductIds().size() >0)
                {
                    JSONArray productIds = new JSONArray(currentClientToInsert.getProductIds());
                    String strProduct = productIds.toString();
                    myParams.put(GlobalHelper.PRODUCTS,strProduct);

                    Log.e(TAG, "Product Str "+strProduct );
                }

                if(currentClientToInsert.getInterestIds().size() >0)
                {
                    JSONArray interestIds = new JSONArray(currentClientToInsert.getInterestIds());
                    String strInterest = interestIds.toString();
                    myParams.put(GlobalHelper.INTERESTS,strInterest);

                    Log.e(TAG, "Interest str "+strInterest );
                }

                int adminId = (int)gh.getAdminInfo().get(GlobalHelper.SQL_COL_ID);
                myParams.put(GlobalHelper.SQL_COL_ADMIN_ID, adminId+"");
                myParams.put(GlobalHelper.SQL_COL_CLIENT_ID, newId+"");

                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion


    //region load All Clients
    public void loadAllClients(final int adminId, final IGetAllMyClients callback)
    {
        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
//                Log.e(TAG, "onResponse: "+response );
                Log.e(TAG, "Got all clients Response" );
                try
                {
                    JSONArray responseArray = new JSONArray(response);
                    List<Model_Client> allClients = new ArrayList<>();
                    for(int a=0;a<responseArray.length();a++)
                    {
                        Model_Client newClient = new Model_Client();
                        JSONObject currentObject = responseArray.getJSONObject(a);


                        newClient.setId(currentObject.getInt(GlobalHelper.SQL_COL_ID));
                        newClient.setAdmin_id(currentObject.getInt(GlobalHelper.SQL_COL_ADMIN_ID));
                        newClient.setName(currentObject.getString(GlobalHelper.SQL_COL_NAME));
                        newClient.setNick(currentObject.getString(GlobalHelper.SQL_COL_NICK));
                        newClient.setGender(currentObject.getInt(GlobalHelper.SQL_COL_GENDER));

                        newClient.setVkId(currentObject.getString(GlobalHelper.SQL_COL_VK_ID));
                        newClient.setCity(currentObject.getString(GlobalHelper.SQL_COL_CITY));
                        newClient.setDistrict(currentObject.getString(GlobalHelper.SQL_COL_DISTRICT));

                        if(currentObject.getString(GlobalHelper.SQL_COL_BIRTHDAY) != null)
                        {
                            Date birthDay = GlobalHelper.dateFormatForMySQL.parse(currentObject.getString(GlobalHelper.SQL_COL_BIRTHDAY));
                            newClient.setBirthday(birthDay);
                        }

                        newClient.setCategory(currentObject.getInt(GlobalHelper.SQL_COL_CATEGORY));
                        newClient.setPhone(currentObject.getString(GlobalHelper.SQL_COL_PHONE));
                        newClient.setEmail(currentObject.getString(GlobalHelper.SQL_COL_EMAIL));

                        Date firstVisit = GlobalHelper.dateFormatForMySQL.parse(currentObject.getString(GlobalHelper.SQL_COL_FIRST_VISIT));
                        newClient.setFirstVisit(firstVisit);

                        Date lastVisit = GlobalHelper.dateFormatForMySQL.parse(currentObject.getString(GlobalHelper.SQL_COL_LAST_VISIT));
                        newClient.setLastVisit(lastVisit);

                        newClient.setSale(currentObject.getInt(GlobalHelper.SQL_COL_SALE));
                        newClient.setComment(currentObject.getString(GlobalHelper.SQL_COL_COMMENT));

                        JSONArray productsArray = currentObject.getJSONArray(GlobalHelper.PRODUCTS);
                        List<Integer> productIds = new ArrayList<>();
                        for (int i=0;i<productsArray.length();i++)
                        {
                            productIds.add(productsArray.getInt(i));
                        }
                        newClient.setProductIds(productIds);

                        JSONArray interstsArray = currentObject.getJSONArray(GlobalHelper.INTERESTS);
                        List<Integer> interestIds = new ArrayList<>();
                        for(int i=0;i<interstsArray.length();i++)
                        {
                            interestIds.add(interstsArray.getInt(i));
                        }
                        newClient.setInterestIds(interestIds);
                        
                        allClients.add(newClient);
                    }

                    callback.onSuccess(allClients);
                }
                catch (Exception e)
                {
                    Log.e(TAG, "Error on Gettong All Clients "+e.getMessage()+"Line is "+e.getStackTrace()[0].getLineNumber());
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError(error.getMessage());
            }
        };


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalHelper.GET_ALL_CLIENTS_URL, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();
                myParams.put(GlobalHelper.SQL_COL_ADMIN_ID, String.valueOf(adminId));
                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion


    //region Get Client by Id
    public void getClientByID(final int clientId, final IGetClientByID callback)
    {
        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    Model_Client newClient = new Model_Client();
                    JSONObject currentObject = new JSONObject(response);
                    newClient.setId(currentObject.getInt(GlobalHelper.SQL_COL_ID));
                    newClient.setAdmin_id(currentObject.getInt(GlobalHelper.SQL_COL_ADMIN_ID));
                    newClient.setName(currentObject.getString(GlobalHelper.SQL_COL_NAME));
                    newClient.setNick(currentObject.getString(GlobalHelper.SQL_COL_NICK));
                    newClient.setGender(currentObject.getInt(GlobalHelper.SQL_COL_GENDER));


                    newClient.setVkId(currentObject.getString(GlobalHelper.SQL_COL_VK_ID));
                    newClient.setCity(currentObject.getString(GlobalHelper.SQL_COL_CITY));
                    newClient.setDistrict(currentObject.getString(GlobalHelper.SQL_COL_DISTRICT));


                    if(currentObject.getString(GlobalHelper.SQL_COL_BIRTHDAY) != null)
                    {
                        Date birthDay = GlobalHelper.dateFormatForMySQL.parse(currentObject.getString(GlobalHelper.SQL_COL_BIRTHDAY));
                        newClient.setBirthday(birthDay);
                    }


                    newClient.setCategory(currentObject.getInt(GlobalHelper.SQL_COL_CATEGORY));
                    newClient.setPhone(currentObject.getString(GlobalHelper.SQL_COL_PHONE));
                    newClient.setEmail(currentObject.getString(GlobalHelper.SQL_COL_EMAIL));

                    newClient.setCategory(currentObject.getInt(GlobalHelper.SQL_COL_CATEGORY));
                    newClient.setPhone(currentObject.getString(GlobalHelper.SQL_COL_PHONE));
                    newClient.setEmail(currentObject.getString(GlobalHelper.SQL_COL_EMAIL));

                    Date firstVisit = GlobalHelper.dateFormatForMySQL.parse(currentObject.getString(GlobalHelper.SQL_COL_FIRST_VISIT));
                    newClient.setFirstVisit(firstVisit);

                    Date lastVisit = GlobalHelper.dateFormatForMySQL.parse(currentObject.getString(GlobalHelper.SQL_COL_LAST_VISIT));
                    newClient.setLastVisit(lastVisit);

                    newClient.setSale(currentObject.getInt(GlobalHelper.SQL_COL_SALE));
                    newClient.setComment(currentObject.getString(GlobalHelper.SQL_COL_COMMENT));

                    JSONArray productsArray = currentObject.getJSONArray(GlobalHelper.PRODUCTS);
                    List<Integer> productIds = new ArrayList<>();
                    for (int i=0;i<productsArray.length();i++)
                    {
                        productIds.add(productsArray.getInt(i));
                    }
                    newClient.setProductIds(productIds);

                    JSONArray interstsArray = currentObject.getJSONArray(GlobalHelper.INTERESTS);
                    List<Integer> interestIds = new ArrayList<>();
                    for(int i=0;i<interstsArray.length();i++)
                    {
                        interestIds.add(interstsArray.getInt(i));
                    }
                    newClient.setInterestIds(interestIds);

                    callback.onSuccess(newClient);
                }
                catch (Exception e)
                {
                    callback.onError("error");
                }

            }
        };


        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError("Error");
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalHelper.GET_CLIENT_BY_ID_URL, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();
                myParams.put(GlobalHelper.SQL_COL_ID, String.valueOf(clientId));
                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion


    //region Edit Product
    public void editProduct(final String text, final int productId, final ISimpleCallback callback)
    {
        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                callback.onSuccess(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError(error.getMessage());
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalHelper.EDIT_PRODUCT_URL, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();
                myParams.put(GlobalHelper.SQL_COL_PRODUCT_ID, String.valueOf(productId));
                myParams.put(GlobalHelper.SQL_COL_TEXT, text);
                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion


    //region Delete Product
    public void deleteProduct(final int productId, final ISimpleCallback callback)
    {
        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.e(TAG, "Delete product response "+response);
                callback.onSuccess(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError(error.getMessage());
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalHelper.DELETE_PRODUCT_URL, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();
                myParams.put(GlobalHelper.SQL_COL_PRODUCT_ID, String.valueOf(productId));
                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion


    //region editInterest
    public void editInterest(final String text, final int interestId, final ISimpleCallback callback)
    {
        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                callback.onSuccess(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError(error.getMessage());
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalHelper.EDIT_INTEREST_URL, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();
                myParams.put(GlobalHelper.SQL_COL_INTEREST_ID, String.valueOf(interestId));
                myParams.put(GlobalHelper.SQL_COL_TEXT, text);
                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion


    //region Delete Interest
    public void deleteInterest(final int interestId, final ISimpleCallback callback)
    {
        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.e(TAG, "Delete interest response "+response);
                callback.onSuccess(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError(error.getMessage());
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalHelper.DELETE_INTEREST_URL, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();
                myParams.put(GlobalHelper.SQL_COL_INTEREST_ID, String.valueOf(interestId));
                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion


    //region addMultiClients
    public void addMultiClients(List<Model_Client> listToAdd, final ISimpleCallback callback)
    {
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(ctx.getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);

        resGet = 0;
        resSend = 0;

        for(final Model_Client client : listToAdd)
        {

            Response.Listener<String> successListener = new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    Log.e(TAG, response );
                    try
                    {
                        int newId = Integer.parseInt(response);
                        resGet++;

                        if(resSend == resGet)
                        {
                            Log.e(TAG, "All Inserted successfullt");
                            callback.onSuccess(resSend+"");
                        }
                    }
                    catch (Exception e)
                    {
                        Log.e(TAG, response );
                        Log.e(TAG, "Catch error on multiClientAdd");
                        Log.e(TAG, "Error On Inserting All Clients" );
                        callback.onError("Ошибка при добавлении клиентов, повторите позже");
                    }


                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Log.e(TAG, "Error on inserting From Response Error Listener "+ error );
                    callback.onError("Ошибка связи с сервером.");
                }
            };

            StringRequest stringRequest = new StringRequest(Request.Method.POST,GlobalHelper.INSERT_CLIENT_URL, successListener, errorListener)
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError
                {
                    Map<String, String> myParams = new HashMap<>();
                    myParams.put(GlobalHelper.SQL_COL_ADMIN_ID, client.getAdmin_id()+"");
                    myParams.put(GlobalHelper.SQL_COL_NAME, client.getName());
                    myParams.put(GlobalHelper.SQL_COL_NICK, client.getNick());
                    myParams.put(GlobalHelper.SQL_COL_GENDER, client.getGender()+"");
                    myParams.put(GlobalHelper.SQL_COL_BIRTHDAY, GlobalHelper.dateFormatForMySQL.format(client.getBirthday()));
                    myParams.put(GlobalHelper.SQL_COL_CATEGORY, client.getCategory()+"");
                    myParams.put(GlobalHelper.SQL_COL_PHONE, client.getPhone());
                    myParams.put(GlobalHelper.SQL_COL_EMAIL, client.getEmail());
                    myParams.put(GlobalHelper.SQL_COL_FIRST_VISIT, GlobalHelper.dateFormatForMySQL.format(client.getFirstVisit()));
                    myParams.put(GlobalHelper.SQL_COL_LAST_VISIT, GlobalHelper.dateFormatForMySQL.format(client.getLastVisit()));
                    myParams.put(GlobalHelper.SQL_COL_SALE, client.getSale()+"");
                    myParams.put(GlobalHelper.SQL_COL_COMMENT, client.getComment());

                    myParams.put(GlobalHelper.SQL_COL_VK_ID,client.getVkId());
                    myParams.put(GlobalHelper.SQL_COL_CITY,client.getCity());
                    myParams.put(GlobalHelper.SQL_COL_DISTRICT,client.getDistrict());

                    return myParams;
                }
            };

            requestQueue.add(stringRequest);
            resSend++;
        }


        requestQueue.start();
    }
    //endregion


    //region Insert Excel
    public void makeExcel(List<Model_Client> clients, final ISimpleCallback callback)
    {
        String Ids = "";
        for(Model_Client cli : clients)
        {
            Ids = Ids+cli.getId();
            if(clients.indexOf(cli) != clients.size()-1)
            {
                Ids+="-";
            }
        }


        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                callback.onSuccess(response);

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError("error");
            }
        };


        final String finalIds = Ids;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,GlobalHelper.MAKE_EXCEL, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                int adminId = (int)gh.getAdminInfo().get(GlobalHelper.SQL_COL_ID);
                Map<String, String> myParams = new HashMap<>();
                myParams.put(GlobalHelper.SQL_COL_ID_LIST, finalIds);
                myParams.put(GlobalHelper.SQL_COL_ADMIN_ID, adminId+"");

                return myParams;
            }
        };


        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion


    //region Load All Excel
    public void loadAllExcel(final IGetAllExcelsCallback callback)
    {
        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.e(TAG, "Get all Excels Response "+response );
                try
                {
                    JSONArray responseArray = new JSONArray(response);
                    List<Model_Excel_Item> allItems = new ArrayList<>();
                    for(int a=0;a<responseArray.length();a++)
                    {
                        Model_Excel_Item newItem = new Model_Excel_Item();
                        JSONObject currentObject = responseArray.getJSONObject(a);
                        newItem.setId(currentObject.getInt(GlobalHelper.SQL_COL_ID));
                        newItem.setAdmin_id(currentObject.getInt(GlobalHelper.SQL_COL_ADMIN_ID));
                        newItem.setClient_num(currentObject.getInt(GlobalHelper.SQL_COL_CLIENT_NUM));

                        Date makeDate = GlobalHelper.dateFormatForMySQL.parse(currentObject.getString(GlobalHelper.SQL_COL_MAKE_DATE));
                        newItem.setMake_date(makeDate);

                        newItem.setFilename(currentObject.getString(GlobalHelper.SQL_COL_FILENAME));

                        allItems.add(newItem);
                    }

                    callback.onSuccess(allItems);
                }
                catch (Exception e)
                {
                    Log.e(TAG, "onResponse: Json catch in response" );
                    callback.onError("Json reading error");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "onErrorResponse: Volley Error" );
            }
        };


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalHelper.GET_ALL_EXCELS, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                int adminId = (int)gh.getAdminInfo().get(GlobalHelper.SQL_COL_ID);
                Map<String, String> myParams = new HashMap<>();
                myParams.put(GlobalHelper.SQL_COL_ADMIN_ID, String.valueOf(adminId));
                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion


    //region Insert Filter
    public void InsertNewFilter(final Model_Filter filter, final ISimpleCallback callback)
    {
        currentAddFilterCallBack = callback;
        currentFilterToInsert = filter;

        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    int newId = Integer.parseInt(response);
                    if(filter.getListProductIds().size()>0 || filter.getListInterestIds().size()>0)
                    {
                        Log.e(TAG, "Insert Filter Callback Get success and no products and interests will callback");
                        insertFilterProductAndInterests(newId);
                    }
                    else
                    {
                        Log.e(TAG, "Insert Filter Callback Have products or interests will add filter products and interests" );
                        callback.onSuccess("success");
                    }
                }
                catch (Exception e)
                {
                    Log.e(TAG, "onResponse: "+ "got non number" );
                    Log.e(TAG, "Add filter error "+e.getMessage() );
                    callback.onError("error");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "onErrorResponse: "+error.getMessage() );
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST,GlobalHelper.INSERT_FILTER, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();
                int adminId = (int)gh.getAdminInfo().get(GlobalHelper.SQL_COL_ID);
                myParams.put(GlobalHelper.SQL_COL_ID, filter.getId() +"");

                myParams.put(GlobalHelper.SQL_COL_ADMIN_ID, adminId+"");
                myParams.put(GlobalHelper.SQL_COL_NAME, filter.getName());
                myParams.put(GlobalHelper.SQL_COL_CAT_ANY, filter.getCat_any()+"");
                myParams.put(GlobalHelper.SQL_COL_CAT_NEW,filter.getCat_new()+"");
                myParams.put(GlobalHelper.SQL_COL_CAT_POSTOYAN,filter.getCat_postoyan()+"");
                myParams.put(GlobalHelper.SQL_COL_CAT_VIP,filter.getCat_vip()+"");
                myParams.put(GlobalHelper.SQL_COL_GENDER,filter.getGender()+"");
                myParams.put(GlobalHelper.SQL_COL_CITY,filter.getCity()+"");
                myParams.put(GlobalHelper.SQL_COL_DISTRICT,filter.getDistrict()+"");
                myParams.put(GlobalHelper.SQL_COL_AGE_OT,filter.getAge_ot()+"");
                myParams.put(GlobalHelper.SQL_COL_AGE_DO,filter.getAge_do()+"");
                myParams.put(GlobalHelper.SQL_COL_SALE_OT,filter.getSale_ot()+"");
                myParams.put(GlobalHelper.SQL_COL_SALE_DO,filter.getSale_do()+"");
                myParams.put(GlobalHelper.SQL_COL_PHONE,filter.getSearchPhone()+"");
                myParams.put(GlobalHelper.SQL_COL_EMAIL,filter.getSearchEmail()+"");

                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }





    private void insertFilterProductAndInterests(final int newId)
    {
        Log.e(TAG, "insertFilterProductAndInterests: Begin adding filter products and interests" );

        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.e(TAG, "Get response from inserting filter products and interests "+response );
                currentAddFilterCallBack.onSuccess("success");
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "ERRROROROR from inserting filter products and interests "+error.getMessage() );
                currentAddFilterCallBack.onError(error.getMessage());
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST,GlobalHelper.INSERT_FILTER_PRODUCTS_AND_INTERESTS_URL, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();

                if(currentFilterToInsert.getListProductIds().size() >0)
                {
                    JSONArray productIds = new JSONArray(currentFilterToInsert.getListProductIds());
                    String strProduct = productIds.toString();
                    myParams.put(GlobalHelper.PRODUCTS,strProduct);

                    Log.e(TAG, "Product Str "+strProduct );
                }

                if(currentFilterToInsert.getListInterestIds().size() >0)
                {
                    JSONArray interestIds = new JSONArray(currentFilterToInsert.getListInterestIds());
                    String strInterest = interestIds.toString();
                    myParams.put(GlobalHelper.INTERESTS,strInterest);

                    Log.e(TAG, "Interest str "+strInterest );
                }

                int adminId = (int)gh.getAdminInfo().get(GlobalHelper.SQL_COL_ID);
                myParams.put(GlobalHelper.SQL_COL_ADMIN_ID, adminId+"");
                myParams.put(GlobalHelper.SQL_COL_FILTER_ID, newId+"");
                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion


    //region get All User Filters
    public void getAllFilters(final IGetAllFiltersCallback callback)
    {
        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.e(TAG, "allFilters Response "+response );
                Log.e(TAG, "Got all filters Response" );
                try
                {
                    JSONArray responseArray = new JSONArray(response);
                    List<Model_Filter> allFilters = new ArrayList<>();
                    for(int a=0;a<responseArray.length();a++)
                    {
                        Model_Filter newFilter = new Model_Filter();
                        JSONObject currentObject = responseArray.getJSONObject(a);

                        newFilter.setId(currentObject.getInt(GlobalHelper.SQL_COL_ID));
                        newFilter.setAdmin_id(currentObject.getInt(GlobalHelper.SQL_COL_ADMIN_ID));
                        newFilter.setName(currentObject.getString(GlobalHelper.SQL_COL_NAME));

                        newFilter.setCat_any(currentObject.getInt(GlobalHelper.SQL_COL_CAT_ANY));
                        newFilter.setCat_new(currentObject.getInt(GlobalHelper.SQL_COL_CAT_NEW));
                        newFilter.setCat_postoyan(currentObject.getInt(GlobalHelper.SQL_COL_CAT_POSTOYAN));
                        newFilter.setCat_vip(currentObject.getInt(GlobalHelper.SQL_COL_CAT_VIP));

                        newFilter.setGender(currentObject.getInt(GlobalHelper.SQL_COL_GENDER));
                        newFilter.setCity(currentObject.getString(GlobalHelper.SQL_COL_CITY));
                        newFilter.setDistrict(currentObject.getString(GlobalHelper.SQL_COL_DISTRICT));

                        newFilter.setAge_ot(currentObject.getInt(GlobalHelper.SQL_COL_AGE_OT));
                        newFilter.setAge_do(currentObject.getInt(GlobalHelper.SQL_COL_AGE_DO));
                        newFilter.setSale_ot(currentObject.getInt(GlobalHelper.SQL_COL_SALE_OT));
                        newFilter.setSale_do(currentObject.getInt(GlobalHelper.SQL_COL_SALE_DO));

                        newFilter.setSearchPhone(currentObject.getInt(GlobalHelper.SQL_COL_PHONE));
                        newFilter.setSearchEmail(currentObject.getInt(GlobalHelper.SQL_COL_EMAIL));


                        JSONArray productsArray = currentObject.getJSONArray(GlobalHelper.PRODUCTS);
                        List<Integer> productIds = new ArrayList<>();
                        for (int i=0;i<productsArray.length();i++)
                        {
                            productIds.add(productsArray.getInt(i));
                        }
                        newFilter.setListProductIds(productIds);

                        JSONArray interstsArray = currentObject.getJSONArray(GlobalHelper.INTERESTS);
                        List<Integer> interestIds = new ArrayList<>();
                        for(int i=0;i<interstsArray.length();i++)
                        {
                            interestIds.add(interstsArray.getInt(i));
                        }
                        newFilter.setListInterestIds(interestIds);

                        allFilters.add(newFilter);
                    }

                    callback.onSuccess(allFilters);
                }
                catch (Exception e)
                {
                    Log.e(TAG, "Error on Gettong All Clients "+e.getMessage()+"Line is "+e.getStackTrace()[0].getLineNumber());
                }
            }
        };


        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError(error.getMessage());
            }
        };


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalHelper.GET_ALL_FILTERS, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();
                int adminId = (int)gh.getAdminInfo().get(GlobalHelper.SQL_COL_ID);
                myParams.put(GlobalHelper.SQL_COL_ADMIN_ID, String.valueOf(adminId));
                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion


    //region Filter Clients
    public void filterClients(final int filter_id, final IGetAllMyClients callback)
    {
        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.e(TAG, "onResponse: "+response );
                Log.e(TAG, "Got all clients Response" );
                try
                {
                    JSONArray responseArray = new JSONArray(response);
                    List<Model_Client> allClients = new ArrayList<>();
                    for(int a=0;a<responseArray.length();a++)
                    {
                        Model_Client newClient = new Model_Client();
                        JSONObject currentObject = responseArray.getJSONObject(a);


                        newClient.setId(currentObject.getInt(GlobalHelper.SQL_COL_ID));
                        newClient.setAdmin_id(currentObject.getInt(GlobalHelper.SQL_COL_ADMIN_ID));
                        newClient.setName(currentObject.getString(GlobalHelper.SQL_COL_NAME));
                        newClient.setNick(currentObject.getString(GlobalHelper.SQL_COL_NICK));
                        newClient.setGender(currentObject.getInt(GlobalHelper.SQL_COL_GENDER));

                        newClient.setVkId(currentObject.getString(GlobalHelper.SQL_COL_VK_ID));
                        newClient.setCity(currentObject.getString(GlobalHelper.SQL_COL_CITY));
                        newClient.setDistrict(currentObject.getString(GlobalHelper.SQL_COL_DISTRICT));

                        if(currentObject.getString(GlobalHelper.SQL_COL_BIRTHDAY) != null)
                        {
                            Date birthDay = GlobalHelper.dateFormatForMySQL.parse(currentObject.getString(GlobalHelper.SQL_COL_BIRTHDAY));
                            newClient.setBirthday(birthDay);
                        }

                        newClient.setCategory(currentObject.getInt(GlobalHelper.SQL_COL_CATEGORY));
                        newClient.setPhone(currentObject.getString(GlobalHelper.SQL_COL_PHONE));
                        newClient.setEmail(currentObject.getString(GlobalHelper.SQL_COL_EMAIL));

                        Date firstVisit = GlobalHelper.dateFormatForMySQL.parse(currentObject.getString(GlobalHelper.SQL_COL_FIRST_VISIT));
                        newClient.setFirstVisit(firstVisit);

                        Date lastVisit = GlobalHelper.dateFormatForMySQL.parse(currentObject.getString(GlobalHelper.SQL_COL_LAST_VISIT));
                        newClient.setLastVisit(lastVisit);

                        newClient.setSale(currentObject.getInt(GlobalHelper.SQL_COL_SALE));
                        newClient.setComment(currentObject.getString(GlobalHelper.SQL_COL_COMMENT));

                        JSONArray productsArray = currentObject.getJSONArray(GlobalHelper.PRODUCTS);
                        List<Integer> productIds = new ArrayList<>();
                        for (int i=0;i<productsArray.length();i++)
                        {
                            productIds.add(productsArray.getInt(i));
                        }
                        newClient.setProductIds(productIds);

                        JSONArray interstsArray = currentObject.getJSONArray(GlobalHelper.INTERESTS);
                        List<Integer> interestIds = new ArrayList<>();
                        for(int i=0;i<interstsArray.length();i++)
                        {
                            interestIds.add(interstsArray.getInt(i));
                        }
                        newClient.setInterestIds(interestIds);

                        allClients.add(newClient);
                    }

                    callback.onSuccess(allClients);
                }
                catch (Exception e)
                {
                    Log.e(TAG, "Error on Gettong All Clients "+e.getMessage()+"Line is "+e.getStackTrace()[0].getLineNumber());
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError(error.getMessage());
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GlobalHelper.FILTER_CLIENTS, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();
                myParams.put(GlobalHelper.SQL_COL_FILTER_ID, String.valueOf(filter_id));
                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion


    //region Edit Filter
    public void EditFilter(final Model_Filter filter, final ISimpleCallback callback)
    {
        currentEditFilterCallBack = callback;
        currentFilterToEdit = filter;


        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                if(!response.equals("success"))
                {
                    callback.onError("error");
                }

                editFilterProductAndInterests(filter.getId());

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "onErrorResponse: "+error.getMessage() );
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST,GlobalHelper.EDIT_FILTER, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();
                int adminId = (int)gh.getAdminInfo().get(GlobalHelper.SQL_COL_ID);
                myParams.put(GlobalHelper.SQL_COL_ID, filter.getId() +"");

                myParams.put(GlobalHelper.SQL_COL_ADMIN_ID, adminId+"");
                myParams.put(GlobalHelper.SQL_COL_NAME, filter.getName());
                myParams.put(GlobalHelper.SQL_COL_CAT_ANY, filter.getCat_any()+"");
                myParams.put(GlobalHelper.SQL_COL_CAT_NEW,filter.getCat_new()+"");
                myParams.put(GlobalHelper.SQL_COL_CAT_POSTOYAN,filter.getCat_postoyan()+"");
                myParams.put(GlobalHelper.SQL_COL_CAT_VIP,filter.getCat_vip()+"");
                myParams.put(GlobalHelper.SQL_COL_GENDER,filter.getGender()+"");
                myParams.put(GlobalHelper.SQL_COL_CITY,filter.getCity()+"");
                myParams.put(GlobalHelper.SQL_COL_DISTRICT,filter.getDistrict()+"");
                myParams.put(GlobalHelper.SQL_COL_AGE_OT,filter.getAge_ot()+"");
                myParams.put(GlobalHelper.SQL_COL_AGE_DO,filter.getAge_do()+"");
                myParams.put(GlobalHelper.SQL_COL_SALE_OT,filter.getSale_ot()+"");
                myParams.put(GlobalHelper.SQL_COL_SALE_DO,filter.getSale_do()+"");
                myParams.put(GlobalHelper.SQL_COL_PHONE,filter.getSearchPhone()+"");
                myParams.put(GlobalHelper.SQL_COL_EMAIL,filter.getSearchEmail()+"");

                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }

    private void editFilterProductAndInterests(final int id)
    {
        Log.e(TAG, "insertFilterProductAndInterests: Begin adding filter products and interests" );

        Response.Listener<String> successListener = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.e(TAG, "Get response from inserting filter products and interests "+response );
                currentEditFilterCallBack.onSuccess("success");
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "ERRROROROR from inserting filter products and interests "+error.getMessage() );
                currentEditFilterCallBack.onError(error.getMessage());
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST,GlobalHelper.EDIT_FILTER_PRODUCTS_AND_INTERESTS_URL, successListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> myParams = new HashMap<>();

                if(currentFilterToEdit.getListProductIds().size() >0)
                {
                    JSONArray productIds = new JSONArray(currentFilterToEdit.getListProductIds());
                    String strProduct = productIds.toString();
                    myParams.put(GlobalHelper.PRODUCTS,strProduct);

                    Log.e(TAG, "Product Str "+strProduct );
                }

                if(currentFilterToEdit.getListInterestIds().size() >0)
                {
                    JSONArray interestIds = new JSONArray(currentFilterToEdit.getListInterestIds());
                    String strInterest = interestIds.toString();
                    myParams.put(GlobalHelper.INTERESTS,strInterest);

                    Log.e(TAG, "Interest str "+strInterest );
                }

                int adminId = (int)gh.getAdminInfo().get(GlobalHelper.SQL_COL_ID);
                myParams.put(GlobalHelper.SQL_COL_ADMIN_ID, adminId+"");
                myParams.put(GlobalHelper.SQL_COL_FILTER_ID, id+"");
                return myParams;
            }
        };

        Volley.newRequestQueue(ctx).add(stringRequest);
    }
    //endregion
}


















