package dimfcompany.com.salesideas.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Interfaces.IClientExcelClicked;
import dimfcompany.com.salesideas.Interfaces.IExcelCardClickCallback;
import dimfcompany.com.salesideas.Models.Model_Client;
import dimfcompany.com.salesideas.Models.Model_User_VK;
import dimfcompany.com.salesideas.R;

public class Adapter_Clients_From_Excel extends RecyclerView.Adapter<Adapter_Clients_From_Excel.ClientCardVH>
{
    private static final String TAG = "Adapter_Clients_From_Ex";
    Context ctx;
    GlobalHelper gh;
    List<Model_Client> currentListToShow;
    List<Model_Client> fullListCopy;
    IClientExcelClicked callback;
    Drawable drawMale,drawFemale;


    public Adapter_Clients_From_Excel(Context ctx, List<Model_Client> currentListToShow, IClientExcelClicked callback)
    {
        this.ctx = ctx;
        this.currentListToShow = currentListToShow;
        this.fullListCopy = new ArrayList<>(currentListToShow);
        this.callback = callback;
        gh = (GlobalHelper)ctx.getApplicationContext();

        drawMale = ctx.getResources().getDrawable(R.drawable.ic_user_male);
        drawFemale = ctx.getResources().getDrawable(R.drawable.ic_user_female);
    }

    @NonNull
    @Override
    public ClientCardVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View clientCardView = inflater.inflate(R.layout.item_client_from_excel,viewGroup,false);
        ClientCardVH cardVH = new ClientCardVH(clientCardView);
        return cardVH;
    }

    @Override
    public void onBindViewHolder(@NonNull ClientCardVH clientCardVH, final int i)
    {
        Log.e(TAG, "onBindViewHolder: Binding!" );

        final Model_Client client = currentListToShow.get(i);

        clientCardVH.tvName.setText(client.getName());


        Log.e(TAG, "Phone "+client.getPhone());
        if(client.getPhone() != null && !client.getPhone().isEmpty())
        {
            clientCardVH.phoneRow.setVisibility(View.VISIBLE);
            clientCardVH.tvPhone.setText(client.getPhone());
        }

        if(client.getEmail() != null && !client.getEmail().isEmpty())
        {
            clientCardVH.emailRow.setVisibility(View.VISIBLE);
            clientCardVH.tvEmail.setText(client.getEmail());
        }

        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        if(!fmt.format(client.getBirthday()).equals(fmt.format(new Date(0))))
        {
            Log.e(TAG, "onBindViewHolder: DAte as int "+client.getBirthday().getTime() );
            String birthAge = GlobalHelper.dateFormatForDisplay.format(client.getBirthday());
            clientCardVH.tvBirthday.setText(birthAge);
            clientCardVH.birthdayRow.setVisibility(View.VISIBLE);
        }

        if(client.getGender() == 0 )
        {
            clientCardVH.imgAvatart.setImageDrawable(drawMale);
        }
        else
        {
            clientCardVH.imgAvatart.setImageDrawable(drawFemale);
        }

        clientCardVH.btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                remove(i);
            }
        });

        clientCardVH.btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.addClicked(client);
            }
        });

        clientCardVH.btnEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.editClicked(client);
            }
        });
    }

    public void filter(String text)
    {
        if(text == null)
        {
            return;
        }

        if(text.isEmpty())
        {
            Log.e(TAG, "filter: Search Texxt is null Will show all" );
            currentListToShow = new ArrayList<>(fullListCopy);
        }
        else
        {
            Log.e(TAG, "filter: text not empty will filter" );
            text = text.toLowerCase();
            currentListToShow.clear();

            for(Model_Client client : fullListCopy)
            {
                int pos = fullListCopy.indexOf(client);

                if(client.getName().toLowerCase().contains(text))
                {
                    currentListToShow.add(client);
                    continue;
                }

                if(client.getEmail().toLowerCase().contains(text))
                {
                    currentListToShow.add(client);
                    continue;
                }

                if(client.getPhone() != null)
                {
                    String textAsPhone = gh.formatPhoneStr(text);
                    String clientPhone = gh.formatPhoneStr(client.getPhone());

                    if(!textAsPhone.isEmpty() && !clientPhone.isEmpty())
                    {
                        if(clientPhone.contains(textAsPhone))
                        {
                            currentListToShow.add(client);
                            client.setPosInFullList(pos);
                            continue;
                        }
                    }
                }
            }
        }

        notifyDataSetChanged();
    }

    public void remove(Model_Client client)
    {
        remove(currentListToShow.indexOf(client));
    }

    public void remove(int position)
    {
        Model_Client tempContact = currentListToShow.get(position);
        currentListToShow.remove(position);
        fullListCopy.remove(tempContact);

        notifyItemRemoved(position);
        notifyItemRangeChanged(0,currentListToShow.size());
    }

    @Override
    public int getItemCount()
    {
        return currentListToShow.size();
    }

    class ClientCardVH extends RecyclerView.ViewHolder
    {
        TextView tvPhone,tvEmail,tvBirthday,tvName;
        LinearLayout phoneRow,emailRow,birthdayRow;
        CircleImageView imgAvatart;
        Button btnAdd,btnEdit,btnDelete;

        public ClientCardVH(@NonNull View itemView)
        {
            super(itemView);

            phoneRow = itemView.findViewById(R.id.laPhoneRow);
            emailRow = itemView.findViewById(R.id.laEmailRow);
            birthdayRow = itemView.findViewById(R.id.laBirthdayRow);

            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvBirthday = itemView.findViewById(R.id.tvBirthday);
            tvName = itemView.findViewById(R.id.tvName);

            imgAvatart = itemView.findViewById(R.id.avatar);

            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public List<Model_Client> getCurrentListToShow()
    {
        return currentListToShow;
    }
}
