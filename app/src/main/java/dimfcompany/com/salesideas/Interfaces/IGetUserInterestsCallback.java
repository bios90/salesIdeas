package dimfcompany.com.salesideas.Interfaces;

import java.util.List;

import dimfcompany.com.salesideas.Models.Model_Interest;
import dimfcompany.com.salesideas.Models.Model_Product;

public interface IGetUserInterestsCallback
{
    void onSuccess(List<Model_Interest> userInterests);
    void onError(String strError);
}
