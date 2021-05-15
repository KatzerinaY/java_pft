package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactModificationTests extends TestBase{
    @Test
    public void testContactCreation()  {
        app.getContactHelper().selectContact();
        app.getContactHelper().initContactModification();
        app.getContactHelper().fillContactForm(new ContactData("Jim", "Smith",
                "New-York, 24 Avenue,35", "+10777454545", "jm123smith@m.ru"));
        app.getContactHelper().submitContactModification();
        app.getNavigationHelper().returnToMainPage();
    }
}
