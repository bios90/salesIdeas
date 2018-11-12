package dimfcompany.com.salesideas.Helpers;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import dimfcompany.com.salesideas.Interfaces.ISimpleCallback;

public class DownloadHelper extends AsyncTask<String,Void,String>
{
    private static final String TAG = "DownloadHelper";
    URL url;
    File file;
    ISimpleCallback callback;
    boolean success;



    public DownloadHelper(URL url, File file, ISimpleCallback callback)
    {
        this.url = url;
        this.file = file;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... strings)
    {
        Log.e(TAG, "doInBackground: Begin downloading" );
        try
        {
            int count;
            URLConnection conection = url.openConnection();
            conection.connect();
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            OutputStream output = new FileOutputStream(file);

            byte data[] = new byte[1024];

            while ((count = input.read(data)) != -1)
            {
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        }
        catch (Exception e)
        {
            success = false;
            Log.e(TAG, "doInBackground: Error on downloading" );
            return null;
        }
        success= true;
        Log.e(TAG, "doInBackground: Downloaded succesfully" );
        return null;
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        if(success)
        {
            callback.onSuccess("success");
        }
        else
            {
                callback.onError("error");
            }
    }
}
