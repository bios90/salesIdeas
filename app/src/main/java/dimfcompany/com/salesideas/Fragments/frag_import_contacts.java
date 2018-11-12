package dimfcompany.com.salesideas.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.mikepenz.itemanimators.SlideRightAlphaAnimator;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import dimfcompany.com.salesideas.Adapters.Adapter_Contacts;
import dimfcompany.com.salesideas.Helpers.Compare_Helper;
import dimfcompany.com.salesideas.Helpers.GetContactsHelper;
import dimfcompany.com.salesideas.Helpers.GlobalHelper;
import dimfcompany.com.salesideas.Helpers.MySqlHelper;
import dimfcompany.com.salesideas.Interfaces.IGetAllMyClients;
import dimfcompany.com.salesideas.Interfaces.IRecContactCallback;
import dimfcompany.com.salesideas.Interfaces.ISimpleCallback;
import dimfcompany.com.salesideas.Models.Model_Client;
import dimfcompany.com.salesideas.Models.Model_Phone_Contact;
import dimfcompany.com.salesideas.Models.Model_User_VK;
import dimfcompany.com.salesideas.R;
import dimfcompany.com.salesideas.act_edit_vk_user;
import dimfcompany.com.salesideas.act_main;

public class frag_import_contacts extends Fragment
{
    private static final String TAG = "frag_import_contacts";

    act_main actParent;
    GlobalHelper gh;
    View thisFragmentRootView;
    List<Model_Client> allCurrentClients = new ArrayList<>();
    List<Model_Phone_Contact> allPhoneContacts = new ArrayList<>();
//    List<Model_Phone_Contact> currentPhoneContactsToShow = new ArrayList<>();
    Adapter_Contacts adapter;
    AlertDialog spotsDialog;
    Model_Phone_Contact lastContactToEdit;


    RecyclerView recContacts;
    RelativeLayout laAddAllContacts;
    EditText etSearch;

    private static final int CONTACT_EDIT_CODE = 9000;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_import_contacts,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        thisFragmentRootView = view;
        init();

        checkPermissions();
    }









//    private void makeDelete(final Model_Phone_Contact contact)
//    {
//        View phoneCard = contact.getCardView();
//        final ExpandableLayout expContactCard = phoneCard.findViewById(R.id.expContactCard);
//        final int pos = recContacts.indexOfChild(phoneCard);
//
//        Log.e(TAG, "onExpansionUpdate: Pos is "+pos );
//        allPhoneContacts.remove(contact);
//        currentPhoneContactsToShow.remove(contact);
//
//        recContacts.removeViewAt(pos);
//        adapter.notifyItemRemoved(pos);
////        adapter.notifyItemRangeChanged(0,currentPhoneContactsToShow.size());
//        Toast.makeText(actParent, "Size of current is "+currentPhoneContactsToShow.size(), Toast.LENGTH_SHORT).show();
//
//
//        Animation animSlide = AnimationUtils.loadAnimation(actParent,
//                R.anim.slide_left);
//        animSlide.setAnimationListener(new Animation.AnimationListener()
//        {
//            @Override
//            public void onAnimationStart(Animation animation)
//            {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation)
//            {
//                Log.e(TAG, "onExpansionUpdate: Pos is "+pos );
//                allPhoneContacts.remove(contact);
//                currentPhoneContactsToShow.remove(contact);
//
//                recContacts.removeViewAt(pos);
//                adapter.notifyItemRemoved(pos);
//                adapter.notifyItemRangeChanged(0,currentPhoneContactsToShow.size());
//                Toast.makeText(actParent, "Size of current is "+currentPhoneContactsToShow.size(), Toast.LENGTH_SHORT).show();
//
////                expContactCard.collapse();
////                expContactCard.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener()
////                {
////                    @Override
////                    public void onExpansionUpdate(float expansionFraction, int state)
////                    {
////                        if(expansionFraction == 0)
////                        {
////
////                            Log.e(TAG, "onExpansionUpdate: Pos is "+pos );
////                            allPhoneContacts.remove(contact);
////                            currentPhoneContactsToShow.remove(contact);
////
////                            recContacts.removeViewAt(pos);
////                            adapter.notifyItemRemoved(pos);
////                            adapter.notifyItemRangeChanged(0,currentPhoneContactsToShow.size());
////                            Toast.makeText(actParent, "Size of current is "+currentPhoneContactsToShow.size(), Toast.LENGTH_SHORT).show();
////                        }
////                    }
////                });
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation)
//            {
//
//            }
//        });
//
////        expContactCard.startAnimation(animSlide);
//    }


    private void init()
    {
        actParent = (act_main) getActivity();
        gh = (GlobalHelper) actParent.getApplicationContext();
        recContacts = thisFragmentRootView.findViewById(R.id.recAllContacts);
        recContacts.setItemAnimator(new SlideRightAlphaAnimator());
        laAddAllContacts = thisFragmentRootView.findViewById(R.id.laAddAllContacts);
        etSearch = thisFragmentRootView.findViewById(R.id.etSearch);

        laAddAllContacts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addAll();
            }
        });


        etSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String text = etSearch.getText().toString();
                adapter.filter(text);
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }

    private void addAll()
    {
        List<Model_Phone_Contact> contactsToAdd = adapter.getCurrentContacts();
        if(contactsToAdd.size() == 0)
        {
            gh.showAlerter("","Нет контактов для добавления",actParent);
            return;
        }

        spotsDialog = gh.spotsDialogDialog(actParent);
        List<Model_Client> clientsToInsert = new ArrayList<>();

        for(Model_Phone_Contact contact : contactsToAdd)
        {
            Model_Client clientFromVk = gh.phoneContactToClient(contact);
            clientFromVk.initializeForAdd(actParent);
            clientsToInsert.add(clientFromVk);
        }

        MySqlHelper mySqlHelper = new MySqlHelper(actParent);
        mySqlHelper.addMultiClients(clientsToInsert, new ISimpleCallback()
        {
            @Override
            public void onSuccess(String response)
            {
                try
                {
                    resetAll();
                    int addedNum = Integer.parseInt(response);
                    gh.showAlerter("Успешно","Добавлено "+addedNum+" новых клиента",actParent);
                    if(spotsDialog != null)
                    {
                        spotsDialog.dismiss();
                    }
                }
                catch (Exception e)
                {
                    resetAll();
                    gh.showAlerter("Ошибка","Ошибка при добавлении, повторите позже.",actParent);
                    if(spotsDialog != null)
                    {
                        spotsDialog.dismiss();
                    }
                }
            }

            @Override
            public void onError(String strError)
            {
                resetAll();
                gh.showAlerter("Ошибка",strError,actParent);
                if(spotsDialog != null)
                {
                    spotsDialog.dismiss();
                }
            }
        });
    }


    private void loadAllClients()
    {
        Log.e(TAG, "Begin loadAllCurrentUsers" );
        MySqlHelper mySqlHelper = new MySqlHelper(actParent);
        int adminId = (int)gh.getAdminInfo().get(GlobalHelper.SQL_COL_ID);
        mySqlHelper.loadAllClients(adminId, new IGetAllMyClients()
        {
            @Override
            public void onSuccess(List<Model_Client> allClients)
            {
                Log.e(TAG, "Get all clients from Jino ok!" );
                Log.e(TAG, "all clients size is "+allClients.size());
                allCurrentClients = allClients;

                Log.e(TAG, "onSuccess: Size of contacts to show before sorted is "+ allPhoneContacts.size() );
                new makeCompareInBG().execute("");
            }

            @Override
            public void onError(String strError)
            {
                gh.showAlerter("Ошибка","Не удалось загрузить контакты телефона, повторите позже.",actParent);
                Log.e(TAG, "Error on loading current Users!" );
            }
        });
    }



    private void checkPermissions()
    {
        Log.e(TAG, "checkPermissions: Begin Check Permissions");
        if (!XXPermissions.isHasPermission(actParent, Permission.Group.CONTACTS) )
        {
            XXPermissions.with(actParent)
                    .permission(Permission.Group.CONTACTS)
                    .request(new OnPermission()
                    {
                        @Override
                        public void hasPermission(List<String> granted, boolean isAll)
                        {
                            if(isAll)
                            {
                                new loadContactsInBackground().execute("");
                                return;
                            }
                        }

                        @Override
                        public void noPermission(List<String> denied, boolean quick)
                        {
                            gh.showAlerter("Ошибка","Для импорта контактов необходим разрешение для доступа к списку контактов.",actParent);
                            return;
                        }
                    });
        }
        else
        {
            new loadContactsInBackground().execute("");

        }
    }

    private void setRecycler()
    {
        Log.e(TAG, "setRecycler: Begin Setting Recycler");
        Log.e(TAG, "setRecycler: Setting recycler num of contacts to show is "+allPhoneContacts.size() );
        if(allPhoneContacts.size() == 0)
        {
            gh.showAlerter("","Не найдено новых контактов",actParent);
            if(spotsDialog != null)
            {
                spotsDialog.dismiss();
            }
            return;
        }
        adapter = new Adapter_Contacts(actParent, allPhoneContacts, new IRecContactCallback()
        {

            @Override
            public void deleteClicked(Model_Phone_Contact contact)
            {

            }

            @Override
            public void editClicked(Model_Phone_Contact contact)
            {
                makeEdit(contact);
            }

            @Override
            public void addClicked(Model_Phone_Contact contact)
            {
                makeAdd(contact);
            }
        });

        recContacts.setLayoutManager(new LinearLayoutManager(actParent));
        recContacts.setAdapter(adapter);

        if(spotsDialog != null)
        {
            spotsDialog.dismiss();
        }
    }

    private void makeAdd(final Model_Phone_Contact contact)
    {
        Model_Client clientFromContact = gh.phoneContactToClient(contact);
        clientFromContact.initializeForAdd(actParent);

        MySqlHelper mySqlHelper = new MySqlHelper(actParent);
        mySqlHelper.addNewClient(clientFromContact, new ISimpleCallback()
        {
            @Override
            public void onSuccess(String response)
            {
                if(response.equals("success"))
                {
                    gh.showAlerter("Успешно","Новый клиент успешно добавлен",actParent);
                    Animation animSlide = AnimationUtils.loadAnimation(actParent,
                            R.anim.slide_left);
                    animSlide.setAnimationListener(new Animation.AnimationListener()
                    {
                        @Override
                        public void onAnimationStart(Animation animation)
                        {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation)
                        {
                            ExpandableLayout expVkCard = contact.getCardView().findViewById(R.id.expContactCard);
                            expVkCard.collapse();
                            expVkCard.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener()
                            {
                                @Override
                                public void onExpansionUpdate(float expansionFraction, int state)
                                {
                                    if(expansionFraction == 0)
                                    {
                                        int pos = allPhoneContacts.indexOf(contact);
                                        allPhoneContacts.remove(contact);
                                        allPhoneContacts.remove(contact);
                                        recContacts.removeViewAt(pos);
                                        adapter.notifyItemRemoved(pos);
                                        adapter.notifyItemRangeChanged(0,allPhoneContacts.size());
                                        recContacts.invalidate();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation)
                        {

                        }
                    });

                    contact.getCardView().startAnimation(animSlide);
                }

                else
                {
                    gh.showAlerter("Ошибка","Не удалось добавить нового пользователя, повторите позже.",actParent);
                }
            }

            @Override
            public void onError(String strError)
            {
                gh.showAlerter("Ошибка","Не удалось добавить нового пользователя, повторите позже.",actParent);
            }
        });
    }

    private void makeEdit(Model_Phone_Contact contact)
    {
        lastContactToEdit = contact;
        Model_Client vkToClient = gh.phoneContactToClient(contact);
        vkToClient.initializeForAdd(actParent);
        gh.setCurrentClientFromVKEdit(vkToClient);
        Intent intent = new Intent(actParent,act_edit_vk_user.class);
        startActivityForResult(intent,CONTACT_EDIT_CODE);
    }

    private void resetAll()
    {
        Log.e(TAG, "resetAll: Making All Contacts Reset" );
        allPhoneContacts.clear();
        allCurrentClients.clear();
//        currentPhoneContactsToShow.clear();
        recContacts.removeAllViews();
        recContacts.setAdapter(null);

    }

    private class makeCompareInBG extends AsyncTask<String,Void,String>
    {

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            setRecycler();
        }

        @Override
        protected String doInBackground(String... strings)
        {
            Compare_Helper compare_helper = new Compare_Helper(actParent);
            allPhoneContacts = compare_helper.comparePhoneAndCurrent(allPhoneContacts,allCurrentClients);
            return null;
        }
    }

    private class loadContactsInBackground extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            spotsDialog = gh.spotsDialogDialog(actParent,"Загрузка");
            spotsDialog.show();
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Log.e(TAG, "onPostExecute: All phone contacts size is "+allPhoneContacts.size() );
        }

        @Override
        protected String doInBackground(String... strings)
        {
            Log.e(TAG, "getAllContacts: Begin getting ALl" );
            resetAll();
//            Cursor phones = actParent.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
//            while (phones.moveToNext())
//            {
//                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                if(phoneNumber.length() < 5)
//                {
//                    continue;
//                }
//                String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                allPhoneContacts.add(new Model_Phone_Contact(name,phoneNumber));
//            }
//            phones.close();
//            ContentResolver cr = actParent.getContentResolver();
//            String[] PROJECTION = new String[]
//                    { ContactsContract.RawContacts._ID,
//                            ContactsContract.Contacts.DISPLAY_NAME,
//                            ContactsContract.Contacts.HAS_PHONE_NUMBER
//                    };
//            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, PROJECTION, null, null, null);
//            while (cur.moveToNext())
//            {
//                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
//                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//
//                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0)
//                {
//                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id},ContactsContract.CommonDataKinds.Phone.NUMBER +" ASC LIMIT 1");
//                    while (pCur.moveToNext())
//                    {
//                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//
//                        Model_Phone_Contact newContact = new Model_Phone_Contact();
//                        newContact.setContactId(id);
//                        newContact.setName(name);
//                        newContact.setPhone(phoneNo);
//
////                        //region Get BirthDay
////                        String columns[] =
////                                {
////                                    ContactsContract.CommonDataKinds.Event.START_DATE,
////                                    ContactsContract.CommonDataKinds.Event.TYPE,
////                                    ContactsContract.CommonDataKinds.Event.MIMETYPE,
////                                };
////
////                        String where = ContactsContract.CommonDataKinds.Event.TYPE + "=" + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY + " and " + ContactsContract.CommonDataKinds.Event.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "' and "+ ContactsContract.Data.CONTACT_ID + " = " + id;
////                        String[] selectionArgs = null;
////                        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME;
////
////                        Cursor birthdayCur = cr.query(ContactsContract.Data.CONTENT_URI, columns, where, selectionArgs, sortOrder);
////                        if (birthdayCur.getCount() > 0)
////                        {
////                            while (birthdayCur.moveToNext())
////                            {
////                                String birthday = birthdayCur.getString(birthdayCur.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
////                                newContact.setBdate(birthday);
////                            }
////                        }
////                        birthdayCur.close();
////                        //endregion
//
//
//                        //region get Email
//                        Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Email.DATA}, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, ContactsContract.CommonDataKinds.Email.DATA+" ASC LIMIT 1");
//
//                        while(emailCur.moveToNext())
//                        {
//                            String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
//                            if(email!=null)
//                            {
//                                newContact.setEmail(email);
//                            }
//                        }
//
//                        emailCur.close();
//                        //endregion
//
//                        allPhoneContacts.add(newContact);
//
//                    }
//                    pCur.close();
//                }
//            }
//            cur.close();

            allPhoneContacts = GetContactsHelper.getAll(actParent);
            loadAllClients();
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CONTACT_EDIT_CODE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                gh.showAlerter("Успешно","Новый клиент успешно добавлен",actParent);
                adapter.remove(lastContactToEdit);
            }
            else if(resultCode == Activity.RESULT_CANCELED)
            {
                if(data != null)
                {
                    int err = data.getIntExtra("error", 777);
                    if (err == 999)
                    {
                        gh.showAlerter("Ошибка","Не удалось добавить нового клиента, повторите позже.",actParent);
                    }
                }
            }
        }
    }

}
