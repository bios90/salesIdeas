package dimfcompany.com.salesideas.Models;

import java.io.File;
import java.util.Date;

public class Model_Excel_Item
{
    int id;
    int admin_id;
    int client_num;
    Date make_date;
    String filename;

    File localFile;

    public Model_Excel_Item()
    {

    }

    public File getLocalFile()
    {
        return localFile;
    }

    public void setLocalFile(File localFile)
    {
        this.localFile = localFile;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getAdmin_id()
    {
        return admin_id;
    }

    public void setAdmin_id(int admin_id)
    {
        this.admin_id = admin_id;
    }

    public int getClient_num()
    {
        return client_num;
    }

    public void setClient_num(int client_num)
    {
        this.client_num = client_num;
    }

    public Date getMake_date()
    {
        return make_date;
    }

    public void setMake_date(Date make_date)
    {
        this.make_date = make_date;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }
}
