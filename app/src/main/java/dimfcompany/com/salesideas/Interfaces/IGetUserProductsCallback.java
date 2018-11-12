package dimfcompany.com.salesideas.Interfaces;

import java.util.List;

import dimfcompany.com.salesideas.Models.Model_Product;

public interface IGetUserProductsCallback
{
    void onSuccess(List<Model_Product> userProducts);
    void onError(String strError);
}
