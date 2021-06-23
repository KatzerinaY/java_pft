package ru.stqa.pft.mantis.appmanager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.openqa.selenium.By;

import ru.stqa.pft.mantis.model.User;

import java.util.List;

public class SessionHelper extends HelperBase{
    private final SessionFactory sessionFactory;

    public SessionHelper(ApplicationManager app) {
        super(app);
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();

        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public List<User> dbUsers(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<User> result = session.createQuery( "from User" ).list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public void login(String username, String password) {
        wd.get(app.getProperty("web.baseUrl") + "login_page.php");
        type(By.name("username"),username);
        click(By.cssSelector("input[type='submit']"));
        type(By.name("password"), password);
        click(By.cssSelector("input[type='submit']"));
    }


    public void gotoUserPage() {
        click(By.cssSelector("a[href*= manage_overview_page]"));
        click(By.cssSelector("a[href*= manage_user_page]"));
    }


    public void selectUserById(int id) {
        click(By.cssSelector("a[href*= 'user_id="+id+"']"));
    }

    public void resetPassword(){
        click(By.xpath("//form[@id = 'manage-user-reset-form']//input[@type='submit']"));
    }

    public void updatePassword(String confirmationLink, String password){
        wd.get(confirmationLink);
        type(By.name("password"),password);
        type(By.name("password_confirm"),password);
        click(By.cssSelector("button[type=submit]"));
    }


}