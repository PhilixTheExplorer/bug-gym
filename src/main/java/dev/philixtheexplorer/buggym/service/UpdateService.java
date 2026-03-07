package dev.philixtheexplorer.buggym.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Handles release lookup and version comparison for update checks.
 */
public class UpdateService {

    private static final String LATEST_RELEASE_API =
            "https://api.github.com/repos/PhilixTheExplorer/bug-gym/releases/latest";

    private final HttpClient httpClient;

    public UpdateService() {
        this(HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build());
    }

    public UpdateService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String fetchLatestVersion() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LATEST_RELEASE_API))
                .header("Accept", "application/vnd.github+json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            return null;
        }

        String body = response.body();
        int idx = body.indexOf("\"tag_name\":");
        if (idx == -1) {
            return null;
        }

        int start = body.indexOf('"', idx + 11) + 1;
        int end = body.indexOf('"', start);
        if (start <= 0 || end <= start) {
            return null;
        }

        return body.substring(start, end).replaceFirst("^v", "");
    }

    public int compareVersions(String v1, String v2) {
        String[] a = v1.split("\\.");
        String[] b = v2.split("\\.");

        for (int i = 0; i < Math.max(a.length, b.length); i++) {
            int n1 = parseVersionPart(a, i);
            int n2 = parseVersionPart(b, i);

            if (n1 != n2) {
                return Integer.compare(n1, n2);
            }
        }

        return 0;
    }

    private int parseVersionPart(String[] parts, int idx) {
        if (idx >= parts.length) {
            return 0;
        }

        try {
            return Integer.parseInt(parts[idx].replaceAll("\\D.*", ""));
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }
}
