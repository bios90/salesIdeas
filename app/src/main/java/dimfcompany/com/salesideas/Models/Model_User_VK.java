package dimfcompany.com.salesideas.Models;

import android.view.View;

public class Model_User_VK
{
    private String vk_id;
    private String firstName;
    private String lastName;
    private int gender;
    private String nick;
    private String bdate;
    private String avaUrl;
    private String vkDomain;
    private String vkPhone;
    private String city;

    View viewAsCard;

    int posInFullList = 99999;

    public Model_User_VK()
    {

    }


    public int getPosInFullList()
    {
        return posInFullList;
    }

    public void setPosInFullList(int posInFullList)
    {
        this.posInFullList = posInFullList;
    }

    public View getViewAsCard()
    {
        return viewAsCard;
    }

    public void setViewAsCard(View viewAsCard)
    {
        this.viewAsCard = viewAsCard;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getFullName()
    {
        return lastName+" "+firstName;
    }

    public String getVkPhone()
    {
        return vkPhone;
    }

    public void setVkPhone(String vkPhone)
    {
        this.vkPhone = vkPhone;
    }

    public String getVkDomain()
    {
        return vkDomain;
    }

    public void setVkDomain(String vkDomain)
    {
        this.vkDomain = vkDomain;
    }

    public String getVk_id()
    {
        return vk_id;
    }

    public void setVk_id(String vk_id)
    {
        this.vk_id = vk_id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public int getGender()
    {
        return gender;
    }

    public void setGender(int gender)
    {
        this.gender = gender;
    }

    public String getNick()
    {
        return nick;
    }

    public void setNick(String nick)
    {
        this.nick = nick;
    }

    public String getBdate()
    {
        return bdate;
    }

    public void setBdate(String bdate)
    {
        this.bdate = bdate;
    }

    public String getAvaUrl()
    {
        return avaUrl;
    }

    public void setAvaUrl(String avaUrl)
    {
        this.avaUrl = avaUrl;
    }
}
