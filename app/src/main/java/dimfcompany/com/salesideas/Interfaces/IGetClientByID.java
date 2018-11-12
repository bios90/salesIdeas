package dimfcompany.com.salesideas.Interfaces;

import dimfcompany.com.salesideas.Models.Model_Client;

public interface IGetClientByID
{
    void onSuccess(Model_Client client);
    void onError(String strError);
}
