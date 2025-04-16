package com.dee.appdownloader.firebase;

import com.dee.appdownloader.client.FirebaseAuthenticator;
import com.dee.appdownloader.core.IAppDownloadService;
import org.json.JSONObject;

import java.io.*;

public class FirebaseAppDownloadService implements IAppDownloadService {
    private final FirebaseAuthenticator authenticator;
    private final FirebaseReleaseService releaseService;

    public FirebaseAppDownloadService() {

        this.authenticator = new FirebaseAuthenticator();
        this.releaseService = new FirebaseReleaseService(authenticator);
    }

    @Override
    public JSONObject getRelease(DownloadRequest request) {
        try {
            return releaseService.getAppReleaseDetails(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void displayProgress(int percentage) {
        int barWidth=30;
        int filled = (percentage * barWidth) / 100;
        String bar = "=".repeat(filled) + " ".repeat(barWidth - filled);

        if (System.getenv("JENKINS_HOME") != null) {
            if (percentage % 10 == 0 || percentage == 100) {
                System.out.println("Download progress: " + percentage + "%");
            }
        } else {
            System.out.printf("\rDownloading: [%s] %d%%", bar, percentage);
            System.out.flush();
            if (percentage == 100) {
                System.out.println(); // Finish with a new line
            }
        }
    }

    public void downloadFile(String url, File outputFile) {
        System.out.println("File Download url: "+ url);
        try (InputStream in = releaseService.downloadFromUrl(url);
             OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile))) {

            byte[] buffer = new byte[8192];
            long totalRead = 0;
            long contentLength = releaseService.getContentLength(url);
            int previous = 0;

            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
                totalRead += read;
                int current = (int) ((totalRead * 100) / contentLength);
                if (current > previous) {
                    displayProgress(current);
                    previous = current;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
