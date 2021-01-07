package com.zhu.entity;

public class Page {
    // 当前页码
    private int current = 1;
    // 一页上限
    private int limit = 10;
    // 数据总数
    private int rows;
    //查询路径 用于复用分页链接
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if(current >= 1){
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if( limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if( rows >= 0 ) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前起始条目
     * eg：current = 1 表示第一页 SQL中的 offset = 0
     * @return
     */
    public int getOffset() {
        return (current - 1) * limit;
    }

    //获取总的页数
    public int getTotal() {
        if(rows % limit == 0){
            return rows/limit;
        }else{
            return rows/limit + 1;
        }
    }

    /**
     * 获取展示的起始页码 比如说当前在第5页 展示的页码就从3开始
     * 3，4，5，6，7
     * @return
     */
    public int getFrom(){
        int from = current - 2;
        return from < 1 ? 1 : from;
    }

    /**
     * 获取展示的结束页码 从5开始
     * 3，4，5，6，7 to即7
     * @return
     */
    public int getTo(){
        int to = current + 2;
        int total = getTotal();
        return to > total ? total : to;
    }
}
