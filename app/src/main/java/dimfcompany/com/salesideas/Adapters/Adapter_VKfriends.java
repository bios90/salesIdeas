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
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Interfaces.IVKCardClicked;
import dimfcompany.com.salesideas.Models.Model_Client;
import dimfcompany.com.salesideas.Models.Model_Phone_Contact;
import dimfcompany.com.salesideas.Models.Model_User_VK;
import dimfcompany.com.salesideas.R;

public class Adapter_VKfriends extends RecyclerView.Adapter<Adapter_VKfriends.VkCardVH>
{
    private static final String TAG = "Adapter_VKfriends";
    Context ctx;
    GlobalHelper gh;
    List<Model_User_VK> currentListToShow;
    List<Model_User_VK> fullListCopy;
    IVKCardClicked callback;

    public Adapter_VKfriends(Context ctx, List<Model_User_VK> currentListToShow, IVKCardClicked callback)
    {
        this.ctx = ctx;
        this.currentListToShow = currentListToShow;
        this.callback = callback;
        gh = (GlobalHelper)ctx.getApplicationContext();
        fullListCopy = new ArrayList<>(currentListToShow);
    }

    @NonNull
    @Override
    public VkCardVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View vkCardView = inflater.inflate(R.layout.item_vk_card2,viewGroup,false);
        VkCardVH cardVH = new VkCardVH(vkCardView);
        return cardVH;
    }

    @Override
    public void onBindViewHolder(@NonNull VkCardVH vkCardVH, final int i)
    {
        final Model_User_VK vkUser = currentListToShow.get(i);

//        vkCardVH.setIsRecyclable(false);

        if(vkUser.getPosInFullList() == 99999)
        {
            vkUser.setPosInFullList(i);
        }

        vkCardVH.tvName.setText(vkUser.getFullName());

        if(vkUser.getVkPhone() != null && !vkUser.getVkPhone().isEmpty())
        {
            vkCardVH.phoneRow.setVisibility(View.VISIBLE);
            vkCardVH.tvPhone.setText(vkUser.getVkPhone());
        }


        if(vkUser.getBdate() != null )
        {
            try
            {
                Date userBDay = GlobalHelper.vkDateFormat.parse(vkUser.getBdate());
                vkCardVH.tvBirthday.setText(GlobalHelper.dateFormatForDisplay.format(userBDay));
                vkCardVH.birthdayRow.setVisibility(View.VISIBLE);
            }
            catch (Exception e)
            {
                vkCardVH.birthdayRow.setVisibility(View.INVISIBLE);
            }
        }

        try
        {
            Picasso.get()
                    .load(vkUser.getAvaUrl())
                    .into(vkCardVH.avatar);
        }
        catch (Exception e)
        {
            Log.e(TAG, "onBindViewHolder: Error on loading Avatar");
        }

        vkCardVH.btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int pos = currentListToShow.indexOf(vkUser);
                remove(pos);
            }
        });

        vkCardVH.btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.addClicked(vkUser);
            }
        });

        vkCardVH.btnEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.editClicked(vkUser);
            }
        });
    }

    public void remove(Model_User_VK vkUser)
    {
        remove(currentListToShow.indexOf(vkUser));
    }

    public void remove(int position)
    {
        Model_User_VK tempContact = currentListToShow.get(position);
        currentListToShow.remove(position);
        fullListCopy.remove(tempContact);

        notifyItemRemoved(position);
        notifyItemRangeChanged(0,currentListToShow.size());
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

            for(Model_User_VK user_vk : fullListCopy)
            {
                int pos = fullListCopy.indexOf(user_vk);

                if(user_vk.getFullName().toLowerCase().contains(text))
                {
                    currentListToShow.add(user_vk);
                    user_vk.setPosInFullList(pos);
                    continue;
                }

                if(user_vk.getVkPhone() != null)
                {
                    String textAsPhone = gh.formatPhoneStr(text);
                    String clientPhone = gh.formatPhoneStr(user_vk.getVkPhone());

                    if(!textAsPhone.isEmpty() && !clientPhone.isEmpty())
                    {
                        if(clientPhone.contains(textAsPhone))
                        {
                            currentListToShow.add(user_vk);
                            user_vk.setPosInFullList(pos);
                            continue;
                        }
                    }
                }
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return currentListToShow.size();
    }

    class VkCardVH extends RecyclerView.ViewHolder
    {
        LinearLayout phoneRow,birthdayRow;
        TextView tvPhone,tvBirthday,tvName;
        Button btnAdd,btnDelete,btnEdit;
        CircleImageView avatar;

        public VkCardVH(@NonNull View itemView)
        {
            super(itemView);

            phoneRow = itemView.findViewById(R.id.laPhoneRow);
            birthdayRow = itemView.findViewById(R.id.laBirthdayRow);

            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvBirthday = itemView.findViewById(R.id.tvBirthday);
            tvName = itemView.findViewById(R.id.tvName);

            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnEdit = itemView.findViewById(R.id.btnEdit);

            avatar = itemView.findViewById(R.id.avatar);
        }
    }

    public List<Model_User_VK> getCurrentListToShow()
    {
        return currentListToShow;
    }
}
