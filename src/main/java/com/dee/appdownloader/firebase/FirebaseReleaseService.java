package com.dee.appdownloader.firebase;


import com.dee.appdownloader.client.FirebaseAuthenticator;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;

public class FirebaseReleaseService {
    private final FirebaseAuthenticator authenticator;

    public FirebaseReleaseService(FirebaseAuthenticator authenticator) {
        this.authenticator = authenticator;
    }

    public JSONObject getAppReleaseDetails(DownloadRequest request) throws Exception {
        String url = String.format("https://firebaseappdistribution.googleapis.com/v1/projects/%s/apps/%s/releases", request.getProjectId(), request.getAppId());

        JSONTokener tokener = new JSONTokener(getJsonFromUrl(url));
        JSONArray releases = new JSONObject(tokener).getJSONArray("releases");

        JSONObject release = findMatch(releases, request.getAppBuild(), request.getAppVersion());
        return release;
    }

    public InputStream downloadFromUrl(String url) throws Exception {
        HttpGet get = new HttpGet(url);
        return HttpClientBuilder.create().build().execute(get).getEntity().getContent();
    }

    public long getContentLength(String url) throws Exception {
        HttpGet get = new HttpGet(url);
        return HttpClientBuilder.create().build().execute(get).getEntity().getContentLength();
    }

    private JSONObject findMatch(JSONArray releases, String build, String version) {
        for (int i = 0; i < releases.length(); i++) {
            JSONObject release = releases.getJSONObject(i);
            boolean match = (build == null || build.equals(release.getString("buildVersion"))) &&
                    (version == null || version.equals(release.getString("displayVersion")));
            if (match) return release;
        }
        return releases.getJSONObject(0);
    }

    private String getJsonFromUrl(String url) throws Exception {
        HttpGet get = new HttpGet(url);
        get.addHeader("Authorization", "Bearer " + authenticator.getAccessToken());
        return new String(HttpClientBuilder.create().build().execute(get).getEntity().getContent().readAllBytes());
    }
}

