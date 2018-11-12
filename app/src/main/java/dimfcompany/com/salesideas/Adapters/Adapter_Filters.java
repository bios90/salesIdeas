package dimfcompany.com.salesideas.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Interfaces.IFilterCardClicked;
import dimfcompany.com.salesideas.Models.Model_Filter;
import dimfcompany.com.salesideas.Models.Model_Interest;
import dimfcompany.com.salesideas.Models.Model_Product;
import dimfcompany.com.salesideas.R;

public class Adapter_Filters extends RecyclerView.Adapter<Adapter_Filters.FilterCard>
{
    private static final String TAG = "Adapter_Filters";
    GlobalHelper gh;
    List<Model_Filter> currentFilters;
    Context ctx;
    List<Model_Product> allProducts;
    List<Model_Interest> allInterests;
    int blackTrans;
    IFilterCardClicked callback;


    public Adapter_Filters(Context ctx, List<Model_Filter> allFilters, List<Model_Product> allProducts, List<Model_Interest> allInterests, IFilterCardClicked callback)
    {
        this.ctx = ctx;
        this.currentFilters = allFilters;
        this.allInterests = allInterests;
        this.allProducts = allProducts;
        this.callback = callback;

        blackTrans = ctx.getResources().getColor(R.color.myBlackTrans);
        gh = (GlobalHelper) ctx.getApplicationContext();
    }

    @NonNull
    @Override
    public FilterCard onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View cardView = inflater.inflate(R.layout.item_filter_card, viewGroup, false);
        FilterCard filterCard = new FilterCard(cardView);
        return filterCard;
    }

    @Override
    public void onBindViewHolder(@NonNull final FilterCard filterCard, int i)
    {
        final Model_Filter filter = currentFilters.get(i);

        filterCard.tvTitle.setText(filter.getName());
        String textCateg = GlobalHelper.textOfCategs(filter.getCat_any(), filter.getCat_new(), filter.getCat_postoyan(), filter.getCat_vip());
        filterCard.tvCateg.setText(textCateg);
        if(textCateg.equals("любая"))
        {
            filterCard.tvCateg.setTextColor(blackTrans);
        }
        filterCard.tvAge.setText(GlobalHelper.textOfAge(filter));
        if(filterCard.tvAge.getText().equals("любой"))
        {
            filterCard.tvAge.setTextColor(blackTrans);
        }

        switch (filter.getGender())
        {
            case 0:
                filterCard.tvGender.setText("мужской");
                break;

            case 1:
                filterCard.tvGender.setText("женский");
                break;

            case 2:
                filterCard.tvGender.setText("любой");
                filterCard.tvGender.setTextColor(blackTrans);
                break;
        }


        if (filter.getCity().equals(""))
        {
            filterCard.tvCity.setText("любой");
            filterCard.tvCity.setTextColor(blackTrans);
        } else
        {
            filterCard.tvCity.setText(filter.getCity());
        }

        if (filter.getDistrict().equals(""))
        {
            filterCard.tvDistrict.setText("любой");
            filterCard.tvDistrict.setTextColor(blackTrans);
        } else
        {
            filterCard.tvDistrict.setText(filter.getDistrict());
        }

        switch (filter.getSearchPhone())
        {
            case 0:
                filterCard.tvPhone.setText("неважно");
                filterCard.tvPhone.setTextColor(blackTrans);
                break;

            case 1:
                filterCard.tvPhone.setText("указан");
                break;

            case 2:
                filterCard.tvPhone.setText("не указан");
                break;
        }

        switch (filter.getSearchEmail())
        {
            case 0:
                filterCard.tvMail.setText("неважно");
                filterCard.tvMail.setTextColor(blackTrans);
                break;

            case 1:
                filterCard.tvMail.setText("указан");
                break;

            case 2:
                filterCard.tvMail.setText("не указан");
                break;
        }

        if(filter.getListProductIds().size()>0)
        {
            filterCard.laProducts.setVisibility(View.VISIBLE);
            Log.e(TAG, "Product ids > 0 will set" );
            for(Model_Product product : allProducts)
            {
                int id = product.getId();
                if(!filter.getListProductIds().contains(id))
                {
                    continue;
                }

                final View productView = LayoutInflater.from(ctx).inflate(R.layout.item_prodinter,filterCard.flexProducts,false);
                TextView tvText = productView.findViewById(R.id.tvText);

                tvText.setText(product.getText());
                filterCard.flexProducts.addView(productView);
            }
        }

        if(filter.getListInterestIds().size()>0)
        {
            filterCard.laInterests.setVisibility(View.VISIBLE);
            Log.e(TAG, "Interest ids > 0 will set");
            for(Model_Interest interest : allInterests)
            {
                int id = interest.getId();
                if(!filter.getListInterestIds().contains(id))
                {
                    continue;
                }

                final View productView = LayoutInflater.from(ctx).inflate(R.layout.item_prodinter,filterCard.flexInterests,false);
                TextView tvText = productView.findViewById(R.id.tvText);

                tvText.setText(interest.getText());
                filterCard.flexInterests.addView(productView);
            }
        }

        filterCard.laTitle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                filterCard.expFilterInfo.toggle();
            }
        });

        filterCard.btnFilter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.filterClicked(filter);
            }
        });

        filterCard.btnEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.editClicked(filter);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return currentFilters.size();
    }

    class FilterCard extends RecyclerView.ViewHolder
    {
        RelativeLayout laTitle;
        TextView tvTitle;
        LinearLayout laCategRow, laAgeRow, laGenderRow, laCityRow, laDistrictRow, laMailRow, laPhoneRow, laProducts, laInterests;
        TextView tvCateg, tvAge, tvGender, tvCity, tvDistrict, tvMail, tvPhone;
        FlexboxLayout flexProducts, flexInterests;
        ExpandableLayout expFilterInfo;
        Button btnFilter,btnEdit;

        public FilterCard(@NonNull View itemView)
        {
            super(itemView);

            laTitle = itemView.findViewById(R.id.laTitle);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            laCategRow = itemView.findViewById(R.id.laCategRow);
            laAgeRow = itemView.findViewById(R.id.laAgeRow);
            laGenderRow = itemView.findViewById(R.id.laGenderRow);
            laCityRow = itemView.findViewById(R.id.laCityRow);
            laDistrictRow = itemView.findViewById(R.id.laDistrictRow);
            laMailRow = itemView.findViewById(R.id.laMailRow);
            laPhoneRow = itemView.findViewById(R.id.laPhoneRow);
            laProducts = itemView.findViewById(R.id.laProducts);
            laInterests = itemView.findViewById(R.id.laInterests);
            tvCateg = itemView.findViewById(R.id.tvCateg);
            tvAge = itemView.findViewById(R.id.tvAge);
            tvGender = itemView.findViewById(R.id.tvGender);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvDistrict = itemView.findViewById(R.id.tvDistrict);
            tvMail = itemView.findViewById(R.id.tvMail);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            flexProducts = itemView.findViewById(R.id.flexProducts);
            flexInterests = itemView.findViewById(R.id.flexInterests);
            flexProducts = itemView.findViewById(R.id.flexProducts);
            expFilterInfo = itemView.findViewById(R.id.expFilterInfo);

            btnFilter = itemView.findViewById(R.id.btnOk);
            btnEdit = itemView.findViewById(R.id.btnCancel);
        }
    }
}
