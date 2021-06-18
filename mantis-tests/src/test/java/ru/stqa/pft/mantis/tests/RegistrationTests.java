package ru.stqa.pft.mantis.tests;


import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.model.MailMessage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public class RegistrationTests extends TestBase {

    //@BeforeMethod   //- OFF  for James // ON for integrated mail server
    public void startMailServer(){
        app.mail().start();
    }

    @Test
    public  void  testRegistration() throws IOException, MessagingException {
        long now = System.currentTimeMillis();
        String user = String.format("user%s",now);
        String password = "password";
        // email = String.format("user%s@localhost.localadomain",now); // for local mail server
        String email = String.format("user%s@localhost",now); //for james mail server

        app.james().createUser(user,password);

        app.registration().start(user, email);
        //List<MailMessage>mailMessages = app.mail().waitForMail(2,10000);//for local mail server
        List<MailMessage>mailMessages = app.james().waitForMail(user,password,60000); //for james mail server

        String confirmationLink = findConfirmationLink(mailMessages,email);
        app.registration().finish(confirmationLink, password, user);

       Assert.assertTrue(app.newSession().login(user,password));
    }

    private String findConfirmationLink(List<MailMessage> mailMessages, String email) {
        MailMessage mailMessage = mailMessages.stream().filter((m)->m.to.equals(email)).findFirst().get();
        VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
        return regex.getText(mailMessage.text);
    }

    //@AfterMethod(alwaysRun = true) //- OFF  for James // ON for integrated mail server
    public void stopMailServer(){
        app.mail().stop();
    }

}
