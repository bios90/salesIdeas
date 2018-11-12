package dimfcompany.com.salesideas.Interfaces;

import java.util.List;

import dimfcompany.com.salesideas.Models.Model_Client;

public interface IGetAllMyClients
{
    void onSuccess(List<Model_Client> allClients);
    void onError(String strError);
}
