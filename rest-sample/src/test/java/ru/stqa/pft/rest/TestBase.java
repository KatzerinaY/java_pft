package ru.stqa.pft.rest;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jayway.restassured.RestAssured;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;


public class TestBase {

    protected static final ApplicationManager app = new ApplicationManager();

    @BeforeClass
    public void setUp() throws IOException {
        app.init();
        RestAssured.authentication = RestAssured.basic(app.getProperty("rest.login"), "");
        skipIfNotFixed(1192);
        //skipIfNotFixed(11920);
    }


    private void skipIfNotFixed(int issueId) {
        if (isIssueOpen(issueId)) {
            throw new SkipException("Ignored because of issue " + issueId);
        }
    }

//    boolean isIssueOpen(int issueId)  {
//        Set<Issue> Issues = getIssues();
//        Iterator<Issue> It = Issues.stream().filter((i) -> i.getId() == issueId).iterator();
//        if (It.hasNext()) {
//            Issue issue = It.next();
//            if (issue.getState_name().equals("Closed")) {
//                return false;
//            } else {
//                return true;
//            }
//        }
//        return false;
//    }

    boolean isIssueOpen(int issueId)  {
        Issue issue = getIssueById(issueId);
            if ((issue != null) && (!issue.getState_name().equals("Closed"))) {
                return true;
            } else {
                return false;
            }
    }

    protected Issue getIssueById(int issueId){

        String json = RestAssured.get(app.getProperty("bugify.path")+String.format("issues/%s.json",issueId)).asString();
        JsonElement parsed = new JsonParser().parse(json);
        JsonElement issues = parsed.getAsJsonObject().get("issues");

        if (issues == null){
            return null;
        }
        Set<Issue> issuesSet= new Gson().fromJson(issues, new TypeToken<Set<Issue>>() {}.getType());
        return issuesSet.iterator().next();
    }


    protected Set<Issue> getIssues()  {
        String json = RestAssured.get(app.getProperty("bugify.path")+"issues.json").asString();
        JsonElement parsed = new JsonParser().parse(json);
        JsonElement issues = parsed.getAsJsonObject().get("issues");
        return new Gson().fromJson(issues, new TypeToken<Set<Issue>>() {
        }.getType());
    }

    protected int createIssue(Issue newIssue) {
        String json = RestAssured.given()
                .parameter("subject", newIssue.getSubject())
                .parameter("description", newIssue.getDescription())
                .post(app.getProperty("bugify.path")+"issues.json").asString();
        JsonElement parsed = new JsonParser().parse(json);
        return parsed.getAsJsonObject().get("issue_id").getAsInt();
    }

}
