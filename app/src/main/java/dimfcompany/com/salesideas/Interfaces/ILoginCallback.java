package dimfcompany.com.salesideas.Interfaces;

import dimfcompany.com.salesideas.Models.Model_Admin;

public interface ILoginCallback
{
    void onSuccess(Model_Admin admin);
    void onError(String strError);
}
