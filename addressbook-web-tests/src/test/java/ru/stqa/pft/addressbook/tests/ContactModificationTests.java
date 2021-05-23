package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.Comparator;
import java.util.List;

public class ContactModificationTests extends TestBase{
    @Test
    public void testContactModification()  {
        if (! app.getContactHelper().isThereContact()){
            app.getContactHelper().createContact(new ContactData("Jim", "Smith",
                    "New-York, 24 Avenue,35", "+10777454545", "jm123smith@m.ru",
                    "test1"));
            app.getNavigationHelper().returnToMainPage();
        }
        List<ContactData> before = app.getContactHelper().getContactList();
        app.getContactHelper().selectContact(0);
        app.getContactHelper().initContactModification();
        ContactData contact = new ContactData("Jim", "Smith",
                "New-York, 24 Avenue,35", "+10777454545", "jm123smith@m.ru",
                null);
        app.getContactHelper().fillContactForm(contact,false);
        app.getContactHelper().submitContactModification();
        app.getNavigationHelper().returnToMainPage();

        List<ContactData> after = app.getContactHelper().getContactList();
        Assert.assertEquals(after.size(), before.size());

        before.remove(0);
        before.add(contact);

        Comparator<? super ContactData> byId= (o1, o2) -> Integer.compare(o1.getId(),o2.getId());
        before.sort(byId);
        after.sort(byId);
        Assert.assertEquals(before,after);

    }
}
