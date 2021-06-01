package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.Comparator;
import java.util.List;

public class ContactCreationTests extends TestBase {

    @Test
    public void testContactCreation() {

        List<ContactData> before = app.contact().list();
        ContactData contact = new ContactData().withFirstname("Jim").withLastname("Smith")
                              .withAddress("New-York, 24 Avenue,35").withPhoneHome("+10777454545")
                              .withEmail("jm123smith@m.ru").withGroup("test1");
        app.contact().create(contact);
        app.goTo().returnToMainPage();
        List<ContactData> after = app.contact().list();
        Assert.assertEquals(after.size(), before.size() + 1);

        Comparator<? super ContactData> byId= (o1, o2) -> Integer.compare(o1.getId(),o2.getId());

        contact.setId(after.stream().max(byId).get().getId());
        before.add(contact);

        before.sort(byId);
        after.sort(byId);
        Assert.assertEquals(before, after);
    }

}
