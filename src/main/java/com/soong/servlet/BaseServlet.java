package com.soong.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 通用 Servlet，作为所有 Servlet 的父类，提供通用的操作，防止 Servlet 暴增。
 * 使用该 Servlet 子类时，调用者需提供一个名为 method 的参数，以确定需要调用
 * 的方法。
 *
 * 另外, 该 Servlet 子类的方法需返回一个字符串类型的结果，以完成转发或重定向
 * 操作。
 */
public abstract class BaseServlet extends HttpServlet {
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String methodName = request.getParameter("method");
        // 规定用户必须传递名为 method 的参数
        if (methodName == null || methodName.trim().isEmpty()) {
            throw new RuntimeException("你没有传递 method 参数");
        }
        // 得到 Class 对象，以完成反射调用方法
        Class cl = this.getClass();
        Method mt;
        try {
            // 根据 method 参数确定调用方法
            mt = cl.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("您所调用的方法：" + methodName + " 不存在");
        }
        try {
            // 反射调用方法，完成之后得到返回的字符串
            String to = (String) mt.invoke(this, request, response);

            // 当字符串为空时，什么也不做
            if (to == null || to.trim().isEmpty()) return;

            /*
             * 调用都需要转发或重定向：这里的规则是字符串由前缀、冒号、后缀组成，前缀后冒号可以没有。
             * 如果没有前缀和冒号，执行转发操作
             * 如果前缀为 f 或 F，执行转发
             * 如果前缀为 r 或 R, 执行重定向
             * 其它前缀为异常情况
             */
            if (to.contains(":")) {
                String[] strs = to.split(":");
                switch (strs[0].toLowerCase()) {
                    case "f": request.getRequestDispatcher(strs[1]).forward(request, response);
                              break;
                    case "r": response.sendRedirect(request.getContextPath()+strs[1]);
                              break;
                    default: throw new RuntimeException("您所定义的操作：" + strs[0] + " 目前版本尚不支持" );
                }
            } else {
                request.getRequestDispatcher(to).forward(request, response);
            }
        } catch (Exception e) {
            throw new RuntimeException("您所调用方法的内部抛出了异常" + e);
        }
    }
}
