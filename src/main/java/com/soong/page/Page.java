package com.soong.page;

import java.util.List;

/**
 * 分页 Bean，用来进行分页
 * 使用该类时，用户需提供
 *  1. 每页记录数 ps(Page size)
 *  2. 需要显示的页面数量 sp(Show pages)
 * 最终生成 Page 之前，用户还要设置 url，用户可通过在 Servlet 中定义如下方法获取到 URL 字符串，再将其设置成
 * Page 对象的 URL
 *
 *  private String getUrl(HttpServletRequest request) {
 *         String contextPath = request.getContextPath();
 *         String servletPath = request.getServletPath();
 *         String queryString = request.getQueryString();
 *
 *         if (queryString.contains("&pc=")) {
 *             int index = queryString.indexOf("&pc=");
 *             queryString = queryString.substring(0, index);
 *         }
 *         return contextPath + servletPath + "?" + queryString;
 *     }
 */
public class Page<T> {
    private int pc; // 当前页码 page code
    private int tr; // 总记录数 total recodes
    private int ps; // 每页记录数 page size
    private List<T> beanList; // 每一页的内容

    // 确定页码
    private int sp; // 需要显示的页数 show pages; 该属性为业务属性，由用户最终确定

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    // 确定页面开始位置
    public int getBegin() {
        // 当总页数小于等于需要显示的页数时，这时，指定 begin 为 1.
        if (getTp() <= sp) return 1;

        // 页码范围的开始
        int begin = pc - (sp / 2 + sp % 2);
        // 确保每次都显示 sp 个页码，当出现尾溢出时，让页面从 getTp() - (sp - 1) 开始
        int maxBegin = getTp() - (sp - 1);

        // 处理头溢出
        if (begin < 1) return 1;

        // 处理尾溢出
        if (begin > maxBegin) return maxBegin;

        return begin;
    }

    // 确定页码结束位置
    public int getEnd () {
        // 当总页数小于等于需要显示的页数时，这时，指定 end 为 tp.
        if (getTp() <= sp) return getTp();

        // 页码范围的结束
        int end = pc + (sp / 2 + sp % 2) - 1;
        // 确保每次都显示 sp 个页码，当出现尾溢出时，让页面到 getTp() 结束
        int maxEnd = getTp();

        // 处理头溢出
        if (end < sp) return sp;

        // 处理尾溢出
        if (end > maxEnd) return maxEnd;

        return end;
    }

    public int getSp() {
        return sp;
    }

    public void setSp(int sp) {
        this.sp = sp;
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public int getTp() {
        int tp = tr / ps;
        return tr % ps == 0 ? tp : (tp + 1);
    }


    public int getTr() {
        return tr;
    }

    public void setTr(int tr) {
        this.tr = tr;
    }

    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    public List<T> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<T> beanList) {
        this.beanList = beanList;
    }
}
