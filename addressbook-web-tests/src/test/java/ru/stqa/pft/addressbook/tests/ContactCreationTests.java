package ru.stqa.pft.addressbook.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.Groups;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactCreationTests extends TestBase {

    @DataProvider
    public Iterator<Object[]> validContactsFromJSON() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/contacts.json")));
        String json = "";
        String line = reader.readLine();
        while (line != null) {
            json += line;
            line = reader.readLine();
        }
        Gson gson = new Gson();
        List<ContactData> contacts = gson.fromJson(json, new TypeToken<List<ContactData>>(){}.getType());  //= List<GroupData>.class

        return contacts.stream().map((g) -> new Object[]{g}).collect(Collectors.toList()).iterator();

    }

    @Test(dataProvider = "validContactsFromJSON")
    public void testContactCreation(ContactData contact) {
        Groups groups = app.db().groups();

        Contacts before = app.db().contacts();
        app.contact().create(contact.withPhoto(new File("src/test/resources/kater.jpg")).inGroup(groups.iterator().next()));
        app.goTo().returnToMainPage();
        assertThat(app.contact().count(), equalTo(before.size() + 1));
        Contacts after = app.db().contacts();

//        Comparator<? super ContactData> byId= (o1, o2) -> Integer.compare(o1.getId(),o2.getId());
//        contact.setId(after.stream().max(byId).get().getId());

        contact.withId(after.stream().mapToInt((o)->o.getId()).max().getAsInt());
        assertThat(after, equalTo(before.withAdded(contact)));
        verifyContactListInUI();
    }

}
