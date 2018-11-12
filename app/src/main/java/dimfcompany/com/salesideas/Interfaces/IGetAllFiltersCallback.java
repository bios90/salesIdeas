package dimfcompany.com.salesideas.Interfaces;

import java.util.List;

import dimfcompany.com.salesideas.Models.Model_Filter;

public interface IGetAllFiltersCallback
{
    void onSuccess(List<Model_Filter> allFilters);
    void onError(String strError);
}
