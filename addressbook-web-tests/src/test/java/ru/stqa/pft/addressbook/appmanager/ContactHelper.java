package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class ContactHelper extends HelperBase {

    public ContactHelper(WebDriver wd) {
        super(wd);
    }

    public void fillContactForm(ContactData contactData, boolean creation) {
        type(By.name("firstname"), contactData.getFirstname());
        type(By.name("lastname"), contactData.getLastname());
        type(By.name("address"), contactData.getAddress());
        type(By.name("home"), contactData.getPhoneHome());
        type(By.name("email"), contactData.getEmail());
        if (creation){
            selectedIfPresent(By.name("new_group"), contactData.getGroup());
        } else {
            Assert.assertFalse(isElementPresent(By.name("new_group")));
        }

    }

    public void submitContactCreation() {
        click(By.name("submit"));
    }

    public void initContactCreation() {
        click(By.linkText("add new"));
    }

    public void deleteSelectedContacts() {

        wd.findElement(By.xpath("//input[@value='Delete']")).click();
        assertTrue(closeAlertWithAcceptOrDismiss(true));
    }

    public void selectContact(int index) {
        List<WebElement> l = wd.findElements(By.name("selected[]"));
        l.get(index).click();
    }

    public void initContactModification(int index) {
        List<WebElement> l = wd.findElements(By.cssSelector("[alt=Edit]"));
        l.get(index).click();
    }

    public void submitContactModification() {
        click(By.name("update"));
    }

    public void create(ContactData contact) {
        initContactCreation();
        fillContactForm(contact,true);
        submitContactCreation();
    }

    public void modify(int index, ContactData contact) {
        initContactModification(index);
        fillContactForm(contact,false);
        submitContactModification();
    }

    public void delete(int index) {
        selectContact(index);
        deleteSelectedContacts();
    }

    public List<ContactData> list() {
        List<ContactData> contacts = new ArrayList<>();
        List<WebElement> elements = wd.findElement(By.id("maintable")).findElements(By.name("entry"));
        for (WebElement element: elements){
            int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value"));

            String lastname = element.findElement(By.xpath(".//td[2]")).getText();
            String firstname = element.findElement(By.xpath(".//td[3]")).getText();

            ContactData contact = new ContactData().withId(id).withFirstname(firstname).withLastname(lastname).withGroup("[none]");
            contacts.add(contact);
        }
        return contacts;
    }
}
