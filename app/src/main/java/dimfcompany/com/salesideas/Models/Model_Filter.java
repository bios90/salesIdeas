package dimfcompany.com.salesideas.Models;

import java.util.ArrayList;
import java.util.List;

public class Model_Filter
{
    int id;
    int admin_id;
    String name;
    int cat_any = 0;
    int cat_new = 0;
    int cat_postoyan = 0;
    int cat_vip = 0;
    int gender;
    String city;
    String district;
    int age_ot = 9999;
    int age_do = 9999;
    int sale_ot = 9999;
    int sale_do = 9999;
    int searchPhone;
    int searchEmail;
    List<Integer> listProductIds = new ArrayList<>();
    List<Integer> listInterestIds = new ArrayList<>();

    public Model_Filter()
    {

    }

    public int getAdmin_id()
    {
        return admin_id;
    }

    public void setAdmin_id(int admin_id)
    {
        this.admin_id = admin_id;
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

    public int getCat_any()
    {
        return cat_any;
    }

    public void setCat_any(int cat_any)
    {
        this.cat_any = cat_any;
    }

    public int getCat_new()
    {
        return cat_new;
    }

    public void setCat_new(int cat_new)
    {
        this.cat_new = cat_new;
    }

    public int getCat_postoyan()
    {
        return cat_postoyan;
    }

    public void setCat_postoyan(int cat_postoyan)
    {
        this.cat_postoyan = cat_postoyan;
    }

    public int getCat_vip()
    {
        return cat_vip;
    }

    public void setCat_vip(int cat_vip)
    {
        this.cat_vip = cat_vip;
    }

    public int getGender()
    {
        return gender;
    }

    public void setGender(int gender)
    {
        this.gender = gender;
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

    public int getAge_ot()
    {
        return age_ot;
    }

    public void setAge_ot(int age_ot)
    {
        this.age_ot = age_ot;
    }

    public int getAge_do()
    {
        return age_do;
    }

    public void setAge_do(int age_do)
    {
        this.age_do = age_do;
    }

    public int getSale_ot()
    {
        return sale_ot;
    }

    public void setSale_ot(int sale_ot)
    {
        this.sale_ot = sale_ot;
    }

    public int getSale_do()
    {
        return sale_do;
    }

    public void setSale_do(int sale_do)
    {
        this.sale_do = sale_do;
    }

    public int getSearchPhone()
    {
        return searchPhone;
    }

    public void setSearchPhone(int searchPhone)
    {
        this.searchPhone = searchPhone;
    }

    public int getSearchEmail()
    {
        return searchEmail;
    }

    public void setSearchEmail(int searchEmail)
    {
        this.searchEmail = searchEmail;
    }

    public List<Integer> getListProductIds()
    {
        return listProductIds;
    }

    public void setListProductIds(List<Integer> listProductIds)
    {
        this.listProductIds = listProductIds;
    }

    public List<Integer> getListInterestIds()
    {
        return listInterestIds;
    }

    public void setListInterestIds(List<Integer> listInterestIds)
    {
        this.listInterestIds = listInterestIds;
    }
}
