import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GLM {
    public GLM() {
    }

    static String file_path="";

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public GLM(String file_path) {
        this.file_path = file_path;
    }

    public static HttpRequest get_Request(String mode, String input_content, String PPT_path) throws IOException {
        // 创建一个JSON字符串，作为请求体
        String jsonRequestBody=get_jsonRequestBody(input_content,PPTReader.readPPTFile(PPT_path));
        // 创建一个HttpRequest对象
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://open.bigmodel.cn/api/paas/v3/model-api/chatglm_std/"+mode)) // 将这里替换为你的API URL
                .header("Authorization", "eyJhbGciOiJIUzI1NiIsInNpZ25fdHlwZSI6IlNJR04iLCJ0eXAiOiJKV1QifQ.eyJhcGlfa2V5IjoiZTE5MTY4ZTczYWMzYTQ5OWQyYjRiYWNlMzA4YTcxNDciLCJleHAiOjE2ODgzNjg3NjU2OTYsInRpbWVzdGFtcCI6MTY4ODM2ODc2NDY5Nn0.xWawoI1b8LrGFyPKH_7dSzHNLGS7LWSshGOoHXMJ8vI") // 替换为你的Authorization String
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(jsonRequestBody)) // 使用JSON字符串创建请求体
                .build();
        return request;
    }
//    public static String get_jsonRequestBody(String input_content,boolean sys_prompt){
    public static String get_jsonRequestBody(String input_content,String File_content){

        String Prompt_start = "下面是一个PPT文档的内容，请你根据内容来回答我的提问，若没有对应内容请直接告诉我而不要编造，请用中文回答。【PPT内容开始】";
        String Prompt_end = "【PPT内容结束】下面是我的问题：";
        // 创建一个ObjectMapper对象，用于处理JSON
        ObjectMapper objectMapper = new ObjectMapper();

        // 创建一个HashMap，用于存储"prompt"对象的数据
        Map<String, Object> prompt = new HashMap<>();
        prompt.put("role", "user");
        if (!Objects.equals(File_content, "")) {
            prompt.put("content", Prompt_start + File_content + Prompt_end + input_content);
        } else {
            prompt.put("content", input_content);
        }
        System.out.println(prompt);

        // 创建一个HashMap，用于存储请求体的数据
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("prompt", prompt);
        requestBody.put("temperature", 0.1);

        String jsonRequestBody;
        try {
            // 使用ObjectMapper将Map转换成JSON字符串
            jsonRequestBody = objectMapper.writeValueAsString(requestBody);
            return jsonRequestBody;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert map to JSON", e);
        }
    }
    public static void main(String[] args) throws IOException {

        // 创建一个HttpClient对象
        HttpClient client = HttpClient.newHttpClient();
        // 创建一个HttpRequest对象
        HttpRequest request = get_Request("invoke","你好,第二页讲了什么","/Users/sunday/Downloads/my12-3.pptx");

        try {
            // 使用HttpClient发送请求，并获取HttpResponse对象
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            // 打印HTTP状态码和响应体
            System.out.println(response.statusCode());
            System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            // 打印异常信息
            e.printStackTrace();
        }
    }
}
