package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("prompt", "Translate the following English text to French: '{}'");
        parameters.put("max_tokens", "60");
        String apiKey = System.getenv("OPENAI_API_KEY"); // 获取环境变量
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/engines/davinci-codex/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(parameters.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
    }
}
