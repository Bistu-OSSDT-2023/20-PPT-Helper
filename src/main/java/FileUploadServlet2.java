import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@WebServlet("/upload2")
@MultipartConfig
public class FileUploadServlet2 extends HttpServlet {

    private static final String UPLOAD_DIRECTORY = "uploaded"; // Directory path

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        String fileName = Path.of(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
        InputStream fileContent = filePart.getInputStream();

        Path uploads = Paths.get(UPLOAD_DIRECTORY);
        if (!Files.exists(uploads)) {
            Files.createDirectories(uploads); // If the directory does not exist, create it
        }
        Path file = uploads.resolve(fileName);

        try (InputStream input = fileContent) {
            Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
        }

        // Request successful
        request.setAttribute("message", "File Uploaded Successfully");
        request.setAttribute("path", file.toString()); // Add the file path to the request attributes
        request.getRequestDispatcher("/result.jsp").forward(request, response);
    }
}