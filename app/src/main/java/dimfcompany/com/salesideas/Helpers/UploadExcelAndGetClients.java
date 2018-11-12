package dimfcompany.com.salesideas.Helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dimfcompany.com.salesideas.Interfaces.IGetAllMyClients;
import dimfcompany.com.salesideas.Models.Model_Client;

public class UploadExcelAndGetClients
{
    private static final String TAG = "UploadExcelAndGetClient";

    GlobalHelper gh;
    Context ctx;
    final String twoHyphens = "--";
    final String lineEnd = "\r\n";
    final String boundary = "apiclient-" + System.currentTimeMillis();
    final String mimeType = "multipart/form-data;boundary=" + boundary;
    byte[] multipartBody;


    public void uploadAndGet(File file, final Context ctx, final IGetAllMyClients callback)
    {
        Log.e(TAG, "makeUpload: Begin Making Upload" );
        this.ctx = ctx;
        gh = (GlobalHelper)ctx.getApplicationContext();

        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try
        {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        }
        catch (Exception e)
        {
            Log.e(TAG, "makeUpload: Error on making byte array");
            e.printStackTrace();
            return;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try
        {
            String path = file.getPath();
            String format = "";
            String last3letters = path.substring(path.length() - 3);
            String last4letters = path.substring(path.length() - 4);
            if(last3letters.equals("xls"))
            {
                format=".xls";
            }
            else if(last4letters.equals("xlsx"))
            {
                format = ".xlsx";
            }


            buildPart(dos, bytes, GlobalHelper.randomStr()+format);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            multipartBody = bos.toByteArray();
        }
        catch (IOException e)
        {
            Log.e(TAG, "Errror hererer");
            e.printStackTrace();
        }

        String url = GlobalHelper.SAVE_FILE_URL;
        MultiPartRequest multipartRequest = new MultiPartRequest(url, null, mimeType, multipartBody, new Response.Listener<NetworkResponse>()
        {
            @Override
            public void onResponse(NetworkResponse response)
            {
                try
                {
                    String str = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    if(str.equals("got error") || str.equals("over size") || str.equals("wrong extension"))
                    {
                        callback.onError(str);
                        return;
                    }

                    Log.e(TAG, "onResponse: "+str );

                    JSONArray responseArray = new JSONArray(str);
                    List<Model_Client> allClients = new ArrayList<>();
                    for(int a=0;a<responseArray.length();a++)
                    {
                        Model_Client newClient = new Model_Client();
                        JSONObject currentObject = responseArray.getJSONObject(a);

                        String name = currentObject.getString(GlobalHelper.SQL_COL_NAME);
                        if(name.toLowerCase().contains("имя ") || name.isEmpty())
                        {
                            Log.e(TAG, "onResponse: Zero row will skip" );
                            continue;
                        }

                        newClient.setName(currentObject.getString(GlobalHelper.SQL_COL_NAME));
                        newClient.setNick(currentObject.getString(GlobalHelper.SQL_COL_NICK));
                        newClient.setGender(currentObject.getInt(GlobalHelper.SQL_COL_GENDER));

                        newClient.setCity(currentObject.getString(GlobalHelper.SQL_COL_CITY));
                        newClient.setDistrict(currentObject.getString(GlobalHelper.SQL_COL_DISTRICT));

                        if(currentObject.getString(GlobalHelper.SQL_COL_BIRTHDAY) != null && !currentObject.getString(GlobalHelper.SQL_COL_BIRTHDAY).isEmpty())
                        {
                            Log.e(TAG, "Birthday STR "+ currentObject.getString(GlobalHelper.SQL_COL_BIRTHDAY));
                            Date birthDay = GlobalHelper.dateFormatAnother.parse(currentObject.getString(GlobalHelper.SQL_COL_BIRTHDAY));
                            newClient.setBirthday(birthDay);
                        }

                        newClient.setCategory(currentObject.getInt(GlobalHelper.SQL_COL_CATEGORY));
                        newClient.setPhone(currentObject.getString(GlobalHelper.SQL_COL_PHONE));
                        newClient.setEmail(currentObject.getString(GlobalHelper.SQL_COL_EMAIL));

                        newClient.setSale(currentObject.getInt(GlobalHelper.SQL_COL_SALE));
                        newClient.setComment(currentObject.getString(GlobalHelper.SQL_COL_COMMENT));

                        allClients.add(newClient);
                    }

                    callback.onSuccess(allClients);
                } catch (Exception e)
                {
                    callback.onError("parse error");
                    Log.e(TAG, "Catch error "+e.getMessage() );
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError("got error");
            }
        });

        Volley.newRequestQueue(ctx).add(multipartRequest);
    }



    private void buildPart(DataOutputStream dataOutputStream, byte[] fileData, String fileName) throws IOException
    {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\"" + fileName + "\"" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileData);
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        // read file and write it into form...
        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0)
        {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(lineEnd);
    }

}
