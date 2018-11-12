package dimfcompany.com.salesideas.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Interfaces.INamesListCallback;
import dimfcompany.com.salesideas.Models.Model_Client;
import dimfcompany.com.salesideas.R;

public class Adapter_Clients_Names extends RecyclerView.Adapter<Adapter_Clients_Names.ClientNameCard>
{
    private static final String TAG = "Adapter_Clients_Names";
    Context ctx;
    GlobalHelper gh;
    List<Model_Client> allClients;
    INamesListCallback callback;
    List<Model_Client> selectedClients = new ArrayList<>();


    public Adapter_Clients_Names(Context ctx, List<Model_Client> allClients, INamesListCallback callback)
    {
        this.ctx = ctx;
        this.allClients = allClients;
        this.callback = callback;
    }



    @NonNull
    @Override
    public ClientNameCard onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View clientCardView = inflater.inflate(R.layout.item_name_with_toggle,viewGroup,false);
        Adapter_Clients_Names.ClientNameCard cardVH = new Adapter_Clients_Names.ClientNameCard(clientCardView);
        return cardVH;
    }

    @Override
    public void onBindViewHolder(@NonNull final ClientNameCard clientNameCard, int i)
    {
        final Model_Client client = allClients.get(i);

        clientNameCard.tvName.setText(client.getName());
        clientNameCard.laRoot.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                clientNameCard.togg.toggle();
                if(clientNameCard.togg.isChecked())
                {
                    selectedClients.add(client);
                }
                else
                    {
                        selectedClients.remove(client);
                    }
                callback.nameClicked();
            }
        });

        if(selectedClients.indexOf(client)>=0)
        {
            clientNameCard.togg.setChecked(true);
        }


    }

    @Override
    public int getItemCount()
    {
        return allClients.size();
    }

    class ClientNameCard extends RecyclerView.ViewHolder
    {
        ToggleButton togg;
        TextView tvName;
        RelativeLayout laRoot;
        public ClientNameCard(@NonNull View itemView)
        {
            super(itemView);
            togg = itemView.findViewById(R.id.togg);
            tvName = itemView.findViewById(R.id.tvName);
            laRoot = itemView.findViewById(R.id.laRoot);
            this.setIsRecyclable(false);
        }
    }

    public List<Model_Client> getAllClients()
    {
        return allClients;
    }

    public List<Model_Client> getAllSelected()
    {
        return selectedClients;
    }

    public void selectAll()
    {
        selectedClients.clear();
        selectedClients = new ArrayList<>(allClients);
        notifyDataSetChanged();
        callback.nameClicked();
    }

    public void ClearAll()
    {
        selectedClients.clear();
        notifyDataSetChanged();
        callback.nameClicked();
    }


}
