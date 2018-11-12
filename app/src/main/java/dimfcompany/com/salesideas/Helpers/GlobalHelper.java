package dimfcompany.com.salesideas.Helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tapadoo.alerter.Alerter;
import com.vk.sdk.VKSdk;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dimfcompany.com.salesideas.Fragments.frag_clients;
import dimfcompany.com.salesideas.Models.Model_Client;
import dimfcompany.com.salesideas.Models.Model_Filter;
import dimfcompany.com.salesideas.Models.Model_Interest;
import dimfcompany.com.salesideas.Models.Model_Phone_Contact;
import dimfcompany.com.salesideas.Models.Model_Product;
import dimfcompany.com.salesideas.Models.Model_User_VK;
import dimfcompany.com.salesideas.R;
import dmax.dialog.SpotsDialog;

public class GlobalHelper extends Application
{
    private static final String TAG = "GlobalHelper";

    public final static String CHECK_IF_EMAIL_IN_USE_URL= "http://www.salesideas.ru/androidphp/checkifemailinuse.php";
    public final static String REGISTER_URL= "http://www.salesideas.ru/androidphp/register.php";
    public final static String LOGIN_URL= "http://www.salesideas.ru/androidphp/login.php";
    public final static String ADD_PRODUCT_URL= "http://www.salesideas.ru/androidphp/addproduct.php";
    public final static String ADD_INTEREST_URL= "http://www.salesideas.ru/androidphp/addinterest.php";
    public final static String GET_USER_PRODUCTS_URL= "http://www.salesideas.ru/androidphp/getuserproducts.php";
    public final static String GET_USER_INTERESTS_URL= "http://www.salesideas.ru/androidphp/getuserinterests.php";
    public final static String INSERT_CLIENT_URL= "http://www.salesideas.ru/androidphp/insertclient.php";
    public final static String EDIT_CLIENT_URL= "http://www.salesideas.ru/androidphp/editclient.php";
    public final static String GET_ALL_CLIENTS_URL= "http://www.salesideas.ru/androidphp/getallclients.php";
    public final static String INSERT_CLIENT_PRODUCTS_AND_INTERESTS_URL= "http://www.salesideas.ru/androidphp/insertclientproductsnadinterests.php";
    public final static String INSERT_FILTER_PRODUCTS_AND_INTERESTS_URL= "http://www.salesideas.ru/androidphp/insertfilterproductsnadinterests.php";
    public final static String EDIT_FILTER_PRODUCTS_AND_INTERESTS_URL= "http://www.salesideas.ru/androidphp/editfilterproductsnadinterests.php";
    public final static String DELETE_CLIENT_PROUDCT_INTERESTS_URL= "http://www.salesideas.ru/androidphp/deleteclientprodinterests.php";
    public final static String GET_CLIENT_BY_ID_URL= "http://www.salesideas.ru/androidphp/getclientbyid.php";
    public final static String EDIT_PRODUCT_URL= "http://www.salesideas.ru/androidphp/editproduct.php";
    public final static String DELETE_PRODUCT_URL= "http://www.salesideas.ru/androidphp/deleteproduct.php";
    public final static String EDIT_INTEREST_URL= "http://www.salesideas.ru/androidphp/editinterest.php";
    public final static String DELETE_INTEREST_URL= "http://www.salesideas.ru/androidphp/deleteinterest.php";
    public final static String MAKE_EXCEL= "http://www.salesideas.ru/androidphp/makeexcel.php";
    public final static String GET_ALL_EXCELS= "http://www.salesideas.ru/androidphp/getalluserexcels.php";
    public static final String SAVE_FILE_URL = "http://www.salesideas.ru/androidphp/tempexcel/savefile.php";
    public static final String INSERT_FILTER = "http://www.salesideas.ru/androidphp/insertfilter.php";
    public static final String GET_ALL_FILTERS = "http://www.salesideas.ru/androidphp/getfilters.php";
    public static final String FILTER_CLIENTS = "http://www.salesideas.ru/androidphp/filterclients.php";
    public static final String EDIT_FILTER = "http://www.salesideas.ru/androidphp/editfilter.php";

    public final static String ADMIN_INFO_PREFS= "AdminPrefs";
    public final static String SQL_COL_EMAIL= "email";
    public final static String SQL_COL_VK_ID= "vk_id";
    public final static String SQL_COL_VK_NAME= "vk_name";
    public final static String SQL_COL_NAME= "name";
    public final static String SQL_COL_PASSWORD= "password";
    public final static String SQL_COL_ID= "id";
    public final static String SQL_COL_USER_ID= "user_id";
    public final static String SQL_COL_TEXT= "text";
    public final static String SQL_COL_NICK= "nick";
    public final static String SQL_COL_GENDER= "gender";
    public final static String SQL_COL_BIRTHDAY= "birthday";
    public final static String SQL_COL_CATEGORY= "category";
    public final static String SQL_COL_PHONE= "phone";
    public final static String SQL_COL_FIRST_VISIT= "first_visit";
    public final static String SQL_COL_LAST_VISIT= "last_visit";
    public final static String SQL_COL_SALE= "sale";
    public final static String SQL_COL_COMMENT= "comment";
    public final static String SQL_COL_ADMIN_ID= "admin_id";
    public final static String SQL_COL_CLIENT_ID= "client_id";
    public final static String SQL_COL_PRODUCT_ID= "product_id";
    public final static String SQL_COL_INTEREST_ID= "interest_id";
    public final static String SQL_COL_VK_API_TOKEN= "vk_api_token";
    public final static String SQL_COL_CITY= "city";
    public final static String SQL_COL_DISTRICT= "district";
    public final static String SQL_COL_ID_LIST= "id_list";
    public final static String SQL_COL_CLIENT_NUM= "client_num";
    public final static String SQL_COL_MAKE_DATE= "make_date";
    public final static String SQL_COL_FILENAME= "filename";
    public final static String SQL_COL_CAT_ANY = "cat_any";
    public final static String SQL_COL_CAT_NEW = "cat_new";
    public final static String SQL_COL_CAT_POSTOYAN = "cat_postoyan";
    public final static String SQL_COL_CAT_VIP = "cat_vip";
    public final static String SQL_COL_AGE_OT = "age_ot";
    public final static String SQL_COL_AGE_DO = "age_do";
    public final static String SQL_COL_SALE_OT = "sale_ot";
    public final static String SQL_COL_SALE_DO = "sale_do";
    public final static String SQL_COL_FILTER_ID = "filter_id";
    public final static String PRODUCTS= "products";
    public final static String INTERESTS= "interests";


    public final static SimpleDateFormat dateFormatForMySQL = new SimpleDateFormat("yyyy-MM-dd");
    public final static SimpleDateFormat dateFormatForDisplay = new SimpleDateFormat("d MMMM yyyy");
    public final static SimpleDateFormat dateFormatAnother = new SimpleDateFormat("dd-MM-yyyy");
    public final static SimpleDateFormat vkDateFormat = new SimpleDateFormat("d.M.yyyy");


    public static final String DATA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static Random RANDOM = new Random();



    Model_Client currentClientToDisplay;
    Model_Client currentClientFromVKEdit;
    Model_Product currentProductToEdit;
    Model_Interest currentInterestToEdit;
    List<Model_Client> currentSorteredClients;

    Model_Filter currentFilterToEdit;

    boolean showVkAdded;
    int vkAddedNum;

    private static String VK_API_TOKEN;

    private final SimpleDateFormat[] possibleDateFormats =
            {
            new SimpleDateFormat("yyyy-MM-dd"),
            new SimpleDateFormat("yyyy.MM.dd"),
            new SimpleDateFormat("yy-MM-dd"),
            new SimpleDateFormat("yy.MM.dd"),
            new SimpleDateFormat("yy/MM/dd"),
            new SimpleDateFormat("MMM dd, yyyy")
            };


    @Override
    public void onCreate()
    {
        super.onCreate();
        VKSdk.initialize(getApplicationContext());
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean isEmailValid(CharSequence email)
    {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
//        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public long dateToLong(Date date)
    {
        long dateAsLong = date.getTime();
        return dateAsLong;
    }

    public String dateToStr(long dateAsLong)
    {
        Date date = new Date(dateAsLong);
        DateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        String s = format.format(date);
        return s;
    }

    public URL excelUrlFromFileName(String filename)
    {
        try
        {
            String str = "http://www.salesideas.ru/androidphp/adminexcel/" + filename + ".xlsx";
            URL url = new URL(str);
            return url;
        }
        catch (Exception e)
        {
            return  null;
        }
    }

    public File mkFileForExcel(Context ctx, String fileName)
    {
        String root = ctx.getApplicationContext().getExternalFilesDir(null).toString();

        final File excelDir = new File(root + "/Excels");
        if (!excelDir.exists())
        {
            excelDir.mkdirs();
        }

        File file = new File(excelDir,fileName+".xlsx");
        return file;
    }

    public File createTempExcelFile(Context ctx,String path) throws IOException
    {
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
            else
            {
                return null;
            }

        String fileName = "tempExcel";
        String root = ctx.getApplicationContext().getExternalFilesDir(null).toString();
        final File TempDir = new File(root + "/TempDir");
        if (!TempDir.exists())
        {
            TempDir.mkdirs();
        }
        File file = File.createTempFile
                (
                    fileName,  /* prefix */
                    format,         /* suffix */
                    TempDir      /* directory */
                );

        return file;
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException
    {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1)
        {
            output.write(buffer, 0, bytesRead);
        }
    }

    public static String textOfCategs(int cat_any,int cat_new, int cat_postoyan, int cat_vip)
    {
        String text = "";
        if(cat_any == 1 || (cat_new == 1 && cat_postoyan == 1 && cat_vip == 1))
        {
            text = "любая";
        }
        else
        {
            if(cat_new ==1)
            {
                text += "Новый";
            }
            if(cat_postoyan == 1)
            {
                if(cat_new == 1)
                {
                    text+="/";
                }
                text+="Постоянный";
            }
            if(cat_vip == 1)
            {
                if(cat_new == 1 || cat_vip == 1)
                {
                    text+="/";
                }
                text+="Vip";
            }
        }

        return text;
    }

    public static List<Model_Product> filterProducts (Model_Filter filter,List<Model_Product> allProducts)
    {
        List<Model_Product> sortedProducts = new ArrayList<>();
        List<Integer> listIds = filter.getListProductIds();

        for(Model_Product product : allProducts)
        {
            int id = product.getId();
            if(listIds.contains(id))
            {
                sortedProducts.add(product);
            }
        }

        return sortedProducts;
    }

    public static List<Model_Interest> filterInterests (Model_Filter filter,List<Model_Interest> allInterests)
    {
        List<Model_Interest> sortedInterests = new ArrayList<>();
        List<Integer> listIds = filter.getListInterestIds();

        for(Model_Interest interest: allInterests)
        {
            int id = interest.getId();
            if(listIds.contains(id))
            {
                sortedInterests.add(interest);
            }
        }

        return sortedInterests;
    }

    public static String textOfAge(Model_Filter filter)
    {
        String answer = "";

        if(filter.getAge_ot() != 9999 && filter.getAge_do() != 9999)
        {
            answer ="от "+filter.getAge_ot()+" до "+filter.getAge_do();
        }else
            {
               if(filter.getAge_ot() == 9999 && filter.getAge_do() == 9999)
               {
                   answer ="любой";
               }
               if(filter.getAge_ot() != 9999)
               {
                   answer ="от "+filter.getAge_ot();
               }
                if(filter.getAge_do() != 9999)
                {
                    answer ="до "+filter.getAge_do();
                }
            }

        return answer;
    }


    public String getAge(int year, int month, int day)
    {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }


    public String getAge(Date date)
    {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }


    public void showAlerter(String title, Activity currentActivity)
    {
        Alerter.create(currentActivity)
        .setTitle(title)
                .setIcon(R.drawable.ic_profile)
            .setDuration(5000)
            .setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Alerter.hide();
                }
            })
        .show();
    }

    public void showAlerter(String title,String text, Activity currentActivity)
    {
        Alerter.create(currentActivity)
                .setTitle(title)
                .setText(text)
                .setIcon(R.drawable.ic_profile)
                .setDuration(5000)
                .setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Alerter.hide();
                    }
                })
                .show();
    }


    public Map<String,Object> getAdminInfo()
    {
        Map<String, Object> info = new HashMap<String, Object>();

        final SharedPreferences prefs = getSharedPreferences(GlobalHelper.ADMIN_INFO_PREFS, MODE_PRIVATE);
        String name = prefs.getString(GlobalHelper.SQL_COL_NAME,null);
        String email = prefs.getString(GlobalHelper.SQL_COL_EMAIL,null);
        String vk_id = prefs.getString(GlobalHelper.SQL_COL_VK_ID,null);
        String vk_name = prefs.getString(GlobalHelper.SQL_COL_NAME,null);
        String vk_api_token = prefs.getString(GlobalHelper.SQL_COL_VK_API_TOKEN,null);
        int id = prefs.getInt(GlobalHelper.SQL_COL_ID,99999999);

        info.put(GlobalHelper.SQL_COL_NAME,name);
        info.put(GlobalHelper.SQL_COL_EMAIL,email);
        info.put(GlobalHelper.SQL_COL_ID,id);
        info.put(GlobalHelper.SQL_COL_VK_ID,vk_id);
        info.put(GlobalHelper.SQL_COL_VK_NAME,vk_name);
        info.put(GlobalHelper.SQL_COL_VK_API_TOKEN,vk_api_token);
        return info;
    }


    public AlertDialog spotsDialogDialog(Context ctx)
    {
        return spotsDialogDialog(ctx,"Загрузк");
    }

    public AlertDialog spotsDialogDialog(Context ctx, String message)
    {
        return new SpotsDialog.Builder()
                .setContext(ctx)
                .setTheme(R.style.Custom)
                .setCancelable(false)
                .setMessage(message)
                .build();
    }

    public boolean isShowVkAdded()
    {
        return showVkAdded;
    }

    public void setShowVkAdded(boolean showVkAdded)
    {
        this.showVkAdded = showVkAdded;
    }

    public int getVkAddedNum()
    {
        return vkAddedNum;
    }

    public void setVkAddedNum(int vkAddedNum)
    {
        this.vkAddedNum = vkAddedNum;
    }

    public static boolean checkFragmentInStack(final FragmentManager fragmentManager, final String fragmentTagName)
    {
        for (int entry = 0; entry < fragmentManager.getBackStackEntryCount(); entry++)
        {
            if (fragmentTagName.equals(fragmentManager.getBackStackEntryAt(entry).getName()))
            {
                return true;
            }
        }
        return false;
    }

    public static Fragment getFragmentIfExists(final FragmentManager fragmentManager, String backStackName)
    {
        for (int entry = 0; entry < fragmentManager.getBackStackEntryCount(); entry++)
        {
            if (backStackName.equals(fragmentManager.getBackStackEntryAt(entry).getName()))
            {
                return (Fragment)fragmentManager.getBackStackEntryAt(entry);
            }
        }
        return null;
    }

    public void makeFragChange(FragmentManager fragManager, Fragment frag, FrameLayout frame)
    {
        if(GlobalHelper.checkFragmentInStack(fragManager,frag.getClass().getName()))
        {
            Fragment backStackFrag = fragManager.findFragmentByTag(frag.getClass().getName());
            fragManager.beginTransaction()
                    .replace(frame.getId(), backStackFrag, frag.getClass().getName())
                    .addToBackStack(frag.getClass().getName())
                    .commit();
        }
        else
        {
            fragManager.beginTransaction()
                    .replace(frame.getId(), frag, frag.getClass().getName())
                    .addToBackStack(frag.getClass().getName())
                    .commit();
        }
    }

    public void makeFragChangeWithoutBackStack(FragmentManager fragManager, Fragment frag, FrameLayout frame)
    {
        fragManager.beginTransaction()
                .replace(frame.getId(), frag, frag.getClass().getName())
                .addToBackStack(null)
                .commit();
    }


    public List<Integer> getSelectedIds(List<Object> listOfItems)
    {

        List<Integer> selected = new ArrayList<>();
        if(listOfItems.isEmpty())
        {
            return selected;
        }

        if(listOfItems.get(0) instanceof Model_Interest)
        {
            List<Model_Interest> listOfInterests = (List<Model_Interest>)(List<?>) listOfItems;
            for (Model_Interest interest : listOfInterests)
            {
                if (interest.getConnectedTogg().isChecked())
                {
                    int id = interest.getId();
                    selected.add(id);
                }
            }
        }

        if(listOfItems.get(0) instanceof Model_Product)
        {
            List<Model_Product> listOfProducts = (List<Model_Product>)(List<?>) listOfItems;
            for (Model_Product product : listOfProducts)
            {
                if (product.getConnectedTogg().isChecked())
                {
                    int id = product.getId();
                    selected.add(id);
                }
            }
        }

        return selected;
    }


    public String formatPhoneStr(String phone)
    {
        if(phone == null)
        {
            return null;
        }
        if(phone.isEmpty())
        {
            return phone;
        }
        phone = phone.replaceAll("[^0-9]", "");

        return phone;
    }

    public boolean phoneEqualsPhone(String phone1, String phone2)
    {
        phone1 = formatPhoneStr(phone1);
        phone2 = formatPhoneStr(phone2);

        phone1 = getLast5NumbersOfPhone(phone1);
        phone2 = getLast5NumbersOfPhone(phone2);

        if(phone1.length() < 5 || phone2.length()<5)
        {
            return false;
        }

        if(phone1.equals(phone2))
        {
            return true;
        }
        return false;
    }

    public String getLast5NumbersOfPhone(String phone)
    {
        if (phone.length() <= 5)
        {
            return phone;
        }
        else if (phone.length() > 5)
        {
            return phone.substring(phone.length() - 5);
        }
        return phone;
    }


    public boolean nameEqualName(String name1, String name2)
    {
        if(numberOfWordsInString(name1) != numberOfWordsInString(name2))
        {
            return false;
        }

        String[] nameArray1 = name1.split("\\s+");
        String[] nameArray2 = name2.split("\\s+");

        for(int a=0;a<nameArray1.length;a++)
        {
            nameArray1[a] = nameArray1[a].toLowerCase();
            nameArray2[a] = nameArray2[a].toLowerCase();
        }

        Arrays.sort(nameArray1);
        Arrays.sort(nameArray2);

        return Arrays.equals(nameArray1,nameArray2);
    }

    public int numberOfWordsInString(String s)
    {
        String trim = s.trim();
        if (trim.isEmpty())
        {
            return 0;
        }
        return trim.split("\\s+").length;
    }


    public Model_Client phoneContactToClient(Model_Phone_Contact phone_contact)
    {
        Model_Client model_client = new Model_Client();
        model_client.setAdmin_id((int)getAdminInfo().get(SQL_COL_ID));

        model_client.setName(phone_contact.getName());
        model_client.setPhone(phone_contact.getPhone());

        if(phone_contact.getEmail() != null)
        {
            model_client.setEmail(phone_contact.getEmail());
        }


        if(phone_contact.getBdate() != null)
        {
            Date bDate = phone_contact.getBdayDate();
            model_client.setBirthday(bDate);
        }

        return model_client;
    }


    public Model_Client vkUserToClient(Model_User_VK user_vk)
    {
        Model_Client model_client = new Model_Client();

        model_client.setAdmin_id((int)getAdminInfo().get(SQL_COL_ID));

        model_client.setVkId(user_vk.getVk_id());
        model_client.setName(user_vk.getFullName());
        model_client.setCity(user_vk.getCity());
        if(user_vk.getGender() == 1)
        {
            model_client.setGender(1);
        }
        else
            {
                model_client.setGender(0);
            }

        try
        {
            Date bdate = vkDateFormat.parse(user_vk.getBdate());
            model_client.setBirthday(bdate);
        }
        catch (Exception e)
        {

            Log.e(TAG, "vkUserToClient: bDate Convert Fail");
        }

        model_client.setPhone(user_vk.getVkPhone());

        return model_client;
    }



    public void changeLinkColors(LinearLayout clickedLa,List<LinearLayout> allLinksList)
    {
        for(LinearLayout navLa : allLinksList)
        {
            TextView tvIcon = (TextView)navLa.getChildAt(1);
            TextView link = (TextView)navLa.getChildAt(2);
            if(navLa == clickedLa)
            {
                tvIcon.setTextColor(getResources().getColor(R.color.myPink));
                link.setTextColor(getResources().getColor(R.color.myPink));
                continue;
            }

            tvIcon.setTextColor(getResources().getColor(R.color.myGray));
            link.setTextColor(getResources().getColor(R.color.myGray));
        }
    }

    public static String randomStr()
    {
        int len = 20;
        StringBuilder sb = new StringBuilder(len);
        for(int a = 0;a<=len;a++)
        {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }
        return sb.toString();
    }


    public Model_Client getCurrentClientToDisplay()
    {
        return currentClientToDisplay;
    }

    public void setCurrentClientToDisplay(Model_Client currentClientToDisplay)
    {
        this.currentClientToDisplay = currentClientToDisplay;
    }

    public Model_Product getCurrentProductToEdit()
    {
        return currentProductToEdit;
    }

    public void setCurrentProductToEdit(Model_Product currentProductToEdit)
    {
        this.currentProductToEdit = currentProductToEdit;
    }

    public Model_Interest getCurrentInterestToEdit()
    {
        return currentInterestToEdit;
    }

    public void setCurrentInterestToEdit(Model_Interest currentInterestToEdit)
    {
        this.currentInterestToEdit = currentInterestToEdit;
    }

    public static String getVkApiToken()
    {
        return VK_API_TOKEN;
    }

    public static void setVkApiToken(String vkApiToken)
    {
        VK_API_TOKEN = vkApiToken;
    }

    public Model_Client getCurrentClientFromVKEdit()
    {
        return currentClientFromVKEdit;
    }

    public void setCurrentClientFromVKEdit(Model_Client currentClientFromVKEdit)
    {
        this.currentClientFromVKEdit = currentClientFromVKEdit;
    }

    public List<Model_Client> getCurrentSorteredClients()
    {
        return currentSorteredClients;
    }

    public void setCurrentSorteredClients(List<Model_Client> currentSorteredClients)
    {
        this.currentSorteredClients = currentSorteredClients;
    }

    public Model_Filter getCurrentFilterToEdit()
    {
        return currentFilterToEdit;
    }

    public void setCurrentFilterToEdit(Model_Filter currentFilterToEdit)
    {
        this.currentFilterToEdit = currentFilterToEdit;
    }
}
