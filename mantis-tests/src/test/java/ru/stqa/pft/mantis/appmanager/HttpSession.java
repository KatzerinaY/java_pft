package ru.stqa.pft.mantis.appmanager;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpSession {
    private CloseableHttpClient httpClient;
    private ApplicationManager app;

    public HttpSession(ApplicationManager app) {
        this.app = app;
        httpClient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build(); //Strategy перенаправлений
    }

    public boolean login(String username, String password) throws IOException {
        HttpPost post = new HttpPost(app.getProperty("web.baseUrl") + "/login.php"); //cоздает пуcтой запроc
        List<NameValuePair> params = new ArrayList<NameValuePair>(); //формирование параметров
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("secure_session", "on"));
        params.add(new BasicNameValuePair("return", "index.php"));
        post.setEntity(new UrlEncodedFormEntity(params)); //упаковка параметров
        CloseableHttpResponse response = httpClient.execute(post);//отправка запроcа
        String body = geTextFrom(response);// получаем ответ Text cтраницы
        return body.contains(String.format("<span class=\"user-info\">%s</span>", username));
    }

    private String geTextFrom(CloseableHttpResponse response) throws IOException {
        try {
            return EntityUtils.toString(response.getEntity());
        } finally {
            response.close();
        }
    }

    public boolean isLoggedInAs(String username) throws IOException {
        HttpGet get = new HttpGet(app.getProperty("web.baseUrl") + "/index.php");
        CloseableHttpResponse response = httpClient.execute(get);
        String body = geTextFrom(response);
        return body.contains(String.format("<span class=\"user-info\">%s</span>", username));
    }

}