package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class HelperBase {

    protected WebDriver wd;

    public HelperBase(WebDriver wd) {
        this.wd = wd;
    }

    protected void type(By locator, String text) {
        click(locator);
        if (text != null) {
            String existingText =  wd.findElement(locator).getAttribute("value");
            if (! text.equals(existingText)) {
                wd.findElement(locator).clear();
                wd.findElement(locator).sendKeys(text);
            }
        }
    }

    protected void attach(By locator, File file) {
        if (file != null) {
           wd.findElement(locator).sendKeys(file.getAbsolutePath());
        }
    }

    protected void click(By locator) {
        wd.findElement(locator).click();
    }

    protected boolean closeAlertWithAcceptOrDismiss(boolean acceptNextAlert) {
        try {
            Alert alert = wd.switchTo().alert();

            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return true;
        } catch (NoAlertPresentException e){
            return false;
        }
    }

    protected boolean isElementPresent(By locator) {
        wd.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        boolean r;
        try {
            wd.findElement(locator);
            r = true;
        } catch (NoSuchElementException ex){
            r = false;
        }
        wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return r;
    }

    protected void selectedIfPresent(By locator,String visibleText) {
        wd.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try {
            new Select(wd.findElement(locator)).selectByVisibleText(visibleText);
        } catch (Exception ex) {
        }
        wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
}
