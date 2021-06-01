package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.Comparator;
import java.util.List;

public class ContactModificationTests extends TestBase{

    @BeforeMethod
    public void ensurePrecondition(){
        if (app.contact().list().size()==0){
            app.contact().create(new ContactData().withFirstname("Jim").withLastname("Smith")
                    .withAddress("New-York, 24 Avenue,35").withPhoneHome("+10777454545")
                    .withEmail("jm123smith@m.ru").withGroup("test1"));
            app.goTo().returnToMainPage();
        }
    }

    @Test
    public void testContactModification()  {

        List<ContactData> before = app.contact().list();
        int index = before.size()-1;
        ContactData contact = new ContactData().withId(before.get(index).getId())
                .withFirstname("Jim").withLastname("Smith").withAddress("New-York, 24 Avenue,35")
                .withPhoneHome("+10777454545").withEmail("jm123smith@m.ru");
        app.contact().modify(index, contact);
        app.goTo().returnToMainPage();

        List<ContactData> after = app.contact().list();
        Assert.assertEquals(after.size(), before.size());

        before.remove(index);
        before.add(contact);

        Comparator<? super ContactData> byId= (o1, o2) -> Integer.compare(o1.getId(),o2.getId());
        before.sort(byId);
        after.sort(byId);
        Assert.assertEquals(before,after);

    }

}
