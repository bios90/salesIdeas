package dimfcompany.com.salesideas.Models;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dimfcompany.com.salesideas.Helpers.GlobalHelper;

public class Model_Client
{
    private static final String TAG = "Model_Client";

    int id;
    int admin_id;
    String name;
    String nick;
    int gender;
    Date birthday = new Date(0);
    int category;
    String phone;
    String email;
    Date firstVisit ;
    Date lastVisit ;
    int sale;
    String comment;

    String vkId;
    String city;
    String district;

    List<Integer> productIds = new ArrayList<>();
    List<Integer> interestIds = new ArrayList<>();

    int posInFullList;

    public final static SimpleDateFormat dateFormatForDisplay = new SimpleDateFormat("d MMMM yyyy");



    private View cardView;

    public Model_Client()
    {

    }

    public void initializeForAdd(Context ctx)
    {
        if(admin_id == 0)
        {
            GlobalHelper gh = (GlobalHelper)ctx.getApplicationContext();
            int adminId = (int)gh.getAdminInfo().get(GlobalHelper.SQL_COL_ID);
            this.admin_id = adminId;
        }
        if(name == null)
        {
            name = "";
        }

        if(nick == null)
        {
            nick = "";
        }

        if(phone == null)
        {
            phone = "";
        }

        if(email == null)
        {
            email = "";
        }

        if(comment == null)
        {
            comment = "";
        }

        if(vkId == null)
        {
            vkId = "";
        }

        if(city == null)
        {
            city = "";
        }

        if(district == null)
        {
            district = "";
        }

        if(birthday == null)
        {
            birthday = new Date(0);
        }

        if(firstVisit == null)
        {
            firstVisit = Calendar.getInstance().getTime();
        }

        if(lastVisit == null)
        {
            lastVisit = Calendar.getInstance().getTime();
        }
    }

    public boolean checkForSearch(String searchText)
    {
        if(name != null)
        {
            if(name.toLowerCase().contains(searchText.toLowerCase()))
            {
                Log.e(TAG, "checkForSearch: Name matches "+name );
                return true;
            }
        }

        if(nick != null)
        {
            if(nick.toLowerCase().contains(searchText.toLowerCase()))
            {
                Log.e(TAG, "checkForSearch: nick matches "+nick);
                return true;
            }
        }

        if(birthday != null )
        {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
            if(!fmt.format(birthday).equals(fmt.format(new Date(0))))
            {
                String bDayFormated = dateFormatForDisplay.format(birthday);
                if(bDayFormated.toLowerCase().contains(searchText.toLowerCase()))
                {
                    Log.e(TAG, "checkForSearch: Bday mathces " + bDayFormated );
                    return true;
                }
            }
        }

        if(phone != null)
        {
            if(phone.toLowerCase().contains(searchText.toLowerCase()))
            {
                Log.e(TAG, "checkForSearch: Phone mathces "+phone );
                return true;
            }
        }

        if(email != null)
        {
            if(email.toLowerCase().contains(searchText.toLowerCase()))
            {
                Log.e(TAG, "checkForSearch: Email mathces "+email );
                return true;
            }
        }

        if(comment != null)
        {
            if(comment.toLowerCase().contains(searchText.toLowerCase()))
            {
                Log.e(TAG, "checkForSearch: Comment Matches "+comment );
                return true;
            }
        }

        if(vkId != null)
        {
            if(vkId.toLowerCase().contains(searchText.toLowerCase()))
            {
                Log.e(TAG, "checkForSearch: VKId mathces "+vkId );
                return true;
            }
        }

        if(city != null)
        {
            if(comment.toLowerCase().contains(searchText.toLowerCase()))
            {
                Log.e(TAG, "checkForSearch: city MAthces "+city );
                return true;
            }
        }

        if(district != null)
        {
            if(comment.toLowerCase().contains(searchText.toLowerCase()))
            {
                Log.e(TAG, "checkForSearch: District Matches "+district );
                return true;
            }
        }

        Log.e(TAG, "checkForSearch: Nothing Matches!!!!!" );
        return false;
    }

    //region Getters and Setters

    public int getPosInFullList()
    {
        return posInFullList;
    }

    public void setPosInFullList(int posInFullList)
    {
        this.posInFullList = posInFullList;
    }

    public String getVkId()
    {
        return vkId;
    }

    public void setVkId(String vkId)
    {
        this.vkId = vkId;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getDistrict()
    {
        return district;
    }

    public void setDistrict(String district)
    {
        this.district = district;
    }


    public View getCardView()
    {
        return cardView;
    }

    public void setCardView(View cardView)
    {
        this.cardView = cardView;
    }

    public int getAdmin_id()
    {
        return admin_id;
    }

    public void setAdmin_id(int admin_id)
    {
        this.admin_id = admin_id;
    }

    public List<Integer> getProductIds()
    {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds)
    {
        this.productIds = productIds;
    }

    public List<Integer> getInterestIds()
    {
        return interestIds;
    }

    public void setInterestIds(List<Integer> interestIds)
    {
        this.interestIds = interestIds;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getNick()
    {
        return nick;
    }

    public void setNick(String nick)
    {
        this.nick = nick;
    }

    public int getGender()
    {
        return gender;
    }

    public void setGender(int gender)
    {
        this.gender = gender;
    }

    public Date getBirthday()
    {
        return birthday;
    }

    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

    public int getCategory()
    {
        return category;
    }

    public void setCategory(int category)
    {
        this.category = category;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Date getFirstVisit()
    {
        return firstVisit;
    }

    public void setFirstVisit(Date firstVisit)
    {
        this.firstVisit = firstVisit;
    }

    public Date getLastVisit()
    {
        return lastVisit;
    }

    public void setLastVisit(Date lastVisit)
    {
        this.lastVisit = lastVisit;
    }

    public int getSale()
    {
        return sale;
    }

    public void setSale(int sale)
    {
        this.sale = sale;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }
    //endregion
}
