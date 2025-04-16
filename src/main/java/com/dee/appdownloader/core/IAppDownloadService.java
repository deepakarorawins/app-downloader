package com.dee.appdownloader.core;

import com.dee.appdownloader.firebase.DownloadRequest;
import org.json.JSONObject;

import java.io.File;

public interface IAppDownloadService {
    JSONObject getRelease(DownloadRequest request);
    void downloadFile(String url, File outputFile);
}
