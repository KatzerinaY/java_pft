package ru.stqa.pft.github;

import com.jcabi.github.*;
import org.testng.annotations.Test;
import com.google.common.collect.ImmutableMap;

import java.io.IOException;

public class GithubTests {

    @Test
    public void testCommit()throws IOException {
        Github github = new RtGithub("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");
        RepoCommits commits = github.repos().get(new Coordinates.Simple("KatzerinaY", "java_pft")).commits();
        for (RepoCommit commit : commits.iterate(new ImmutableMap.Builder<String, String>().build())) {
            System.out.println(new RepoCommit.Smart(commit).message());
        }
    }

}

