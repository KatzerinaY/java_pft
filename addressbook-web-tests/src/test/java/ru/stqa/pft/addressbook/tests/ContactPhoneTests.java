package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ContactPhoneTests extends TestBase{

    @BeforeMethod
    public void ensurePrecondition(){
        if (app.contact().count()==0){
            app.contact().create(new ContactData().withFirstname("Jim").withLastname("Smith")
                    .withAddress("New-York, 24 Avenue,35").withPhoneHome("+1777454545")
                    .withEmail("jm123smith@m.ru").withGroup("test1"));
            app.goTo().returnToMainPage();
        }
    }

    @Test
    public void testContactPhones(){
        app.goTo().returnToMainPage();
        ContactData contact = app.contact().all().iterator().next();
        ContactData contactInfoFromEditForm = app.contact().InfoFromEditForm(contact);
        assertThat(contact.getAllPhones(),equalTo(mergePhones(contactInfoFromEditForm)));
        assertThat(contact.getAllEmails(),equalTo(mergeEmails(contactInfoFromEditForm)));
        assertThat(contact.getAddress(),equalTo(contactInfoFromEditForm.getAddress()));
    }

    private String mergeEmails(ContactData contact) {
        String sp = Arrays.asList(contact.getEmail(), contact.getEmail2(), contact.getEmail3())
                .stream().filter((s) -> !s.equals(""))
                .collect(Collectors.joining("\n"));
        return sp;
    }

    private String mergePhones(ContactData contact) {
        String sp = Arrays.asList(contact.getPhoneHome(), contact.getPhoneMob(), contact.getPhoneWork())
                .stream().filter((s) -> !s.equals(""))
                .map(ContactPhoneTests::cleaned)
                .collect(Collectors.joining("\n"));
        return sp;
    }

    public static String cleaned(String phone){
        return phone.replaceAll("\\s","").replaceAll("[-()]","");
        //"\\s" probelnye simvoly
        //"[-()]" vse, chto v skobkah, vkluchaja -
        //"[(-)]" vse, chto mezdu skobkami
        //"[a-z]" vse, ot a do z
    }
}
