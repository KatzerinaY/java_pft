package ru.stqa.pft.addressbook.tests;

import org.openqa.selenium.remote.BrowserType;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.stqa.pft.addressbook.appmanager.ApplicationManager;

import java.util.Properties;

public class TestBase {

    protected static final ApplicationManager app = new ApplicationManager(System.getProperty("browser",BrowserType.FIREFOX));
    Properties test = System.getProperties();
//    protected static final ApplicationManager app = new ApplicationManager("firefox");

    @BeforeSuite
    public void setUp()
    {
        app.init();
    }

    @AfterSuite
    public void tearDown() {
        app.stop();
    }


}
