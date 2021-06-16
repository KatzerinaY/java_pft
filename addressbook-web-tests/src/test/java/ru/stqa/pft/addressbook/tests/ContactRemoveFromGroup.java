package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactRemoveFromGroup extends TestBase {
    private GroupData group= null;
    private ContactData contact = null;

    @BeforeMethod
    public void ensurePreconditions() {

        if (app.db().groups().size() == 0) {
            app.goTo().groupPage();
            group = new GroupData().withName("test1");
            app.group().create(group);
            app.goTo().returnToMainPage();
        }

        Iterator<ContactData> it = app.db().contacts().iterator();
        while (contact == null && it.hasNext()){
            ContactData item = it.next();
            if (item.getGroups().size() > 0){
                contact = item;
                group = contact.getGroups().iterator().next();
            }
        }

        if (contact == null ) {
            group = app.db().groups().iterator().next();
            if (app.db().contacts().size() == 0) {
                contact = new ContactData().withFirstname("Jim").withLastname("Smith")
                        .withAddress("New-York, 24 Avenue,35").withPhoneHome("+1777454545")
                        .withEmail("jm123smith@m.ru").inGroup(group);
                app.contact().create(contact);
            } else {
                contact = app.db().contacts().iterator().next();
                app.contact().addInGroup(contact, group);
            }
            app.goTo().returnToMainPage();
        }
    }

    @Test
    public void testRemoveContact() {
        Groups before = contact.getGroups();
        app.goTo().returnToMainPage();
        app.contact().DeleteFromGroup(contact, group);
        Groups after = app.db().contacts().stream().filter(c -> c.getId() == contact.getId()).findFirst().get().getGroups();
        assertThat(after, equalTo(before.without(group)));
    }

}