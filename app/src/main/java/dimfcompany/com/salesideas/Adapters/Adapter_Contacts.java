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

import java.util.ArrayList;
import java.util.List;

import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Interfaces.IRecContactCallback;
import dimfcompany.com.salesideas.Models.Model_Phone_Contact;
import dimfcompany.com.salesideas.R;

public class Adapter_Contacts extends RecyclerView.Adapter<Adapter_Contacts.ContactCardVH>
{
    private static final String TAG = "Adapter_Contacts";
    GlobalHelper gh;
    Context ctx;
    List<Model_Phone_Contact> listOfAllContacts;
    List<Model_Phone_Contact> listOfAllContactsFullCopy = new ArrayList<>();
    IRecContactCallback callback;

    public Adapter_Contacts(Context ctx, List<Model_Phone_Contact> listOfAllContacts, IRecContactCallback contactCallback)
    {
        this.ctx = ctx;
        this.listOfAllContacts = listOfAllContacts;
        this.listOfAllContactsFullCopy = new ArrayList<>(listOfAllContacts);
        this.callback = contactCallback;
        gh = (GlobalHelper)ctx.getApplicationContext();

    }

    @NonNull
    @Override
    public ContactCardVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View clientCardView = inflater.inflate(R.layout.item_contact_card,viewGroup,false);
        ContactCardVH cardVH = new Adapter_Contacts.ContactCardVH(clientCardView);
        return cardVH;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactCardVH contactCardVH, final int i)
    {
        final Model_Phone_Contact phoneContact = listOfAllContacts.get(i);

        phoneContact.setCardView(contactCardVH.itemView);

        contactCardVH.tvName.setText(phoneContact.getName());
        contactCardVH.tvPhone.setText(phoneContact.getPhone());

        final int pos = i;

        if(phoneContact.getEmail() == null)
        {
            contactCardVH.laMailRow.setVisibility(View.INVISIBLE);
        }
        else
            {
                contactCardVH.tvMail.setText(phoneContact.getEmail());
            }


        if(phoneContact.getBdate() == null)
        {
            contactCardVH.laBdayRow.setVisibility(View.GONE);
        }
        else
            {
                contactCardVH.tvBday.setText(phoneContact.getBdate());
            }

        contactCardVH.btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                remove(i);
            }
        });

        contactCardVH.btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.addClicked(phoneContact);
            }
        });

        contactCardVH.btnEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.editClicked(phoneContact);
            }
        });
    }

    public void remove(Model_Phone_Contact contact)
    {
        remove(listOfAllContacts.indexOf(contact));
    }

    public void remove(int position)
    {
        Model_Phone_Contact tempContact = listOfAllContacts.get(position);
        listOfAllContacts.remove(tempContact);
        listOfAllContactsFullCopy.remove(tempContact);

        notifyItemRemoved(position);
        notifyItemRangeChanged(0,listOfAllContacts.size());
    }

    public void filter(String text)
    {
        if(text == null)
        {
            return;
        }
        if(text.isEmpty())
        {
            Log.e(TAG, "filter: Text empty loading full" );
            listOfAllContacts = new ArrayList<>(listOfAllContactsFullCopy);
        }else
            {
                Log.e(TAG, "filter: Text not empty will filter" );
                listOfAllContacts.clear();
                text = text.toLowerCase();

                for(Model_Phone_Contact contact : listOfAllContactsFullCopy)
                {
                    int pos = listOfAllContactsFullCopy.indexOf(contact);
                    if(contact.getName().toLowerCase().contains(text))
                    {
                        listOfAllContacts.add(contact);
                        contact.setPosInFullList(pos);
                        continue;
                    }

                    String textAsPhone = gh.formatPhoneStr(text);
                    String contactPhone = gh.formatPhoneStr(contact.getPhone());

                    if(!textAsPhone.isEmpty() && !contactPhone.isEmpty())
                    {
                        if(contactPhone.contains(textAsPhone))
                        {
                            listOfAllContacts.add(contact);
                            contact.setPosInFullList(pos);
                            continue;
                        }
                    }

                    if(contact.getEmail() != null)
                    {
                        if(contact.getEmail().toLowerCase().contains(text))
                        {
                            listOfAllContacts.add(contact);
                            contact.setPosInFullList(pos);
                        }
                    }
                }

            }

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return listOfAllContacts.size();
    }

    class ContactCardVH extends RecyclerView.ViewHolder
    {
        TextView tvName,tvPhone,tvMail,tvBday;
        LinearLayout laPhoneRow,laMailRow,laBdayRow;
        Button btnAdd,btnEdit,btnDelete;

        public ContactCardVH(@NonNull View itemView)
        {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvMail = itemView.findViewById(R.id.tvMail);
            tvBday = itemView.findViewById(R.id.tvBday);

            laPhoneRow = itemView.findViewById(R.id.laPhoneRow);
            laMailRow = itemView.findViewById(R.id.laMailRow);
            laBdayRow = itemView.findViewById(R.id.laBdayRow);

            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public List<Model_Phone_Contact> getCurrentContacts()
    {
        return listOfAllContacts;
    }
}
