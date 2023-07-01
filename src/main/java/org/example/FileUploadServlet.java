package org.example; /**
 * Created by sunday on 2023/7/1 3:45 PM.
 * Author: sunday
 * Description:
 */

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;

@WebServlet(name = "FileUploadServlet", value = "/Upload")
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("file");
        String fileName = getFileName(filePart);

        // 指定本地保存文件的路径
        String uploadDir = "./files/";
        String filePath = uploadDir + fileName;

        // 创建本地文件
        File dest = new File(filePath);
        try (InputStream fileContent = filePart.getInputStream();
             FileOutputStream fos = new FileOutputStream(dest)) {
            int read;
            byte[] buffer = new byte[1024];
            while ((read = fileContent.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] elements = contentDisposition.split(";");
        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                return element.substring(element.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
