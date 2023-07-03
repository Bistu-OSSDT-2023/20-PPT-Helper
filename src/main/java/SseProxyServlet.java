import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.net.http.HttpClient.*;
import java.util.concurrent.*;

@WebServlet(urlPatterns={"/sse"}, asyncSupported=true)
public class SseProxyServlet extends HttpServlet {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/event-stream");
        resp.setCharacterEncoding("UTF-8");

        // Start async
        final AsyncContext asyncContext = req.startAsync();

        // Connect to the remote server
        HttpRequest request = GLM.get_Request("sse-invoke");

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                .thenAccept(response -> {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            // Write each line to the servlet output stream
                            ServletOutputStream outputStream = asyncContext.getResponse().getOutputStream();
                            outputStream.write((line + "\n").getBytes());
                            outputStream.flush();
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
