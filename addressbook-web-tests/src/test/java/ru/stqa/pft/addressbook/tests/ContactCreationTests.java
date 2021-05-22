package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.Comparator;
import java.util.List;

public class ContactCreationTests extends TestBase {

    @Test
    public void testContactCreation() {

        List<ContactData> before = app.getContactHelper().getContactList();
        ContactData contact = new ContactData("Jim", "Smith",
                "New-York, 24 Avenue,35", "+10777454545", "jm123smith@m.ru",
                "test1");
        app.getContactHelper().createContact(contact);
        app.getNavigationHelper().returnToMainPage();
        List<ContactData> after = app.getContactHelper().getContactList();
        Assert.assertEquals(after.size(), before.size() + 1);

        Comparator<? super ContactData> byId= (o1, o2) -> Integer.compare(o1.getId(),o2.getId());

        contact.setId(after.stream().max(byId).get().getId());
        before.add(contact);

        before.sort(byId);
        after.sort(byId);
        Assert.assertEquals(before, after);
    }

}
