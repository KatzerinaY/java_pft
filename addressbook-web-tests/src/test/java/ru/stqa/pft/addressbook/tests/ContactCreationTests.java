package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactCreationTests extends TestBase {

    @Test
    public void testContactCreation() {

        Contacts before = app.contact().all();
        ContactData contact = new ContactData().withFirstname("Jim").withLastname("Smith")
                              .withAddress("New-York, 24 Avenue,35").withPhoneHome("+10777454545")
                              .withEmail("jm123smith@m.ru").withGroup("test1");
        app.contact().create(contact);
        app.goTo().returnToMainPage();
        Contacts after = app.contact().all();
        assertThat(after.size(), equalTo(before.size() + 1));

//        Comparator<? super ContactData> byId= (o1, o2) -> Integer.compare(o1.getId(),o2.getId());
//        contact.setId(after.stream().max(byId).get().getId());

        contact.withId(after.stream().mapToInt((o)->o.getId()).max().getAsInt());
        assertThat(after, equalTo(before.withAdded(contact)));
    }

}
