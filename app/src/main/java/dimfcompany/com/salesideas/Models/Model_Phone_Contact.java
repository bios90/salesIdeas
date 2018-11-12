package dimfcompany.com.salesideas.Models;

import android.util.LongSparseArray;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dimfcompany.com.salesideas.Helpers.GlobalHelper;

public class Model_Phone_Contact
{
    private String contactId;
    long id;

    private String name;
    private String phone;
    private String bdate;
    private String email;

    private boolean hidden;

    private View cardView;

    private int PosInFullList;

    private static final SimpleDateFormat[] possibleDateFormats =
            {
                    new SimpleDateFormat("yyyy-MM-dd"),
                    new SimpleDateFormat("yyyy.MM.dd"),
                    new SimpleDateFormat("yy-MM-dd"),
                    new SimpleDateFormat("yy.MM.dd"),
                    new SimpleDateFormat("yy/MM/dd"),
                    new SimpleDateFormat("MMM dd, yyyy")
            };

    public Model_Phone_Contact()
    {

    }

    public Model_Phone_Contact(long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Model_Phone_Contact(String name, String phone)
    {
        this.name = name;
        this.phone = phone;
    }

    public boolean isHidden()
    {
        return hidden;
    }

    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }

    public int getPosInFullList()
    {
        return PosInFullList;
    }

    public void setPosInFullList(int posInFullList)
    {
        PosInFullList = posInFullList;
    }

    public String getContactId()
    {
        return contactId;
    }

    public void setContactId(String contactId)
    {
        this.contactId = contactId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getBdate()
    {
        return bdate;
    }

    public void setBdate(String bdate)
    {
        this.bdate = bdate;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public View getCardView()
    {
        return cardView;
    }

    public void setCardView(View cardView)
    {
        this.cardView = cardView;
    }

    public Date getBdayDate()
    {
        if(bdate == null)
        {
            return new Date(0);
        }

        Date bDayDate;

        for (SimpleDateFormat f : possibleDateFormats)
        {
            try
            {
                bDayDate = f.parse(bdate);
                return bDayDate;
            }
            catch (ParseException e)
                {

                }
        }

        return new Date(0);
    }










    private LongSparseArray<String> emails;
    private LongSparseArray<String> phones;




    public void addEmail(int type, String address) {
        if (emails == null) {
            emails = new LongSparseArray<String>();
        }
        emails.put(type, address);

        if(email == null)
        {
            email = address;
        }
    }

    public void addPhone(int type, String number) {
        if (phones == null) {
            phones = new LongSparseArray<String>();
        }
        phones.put(type, number);
        if(phone == null)
        {
            phone = number;
        }
    }
}
