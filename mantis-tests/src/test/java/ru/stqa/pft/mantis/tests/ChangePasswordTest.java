package ru.stqa.pft.mantis.tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ru.stqa.pft.mantis.model.MailMessage;
import ru.stqa.pft.mantis.model.User;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class ChangePasswordTest extends TestBase{
    @BeforeMethod
    public void startMailServer(){
        app.mail().start();
    }

    @Test
    public  void  testChangePassword() throws IOException, MessagingException {

        String password = "new password";

        User user = UserToChange();

        app.helper().login(app.getProperty("web.adminLogin"), app.getProperty("web.adminPassword"));
        app.helper().gotoUserPage();
        app.helper().selectUserById(user.getId());
        app.helper().resetPassword();

        List<MailMessage>mailMessages = app.mail().waitForMail(1,10000);//for local mail server
        String confirmationLink = findConfirmationLink(mailMessages,user.getEmail());
        app.helper().updatePassword(confirmationLink, password);
        Assert.assertTrue(app.newSession().login(user.getUsername(), password));
    }


    @AfterMethod(alwaysRun = true)
    public void stopMailServer(){
        app.mail().stop();
    }

    private User UserToChange() throws IOException, MessagingException {
        User user = null;

        Iterator<User> it = app.helper().dbUsers().iterator();
        while (user == null && it.hasNext()){
            User item = it.next();
            if ((!item.getUsername().equals("administrator"))
                    &&(item.getEmail().contains(".localadomain"))){
                user = item;
            }
        }

        if (user != null) {
            return user;
        }else{
            return Registration();
        }

    }


    private User Registration() throws IOException, MessagingException {
        long now = System.currentTimeMillis();
        String password = "password";
        User user = new User().withUsername(String.format("user%s",now)).withEmail(String.format("user%s@localhost.localadomain",now));

        app.james().createUser(user.getUsername(),password);
        app.registration().start(user.getUsername(), user.getEmail());
        List<MailMessage>mailMessages = app.mail().waitForMail(2,10000);//for local mail server
        String confirmationLink = findConfirmationLink(mailMessages,user.getEmail());
        app.registration().finish(confirmationLink, password, user.getUsername());

        return user.withId(app.helper().dbUsers().stream().mapToInt((o) -> o.getId()).max().getAsInt());
    }
}
