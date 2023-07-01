import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class Dify {

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        // 构建请求参数
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("inputs", new HashMap<>());
        parameters.put("query", "你好");
        parameters.put("response_mode", "blocking");
//        parameters.put("conversation_id", "1c7e55fb-1ba2-4e10-81b5-30addcea2276");
        parameters.put("user", "Sunny");

        // 使用Jackson库将Map转化为JSON字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(parameters);
//        String apiKey = System.getenv("DIFY_SECRET-KEY"); // 获取环境变量
        String apiKey = "app-brxdiHvnrsTyxIGjoPgV5XYn";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.dify.ai/v1/chat-messages"))
                .header("Authorization", "Bearer "+apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
    }
}
