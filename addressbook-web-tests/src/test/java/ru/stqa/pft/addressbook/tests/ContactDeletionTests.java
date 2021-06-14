package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactDeletionTests extends TestBase{

  @BeforeMethod
  public void ensurePrecondition(){
    if (app.db().contacts().size()==0){
      app.contact().create(new ContactData().withFirstname("Jim").withLastname("Smith")
              .withAddress("New-York, 24 Avenue,35").withPhoneHome("+1777454545")
              .withEmail("jm123smith@m.ru"));
      app.goTo().returnToMainPage();
    }
  }

  @Test
  public void testContactDeletion()  {

    Contacts before = app.db().contacts();
    ContactData deletedContact = before.iterator().next();
    app.contact().delete(deletedContact);
    app.goTo().returnToMainPage();
    assertThat(app.contact().count(),equalTo(before.size()-1));
    Contacts after = app.db().contacts();
    assertThat(after, equalTo(before.without(deletedContact)));
    verifyContactListInUI();
  }

}
