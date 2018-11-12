package dimfcompany.com.salesideas.Interfaces;

import dimfcompany.com.salesideas.Models.Model_Client;

public interface IClientCardClicked
{
    void cardClicked(Model_Client client);
    void phoneClicked(Model_Client client);
    void emailClicked(Model_Client client);
}
