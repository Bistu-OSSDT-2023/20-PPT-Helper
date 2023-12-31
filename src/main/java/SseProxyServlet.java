import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.net.http.HttpClient.*;
import java.util.Objects;
import java.util.concurrent.*;

@WebServlet(urlPatterns={"/sse"}, asyncSupported=true)
public class SseProxyServlet extends HttpServlet {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/event-stream");
        resp.setCharacterEncoding("UTF-8");
        // 获取请求的内容类型
        String contentType = req.getContentType();
        Message message = null;
        // 确保内容类型为JSON
        if (contentType != null && contentType.contains("application/json")) {
            // 使用request.getInputStream()获取请求体中的内容
            // 使用Jackson的ObjectMapper来解析JSON
            ObjectMapper objectMapper = new ObjectMapper();
             message = objectMapper.readValue(req.getInputStream(), Message.class);

            // 现在你可以访问用户输入的消息
            System.out.println("Received message: " + message.getText());
        }
        // Start async
        final AsyncContext asyncContext = req.startAsync();

        // Connect to the remote server
        assert message != null;
//        GLM glm = new GLM(); // 利用静态变量传递参数
        String file_path="";
        for (Cookie cookie : req.getCookies()) {
            if (cookie.getName().equals("filepath")) {
                file_path = cookie.getValue();
            }
        }
        if (Objects.equals(file_path, "")) {
            resp.getWriter().println("Please upload a file first");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        HttpRequest request = GLM.get_Request("sse-invoke",message.getText(),file_path);
        // 我特意没给不传文件直接对话留接口
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                .thenAccept(response -> {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            // Write each line to the servlet output stream
                            ServletOutputStream outputStream = asyncContext.getResponse().getOutputStream(); // 获取响应输出流
                            outputStream.write((line + "\n").getBytes()); // 写入响应输出流
                            outputStream.flush(); // 刷新响应输出流, 将缓冲的数据发送出去
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        asyncContext.complete();
                    }
                });
    }

    @Override
    public void destroy() {
        executor.shutdown();  // Important to prevent resource leaks
    }
}



