package dimfcompany.com.salesideas.Interfaces;

import dimfcompany.com.salesideas.Models.Model_Phone_Contact;

public interface IRecContactCallback
{
    void deleteClicked(Model_Phone_Contact contact);
    void editClicked(Model_Phone_Contact contact);
    void addClicked(Model_Phone_Contact contact);
}
