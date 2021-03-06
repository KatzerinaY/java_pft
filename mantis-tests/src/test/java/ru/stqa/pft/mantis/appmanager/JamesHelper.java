package ru.stqa.pft.mantis.appmanager;

import org.apache.commons.net.telnet.TelnetClient;
import ru.stqa.pft.mantis.model.MailMessage;

import javax.mail.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JamesHelper {
    private ApplicationManager app;
    private TelnetClient telnet;
    private InputStream in;
    private PrintStream out;

    private Session mailSession;
    private Store store;
    private String mailserver;


    public JamesHelper(ApplicationManager app) {
        this.app = app;
        telnet = new TelnetClient();
        mailSession = Session.getDefaultInstance(System.getProperties());   // create mailSession
    }

    public boolean doesUserExist(String name) {
        initTelnetSession();
        write("verify " + name);
        String result = readUntil("exist");
        closeTelnetSession();
        return result.trim().equals("User " + name + " exist");
    }

    public void createUser(String name, String passwd) {
        initTelnetSession();    // connect with protocol Telnet
        write("adduser " + name + " " + passwd);    // instruction
        String result = readUntil("User " + name + " added");    // wait for this text on console
        closeTelnetSession();    // disconnect
    }

    public void deleteUser(String name) {
        initTelnetSession();
        write("deluser " + name);
        String result = readUntil("User " + name + " deleted");
        closeTelnetSession();
    }

    private void initTelnetSession() {
        mailserver = app.getProperty("mailserver.host");
        int port = Integer.parseInt(app.getProperty("mailserver.port"));
        String login = app.getProperty("mailserver.adminlogin");
        String password = app.getProperty("mailserver.adminpassword");

        try {
            telnet.connect(mailserver, port);    // connect with mail server
            in = telnet.getInputStream();    // in-stream: for reading from telnet
            out = new PrintStream( telnet.getOutputStream() );    // out-stream: for writing/send instruction to telnet

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        readUntil("Login id:");
        write("");
        readUntil("Password:");
        write("");

        readUntil("Login id:");
        write(login);
        readUntil("Password:");
        write(password);

        readUntil("Welcome " + login + ". HELP for a list of commands");
    }

    private String readUntil(String pattern) {
        try {
            char lastChar = pattern.charAt(pattern.length() - 1);
            StringBuffer sb = new StringBuffer();
            char ch = (char) in.read();    // read from in-stream (on console) and compare with template
            while (true) {
                System.out.print(ch);
                sb.append(ch);
                if (ch == lastChar) {
                    if (sb.toString().endsWith(pattern)) {
                        return sb.toString();
                    }
                }
                ch = (char) in.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void write(String value) {
        try {
            out.println(value);
            out.flush();
            System.out.println(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeTelnetSession() {
        write("quit");
    }

    public void drainEmail(String username, String password) throws MessagingException { // clear mail for username
        Folder inbox = openInbox(username, password);
        for (Message message : inbox.getMessages()) {
            message.setFlag(Flags.Flag.DELETED, true);  // each message is marked by DELETED
        }
        closeFolder(inbox);
    }

    private void closeFolder(Folder folder) throws MessagingException {
        folder.close(true);  // delete all marked messages
        store.close();    // disconnect
    }

    private Folder openInbox(String username, String password) throws MessagingException {  // open mail
        store = mailSession.getStore("pop3");  //inform our mailSession: to use POP3 to access the mail
        store.connect(mailserver, username, password);  // connect with POP3
        Folder folder = store.getDefaultFolder().getFolder("INBOX");  // access to the INBOX folder (only!): standard name POP3-folder
        folder.open(Folder.READ_WRITE);
        return folder;
    }

    public List<MailMessage> waitForMail(String username, String password, long timeout) throws MessagingException {
        long now = System.currentTimeMillis();
        while (System.currentTimeMillis() < now + timeout) { //time
            List<MailMessage> allMail = getAllMail(username, password);  // try to get mail
            if (allMail.size() > 0) {
                return allMail;  // mail-list
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        throw new Error("No mail :(");
    }

    public List<MailMessage> getAllMail(String username, String password) throws MessagingException {
        Folder inbox = openInbox(username, password);
        // mail-list to stream -> map(all mails to MailMessage-item) -> list
        List<MailMessage> messages = Arrays.asList(inbox.getMessages()).stream().map((m) -> toModelMail(m)).collect(Collectors.toList());
        closeFolder(inbox);
        return messages;
    }

    public static MailMessage toModelMail(Message m) {
        try {
            // address-list(0), get letter-content and transform into string, do MailMessage-item
            return new MailMessage(m.getAllRecipients()[0].toString(), (String) m.getContent());
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}