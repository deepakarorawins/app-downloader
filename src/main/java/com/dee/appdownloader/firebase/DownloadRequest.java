package com.dee.appdownloader.firebase;

import lombok.Getter;

@Getter
public class DownloadRequest {
    private final String projectId;
    private final String appId;
    private final String appVersion;
    private final String appBuild;

    public DownloadRequest(String projectId, String appId, String appVersion, String appBuild) {
        this.projectId = projectId;
        this.appId = appId;
        this.appVersion = appVersion;
        this.appBuild = appBuild;
    }
}
