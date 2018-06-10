package com.soong.down;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownUtils {
    public static String filenameEncoding(String filename, HttpServletRequest request) throws IOException {
        boolean isMSIE = HttpUtils.isMSBrowser(request);
        if (isMSIE) {
        //IE浏览器的乱码问题解决
            filename = URLEncoder.encode(filename, "UTF-8");
        } else {
        //万能乱码问题解决
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        }
        return filename;
    }

    /**
     * 下载文件的方法
     * @param file，需要下载的文件路径
     * @param servletContext，调用者需要传入一个 ServletContext 上下文对象
     * @param request，请求对象
     * @param response，响应对象
     */
    public static void downLoad(String file, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. 根据文件路径得到文件名，传入的文件是带盘符或路径的，这里使用的文件名需要对其做分割
        int index = file.lastIndexOf("/");
        String filename = file.substring(index + 1);

        // 2. 确定文件的 MIME 类型
        String contentType = servletContext.getMimeType(file);

        // 3. 确定 ContentDisposition
        String contentDisposition = "attachment;filename=" + filenameEncoding(filename, request);

        // 4. 准备输入流
        FileInputStream in = new FileInputStream(file);

        // 5. 准备输出流
        ServletOutputStream out = response.getOutputStream();

        // 6. 设置响应头
        response.setHeader("Content-Type", contentType);
        response.setHeader("Content-Disposition", contentDisposition);

        // 7. 复制流
        IOUtils.copy(in, out);

        // 8. 关流，输出流 Tomcat 会关
        in.close();
    }
}
