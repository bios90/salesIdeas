package dimfcompany.com.salesideas.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Interfaces.IClientCardClicked;
import dimfcompany.com.salesideas.Models.Model_Client;
import dimfcompany.com.salesideas.R;

public class Adapter_AllClients extends RecyclerView.Adapter<Adapter_AllClients.ClientCardVH>
{
    private static final String TAG = "Adapter_AllClients";
    Context ctx;
    GlobalHelper gh;
    List<Model_Client> currentListToShow;
    List<Model_Client> fullListCopy;
    IClientCardClicked callback;
    Drawable drawMale,drawFemale;

    public Adapter_AllClients(Context ctx, List<Model_Client> currentListToShow, IClientCardClicked callback)
    {
        this.ctx = ctx;
        this.currentListToShow = currentListToShow;
        this.callback = callback;
        gh = (GlobalHelper)ctx.getApplicationContext();
        fullListCopy = new ArrayList<>(currentListToShow);

        drawMale = ctx.getResources().getDrawable(R.drawable.ic_user_male);
        drawFemale = ctx.getResources().getDrawable(R.drawable.ic_user_female);
    }

    @NonNull
    @Override
    public ClientCardVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View clientCardView = inflater.inflate(R.layout.item_client_card,viewGroup,false);
        Adapter_AllClients.ClientCardVH cardVH = new Adapter_AllClients.ClientCardVH(clientCardView);
        return cardVH;
    }

    @Override
    public void onBindViewHolder(@NonNull ClientCardVH clientCardVH, int i)
    {
        final Model_Client client = currentListToShow.get(i);

        clientCardVH.setIsRecyclable(false);

        clientCardVH.tvName.setText(client.getName());

        if(client.getPhone().isEmpty())
        {
            clientCardVH.phoneRow.setVisibility(View.INVISIBLE);
        }
        else
        {
            clientCardVH.tvPhone.setText(client.getPhone());
        }

        if(client.getEmail().isEmpty())
        {
            clientCardVH.emailRow.setVisibility(View.INVISIBLE);
        }
        else
        {
            clientCardVH.tvEmail.setText(client.getEmail());
        }

        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        if(fmt.format(client.getBirthday()).equals(fmt.format(new Date(0))))
        {
            clientCardVH.birthdayRow.setVisibility(View.INVISIBLE);
        }
        else
        {
            String birthAge = GlobalHelper.dateFormatForDisplay.format(client.getBirthday());
            clientCardVH.tvBirthday.setText(birthAge);
        }

        if(client.getGender() == 0 )
        {
            clientCardVH.imgAvatart.setImageDrawable(drawMale);
        }
        else
        {
            clientCardVH.imgAvatart.setImageDrawable(drawFemale);
        }

        clientCardVH.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.cardClicked(client);
            }
        });

        clientCardVH.emailRow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.emailClicked(client);
            }
        });

        clientCardVH.phoneRow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.phoneClicked(client);
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
                        client.setPosInFullList(pos);
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

                    if(client.getEmail() != null)
                    {
                        if(client.getEmail().toLowerCase().contains(text))
                        {
                            currentListToShow.add(client);
                           client.setPosInFullList(pos);
                        }
                    }
                }
            }

            notifyDataSetChanged();
    }

//    @Override
//    public long getItemId(int position)
//    {
//        return position;
//    }
//
//    @Override
//    public int getItemViewType(int position)
//    {
//        return position;
//    }

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
        }
    }
}
