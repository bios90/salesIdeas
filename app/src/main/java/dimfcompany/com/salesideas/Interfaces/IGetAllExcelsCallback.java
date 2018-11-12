package dimfcompany.com.salesideas.Interfaces;

import java.util.List;

import dimfcompany.com.salesideas.Models.Model_Excel_Item;

public interface IGetAllExcelsCallback
{
    void onSuccess(List<Model_Excel_Item> allClients);
    void onError(String strError);
}
