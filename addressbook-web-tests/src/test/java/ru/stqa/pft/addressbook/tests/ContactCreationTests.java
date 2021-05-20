package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactCreationTests extends TestBase{

  @Test
  public void testContactCreation()  {
    app.getContactHelper().createContact(new ContactData("Jim", "Smith",
            "New-York, 24 Avenue,35", "+10777454545", "jm123smith@m.ru",
            "test1"));
    app.getNavigationHelper().returnToMainPage();
   }

}
