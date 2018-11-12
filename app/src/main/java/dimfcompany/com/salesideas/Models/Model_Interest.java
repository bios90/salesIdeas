package dimfcompany.com.salesideas.Models;

import android.widget.ToggleButton;

public class Model_Interest
{
    int id;
    String text;
    ToggleButton connectedTogg;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public ToggleButton getConnectedTogg()
    {
        return connectedTogg;
    }

    public void setConnectedTogg(ToggleButton connectedTogg)
    {
        this.connectedTogg = connectedTogg;
    }

    public Model_Interest()
    {

    }
}
