package dimfcompany.com.salesideas.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Interfaces.IExcelCardClickCallback;
import dimfcompany.com.salesideas.Models.Model_Client;
import dimfcompany.com.salesideas.Models.Model_Excel_Item;
import dimfcompany.com.salesideas.R;

public class Adapter_Finished_Excel extends RecyclerView.Adapter<Adapter_Finished_Excel.ExcelCardVH>
{
    GlobalHelper gh;
    Context ctx;
    List<Model_Excel_Item> allItems;
    IExcelCardClickCallback callback;

    public Adapter_Finished_Excel(Context ctx, List<Model_Excel_Item> allItems, IExcelCardClickCallback callback)
    {
        this.ctx = ctx;
        this.allItems = allItems;
        this.callback = callback;
        gh = (GlobalHelper)ctx.getApplicationContext();
    }

    @NonNull
    @Override
    public ExcelCardVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View excelCard = inflater.inflate(R.layout.item_excel_card,viewGroup,false);
        ExcelCardVH cardVH = new ExcelCardVH(excelCard);
        return cardVH;
    }

    @Override
    public void onBindViewHolder(@NonNull ExcelCardVH excelCardVH, int i)
    {
        final Model_Excel_Item excelItem = allItems.get(i);
        
        excelCardVH.tvCount.setText(excelItem.getClient_num()+" клиентов");
        String dateStr = GlobalHelper.dateFormatForDisplay.format(excelItem.getMake_date());
        excelCardVH.tvDate.setText(dateStr);
        
        excelCardVH.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.cardClicked(excelItem);
            }
        });
        
        excelCardVH.tvShare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.shareClicked(excelItem);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return allItems.size();
    }

    class ExcelCardVH extends RecyclerView.ViewHolder
    {
        TextView tvDate,tvCount,tvShare;
        public ExcelCardVH(@NonNull View itemView)
        {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvShare = itemView.findViewById(R.id.tvShare);
        }
    }
}
