package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactAddToGroup extends TestBase{

    private GroupData group= null;
    private ContactData contact = null;

    @BeforeMethod
    public void ensurePrecondition(){

        if (app.db().groups().size() == 0) {        //create group if empty group-list
            app.goTo().groupPage();
            group = new GroupData().withName("test1");
            app.group().create(group);
            app.goTo().returnToMainPage();
//            if (app.db().contacts().iterator().hasNext()){
//                contact = app.db().contacts().iterator().next(); // if group is just created, select any contact
//            }
        }

        Iterator<ContactData> it = app.db().contacts().iterator();
        while (contact == null && it.hasNext()){
            ContactData item = it.next();
            if (item.getGroups().size() < app.db().groups().size()){
                contact = item;
                group = selectGroup(contact);
            }
        }

        if (contact == null) {  //if no suitable contact or empty contact-list
            contact = new ContactData().withFirstname("Jim").withLastname("Smith")
                    .withAddress("New-York, 24 Avenue,35").withPhoneHome("+1777454545")
                    .withEmail("jm123smith@m.ru");
            app.contact().create(contact);
            app.goTo().returnToMainPage();

            group = app.db().groups().iterator().next(); // if contact is just created, select first group
        }
    }


    @Test
    public void testAddContact()  {
        Groups before = contact.getGroups();
        app.contact().addInGroup(contact,group);
        app.goTo().returnToMainPage();

       // Groups after = app.db().contacts().iterator().next().withId(contact.getId()).getGroups();
        Groups after = app.db().contacts().stream().filter(c -> c.getId() == contact.getId()).findFirst().get().getGroups();
        assertThat(after,equalTo(before.withAdded(group)));
    }

    private GroupData selectGroup(ContactData contact) {
        Collection<GroupData> contactGroups = new HashSet<>(contact.getGroups());
        Collection<GroupData> allGroups = new HashSet<>(app.db().groups());
        allGroups.removeAll(contactGroups);
        return allGroups.iterator().next();
    }



}
