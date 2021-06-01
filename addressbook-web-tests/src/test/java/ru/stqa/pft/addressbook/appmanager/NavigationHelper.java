package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

public class NavigationHelper extends HelperBase {

    public NavigationHelper(WebDriver wd) {
        super(wd);
    }

    public void groupPage() {
        wd.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        if (isElementPresent(By.tagName("h1"))
            && wd.findElement(By.tagName("h1")).getText().equals("Groups")
            && isElementPresent(By.name("new"))) {
            wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        } else {
            wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            click(By.linkText("groups"));
        }

    }

    public void returnToMainPage() {
        wd.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        if (isElementPresent(By.id("maintable"))) {
            wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        } else {
            wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            wd.findElement(By.id("logo")).click();
        }
    }

}
