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

    public void createContact(ContactData contact) {
        initContactCreation();
        fillContactForm(contact,true);
        submitContactCreation();
    }

    public boolean isThereContact() {
        return isElementPresent(By.name("selected[]"));
    }

    public List<ContactData> getContactList() {
        List<ContactData> contacts = new ArrayList<>();
        List<WebElement> elements = wd.findElements(By.name("entry"));
        for (WebElement element: elements){
            int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("id"));

            String lastname = element.findElement(By.xpath(".//td[2]")).getText();
            String firstname = element.findElement(By.xpath(".//td[3]")).getText();

            ContactData contact = new ContactData(id, firstname,lastname, null,null,null,"[none]");
            contacts.add(contact);
        }
        return contacts;
    }
}
