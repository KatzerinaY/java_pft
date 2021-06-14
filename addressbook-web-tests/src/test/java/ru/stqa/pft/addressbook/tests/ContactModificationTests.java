package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactModificationTests extends TestBase{

    @BeforeMethod
    public void ensurePrecondition(){
        if (app.db().contacts().size()==0){
            app.contact().create(new ContactData().withFirstname("Jim").withLastname("Smith")
                    .withAddress("New-York, 24 Avenue,35").withPhoneHome("+1777454545")
                    .withEmail("jm123smith@m.ru").withGroup("test1"));
            app.goTo().returnToMainPage();
        }
    }

    @Test
    public void testContactModification()  {

        Contacts before = app.db().contacts();
        ContactData modifiedContact = before.iterator().next();
        ContactData contact = new ContactData().withId(modifiedContact.getId())
                .withFirstname("Jim").withLastname("Smith").withAddress("New-York, 24 Avenue,35")
                .withPhoneHome("+1777454545").withEmail("jm123smith@m.ru");
        app.contact().modify(contact);
        app.goTo().returnToMainPage();
        assertThat(app.contact().count(), equalTo(before.size()));
        Contacts after = app.db().contacts();
        assertThat(after,equalTo(before.without(modifiedContact).withAdded(contact)));

//        Comparator<? super ContactData> byId= (o1, o2) -> Integer.compare(o1.getId(),o2.getId());
//        before.sort(byId);
//        after.sort(byId);
//        Assert.assertEquals(before,after);
        verifyContactListInUI();
    }

}
