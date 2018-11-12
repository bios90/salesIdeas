package dimfcompany.com.salesideas.Interfaces;

import dimfcompany.com.salesideas.Models.Model_Excel_Item;

public interface IExcelCardClickCallback
{
    void cardClicked(Model_Excel_Item item);
    void shareClicked(Model_Excel_Item item);
}
