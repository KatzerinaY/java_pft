package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import java.time.Instant;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
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

    public void selectContactById(int id) {
        wd.findElement(By.cssSelector("input[value='"+id+"']")).click();
    }

    public void initContactModification(int id) {
        wd.findElement(By.xpath("//a[contains(@href, 'edit.php?id=" + id + "')]")).click();
    }

    public void submitContactModification() {
        click(By.name("update"));
    }

    public void create(ContactData contact) {
        initContactCreation();
        fillContactForm(contact,true);
        submitContactCreation();
    }

    public void modify(ContactData contact) {
        initContactModification(contact.getId());
        fillContactForm(contact,false);
        submitContactModification();
    }

    public void delete(ContactData contact) {
        selectContactById(contact.getId());
        deleteSelectedContacts();
    }

    public Contacts all() {
        Contacts contacts = new Contacts();
        WebDriverWait wait = new WebDriverWait(wd,10);
        WebElement maintable = wait.until(presenceOfElementLocated(By.id("maintable")));
        List<WebElement> elements = maintable.findElements(By.name("entry"));
        for (WebElement element: elements){
            int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value"));

            String lastname = element.findElement(By.xpath(".//td[2]")).getText();
            String firstname = element.findElement(By.xpath(".//td[3]")).getText();

            contacts.add(new ContactData().withId(id).withFirstname(firstname).withLastname(lastname).withGroup("[none]"));
        }
        return contacts;
    }
}
