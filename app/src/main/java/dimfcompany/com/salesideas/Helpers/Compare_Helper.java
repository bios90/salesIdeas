package dimfcompany.com.salesideas.Helpers;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import dimfcompany.com.salesideas.Models.Model_Client;
import dimfcompany.com.salesideas.Models.Model_Phone_Contact;
import dimfcompany.com.salesideas.Models.Model_User_VK;

public class Compare_Helper
{
    private static final String TAG = "Compare_Helper";
    Context ctx;
    GlobalHelper gh;

    public Compare_Helper(Context ctx)
    {
        this.ctx = ctx;
        gh = (GlobalHelper)ctx.getApplicationContext();
    }

    public List<Model_Client> compareExcleToCurrent(List<Model_Client> excelClients, List<Model_Client> allClients)
    {
        Log.e(TAG, "Begin compare Excel Clients ad current");

        try
        {
            Iterator<Model_Client> excelIterator = excelClients.iterator();
            while (excelIterator.hasNext())
            {
                Model_Client cli1 = excelIterator.next();
                String name1 = cli1.getName();
                String phone1 = cli1.getPhone();
                String email1 = cli1.getEmail();

                Iterator<Model_Client> allIterator = allClients.iterator();
                while (allIterator.hasNext())
                {
                    Model_Client cli2 = allIterator.next();
                    String name2 = cli2.getName();
                    String phone2 = cli2.getPhone();
                    String email2 = cli2.getEmail();

                    if (!TextUtils.isEmpty(name1) && !TextUtils.isEmpty(name2))
                    {
                        if (gh.nameEqualName(name1, name2))
                        {
                            excelIterator.remove();
                            allIterator.remove();
                            continue;
                        }
                    }

                    if (!TextUtils.isEmpty(phone1) && !TextUtils.isEmpty(phone2))
                    {
                        phone1 = gh.formatPhoneStr(phone1);
                        phone2 = gh.formatPhoneStr(phone2);

                        if (gh.phoneEqualsPhone(phone1, phone2))
                        {
                            excelIterator.remove();
                            allIterator.remove();
                            continue;
                        }
                    }


                    if (!TextUtils.isEmpty(email1) && !TextUtils.isEmpty(email2))
                    {
                        if (email1.equals(email2))
                        {
                            excelIterator.remove();
                            allIterator.remove();
                            continue;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "compare Error: "+e.getMessage() );
            e.printStackTrace();
        }

        return excelClients;
    }

    public List<Model_Phone_Contact> comparePhoneAndCurrent(List<Model_Phone_Contact> allPhoneContacts, List<Model_Client> currentClients)
    {
        Log.e(TAG, "comparePhoneAndCurrent: Begin Compare Phonecontacts to All Current");

        Iterator<Model_Phone_Contact> contactIterator = allPhoneContacts.iterator();
        while(contactIterator.hasNext())
        {
            Model_Phone_Contact phone_contact = contactIterator.next();

            String phone1 = phone_contact.getPhone();
            phone1 = gh.formatPhoneStr(phone1);

            if(phone1 == null)
            {
                contactIterator.remove();
                continue;
            }

            if(phone1.length() < 5)
            {
                continue;
            }

            Iterator<Model_Client> clientIterator = currentClients.iterator();
            while (clientIterator.hasNext())
            {
                Model_Client client = clientIterator.next();
                if(client.getPhone() == null || client.getPhone().isEmpty())
                {
                    continue;
                }

                String phone2 = client.getPhone();
                phone2 = gh.formatPhoneStr(phone2);

                if(gh.phoneEqualsPhone(phone1,phone2))
                {
                    contactIterator.remove();
                    clientIterator.remove();
                    break;
                }

            }
        }

        return allPhoneContacts;

//        for(int a=0;a<allPhoneContacts.size();a++)
//        {
//            Model_Phone_Contact phoneContactToCompare = allPhoneContacts.get(a);
//
//            for(int j = 0;j<currentClients.size();j++)
//            {
//                Model_Client currentClientToCompare = currentClients.get(j);
//
//                if(currentClientToCompare.getPhone() != null && phoneContactToCompare.getPhone() != null )
//                {
//                    if(!currentClientToCompare.getPhone().isEmpty() && !phoneContactToCompare.getPhone().isEmpty())
//                    {
//                        if(gh.phoneEqualsPhone(currentClientToCompare.getPhone(),phoneContactToCompare.getPhone()))
//                        {
//                            if(!foundedContacts.contains(phoneContactToCompare))
//                            {
//                                foundedContacts.add(phoneContactToCompare);
//                            }
//                            continue;
//                        }
//                    }
//                }
//
//                if(gh.nameEqualName(currentClientToCompare.getName(),phoneContactToCompare.getName()))
//                {
//                    if(!foundedContacts.contains(phoneContactToCompare))
//                    {
//                        foundedContacts.add(phoneContactToCompare);
//                    }
//                    continue;
//                }
//            }
//        }
//
//
//        allPhoneContacts.removeAll(foundedContacts);

    }

    public List<Model_User_VK> compareVkAndCurrent(List<Model_User_VK> vkFriends, List<Model_Client> currentClients)
    {
        Log.e(TAG, "compareVkAndCurrent: Begin Comparing Vk Friends and current!");

//        List<Model_User_VK> foundedUsers = new ArrayList<>();


        Log.e(TAG, "compareVkAndCurrent: Making Id Itaration");
        Iterator<Model_User_VK> vkIterator = vkFriends.iterator();
        while (vkIterator.hasNext())
        {
            Model_User_VK currentUserVk = vkIterator.next();

            String vkId = currentUserVk.getVk_id();

            Iterator<Model_Client> clientIteratorByID = currentClients.iterator();
            while (clientIteratorByID.hasNext())
            {
                Model_Client currentClient = clientIteratorByID.next();

                if (!currentClient.getVkId().isEmpty())
                {
                    if (vkId.equals(currentClient.getVkId()))
                    {
                        clientIteratorByID.remove();
                        vkIterator.remove();
                        break;
                    }
                }
            }
        }

        Log.e(TAG, "compareVkAndCurrent: Making Phone iteration");
        vkIterator = vkFriends.iterator();
        while (vkIterator.hasNext())
        {
            Model_User_VK currentUserVk = vkIterator.next();
            String phone1 = currentUserVk.getVkPhone();

            if(phone1 == null || phone1.isEmpty() || phone1.length() < 5)
            {
                continue;
            }

            phone1 = gh.formatPhoneStr(phone1);

            Iterator<Model_Client> clientIteratorByID = currentClients.iterator();
            while (clientIteratorByID.hasNext())
            {
                Model_Client currentClient = clientIteratorByID.next();
                String phone2 = currentClient.getPhone();
                if (phone2 == null || phone2.isEmpty() || phone2.length() < 5)
                {
                    continue;
                }
                phone2 = gh.formatPhoneStr(phone2);

                if(gh.phoneEqualsPhone(phone1,phone2))
                {
                    clientIteratorByID.remove();
                    vkIterator.remove();
                    break;
                }
            }
        }

        Log.e(TAG, "compareVkAndCurrent: Making Name iteration");
        vkIterator = vkFriends.iterator();

        while (vkIterator.hasNext())
        {
            Model_User_VK currentUserVk = vkIterator.next();
            String name1 = currentUserVk.getFullName();

            Iterator<Model_Client> clientIteratorByID = currentClients.iterator();
            while (clientIteratorByID.hasNext())
            {
                Model_Client currentClient = clientIteratorByID.next();
                String name2 = currentClient.getName();

                if(gh.nameEqualName(name1,name2))
                {
                    clientIteratorByID.remove();
                    vkIterator.remove();
                    break;
                }
            }
        }

        return vkFriends;
    }



    public boolean comparePhoneContactAndText(Model_Phone_Contact contact, String text)
    {

        if(contact.getName().toLowerCase().contains(text))
        {
            return true;
        }

        String textAsPhone = gh.formatPhoneStr(text);
        String contactPhone = gh.formatPhoneStr(contact.getPhone());

        if(gh.phoneEqualsPhone(textAsPhone,contactPhone))
        {
            return true;
        }

        if(contact.getEmail() != null)
        {
            if(contact.getEmail().toLowerCase().contains(text))
            {
                return true;
            }
        }

        return false;
    }

    public List<Model_Client> clietsMatchSearch(List<Model_Client> currentClients, String textToSearch)
    {
        List<Model_Client> sortedClients = new ArrayList<>();
        textToSearch = textToSearch.toLowerCase();

        for(Model_Client cli : currentClients)
        {
            if(cli.getName() != null)
            {
                if(cli.getName().toLowerCase().contains(textToSearch))
                {
                    sortedClients.add(cli);
                    continue;
                }
            }



            if(cli.getPhone() != null)
            {
                String textAsPhone = gh.formatPhoneStr(textToSearch);
                if(!textAsPhone.isEmpty())
                {
                    String cliPhone = gh.formatPhoneStr(cli.getPhone());
                    if(cliPhone.contains(textAsPhone))
                    {
                        sortedClients.add(cli);
                        continue;
                    }
                }

            }

            if(cli.getEmail() != null)
            {
                if(cli.getEmail().toLowerCase().contains(textToSearch))
                {
                    sortedClients.add(cli);
                    continue;
                }
            }
            
            Log.e(TAG, "checkForSearch: Nothing Matches!!!!!" );
        }

        return sortedClients;
    }

//    public List<Model_Client> clietsMatchSearch(List<Model_Client> currentClients, String textToSearch)
//    {
//        List<Model_Client> sortedClients = new ArrayList<>();
//        textToSearch = textToSearch.toLowerCase();
//
//        for(Model_Client cli : currentClients)
//        {
//            if(cli.getName() != null)
//            {
//                if(cli.getName().toLowerCase().contains(textToSearch))
//                {
//                    sortedClients.add(cli);
//                    continue;
//                }
//            }
//
//            if(cli.getNick() != null)
//            {
//                if(cli.getNick().toLowerCase().contains(textToSearch))
//                {
//                    sortedClients.add(cli);
//                    continue;
//                }
//            }
//
//            if(cli.getBirthday()!= null )
//            {
//                SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
//                if(!fmt.format(cli.getBirthday()).equals(fmt.format(new Date(0))))
//                {
//                    String bDayFormated = GlobalHelper.dateFormatForDisplay.format(cli.getBirthday());
//                    if(bDayFormated.toLowerCase().contains(textToSearch))
//                    {
//                        sortedClients.add(cli);
//                        continue;
//                    }
//                }
//            }
//
//            if(cli.getPhone() != null)
//            {
//                String textAsPhone = gh.formatPhoneStr(textToSearch);
//                if(!textAsPhone.isEmpty())
//                {
//                    String cliPhone = gh.formatPhoneStr(cli.getPhone());
//                    if(cliPhone.contains(textAsPhone))
//                    {
//                        sortedClients.add(cli);
//                        continue;
//                    }
//                }
//
//            }
//
//            if(cli.getEmail() != null)
//            {
//                if(cli.getEmail().toLowerCase().contains(textToSearch))
//                {
//                    sortedClients.add(cli);
//                    continue;
//                }
//            }
//
//            if(cli.getComment() != null)
//            {
//                if(cli.getComment().toLowerCase().contains(textToSearch))
//                {
//                    sortedClients.add(cli);
//                    continue;
//                }
//            }
//
//            if(cli.getVkId() != null)
//            {
//                if(cli.getVkId().toLowerCase().contains(textToSearch))
//                {
//                    sortedClients.add(cli);
//                    continue;
//                }
//            }
//
//            if(cli.getCity() != null)
//            {
//                if(cli.getCity().toLowerCase().contains(textToSearch))
//                {
//                    sortedClients.add(cli);
//                    continue;
//                }
//            }
//
//            if(cli.getDistrict() != null)
//            {
//                if(cli.getDistrict().toLowerCase().contains(textToSearch))
//                {
//                    sortedClients.add(cli);
//                    continue;
//                }
//            }
//
//            Log.e(TAG, "checkForSearch: Nothing Matches!!!!!" );
//        }
//
//        return sortedClients;
//    }

//    public List<Model_Client> clietsMatchSearch(List<Model_Client> currentClients, String textToSearch)
//    {
//        List<Model_Client> sortedClients = new ArrayList<>();
//        for(Model_Client cli : currentClients)
//        {
//            if(cli.checkForSearch(textToSearch))
//            {
//                Log.e(TAG, "clietsMatchSearch: Client "+cli.getName()+" matches "+textToSearch);
//                sortedClients.add(cli);
//            }
//        }
//
//        return sortedClients;
//    }
}
