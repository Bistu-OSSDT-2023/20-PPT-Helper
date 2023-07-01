package org.example;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@WebServlet(name = "FileUploadServlet", value = "/Upload")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 创建一个 DiskFileItemFactory 实例，用于处理文件项
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // 创建一个 ServletFileUpload 实例，用于解析上传请求
        ServletFileUpload upload = new ServletFileUpload(factory);

        try {
            // 解析上传请求，获取文件项列表
            List<FileItem> items = upload.parseRequest(request);
            for (FileItem item : items) {
                // 判断当前项是否为文件项，且字段名为"file"
                if (!item.isFormField() && item.getFieldName().equals("file")) {
                    // 获取原始文件名
                    String fileName = item.getName();

                    // 在文件名后添加时间戳
                    String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                    String updatedFileName = fileName + "_" + timeStamp;

                    // 指定本地保存文件的路径
                    String uploadDir = "/path/to/save/uploaded/files/";
                    String filePath = uploadDir + updatedFileName;

                    // 创建本地文件并写入文件内容
                    File dest = new File(filePath);
                    item.write(dest);
                }
            }

            // 设置响应状态为200，表示文件上传成功
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            e.printStackTrace();
            // 设置响应状态为500，表示文件上传失败
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 处理 GET 请求，返回错误信息
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println("<html><body>");
        writer.println("<h1> 请规范操作 </h1>");
        writer.println("</body></html>");
    }
}
