package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.List;

public class ContactDeletionTests extends TestBase{

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
  public void testContactDeletion()  {

    List<ContactData> before = app.contact().list();
    int index = before.size()-1;

    app.contact().delete(index);
    app.goTo().returnToMainPage();

    List<ContactData> after = app.contact().list();
    Assert.assertEquals(after.size(),before.size()-1);

    before.remove(index);
    Assert.assertEquals(before, after);
  }

}
