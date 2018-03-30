package com.example.htmltest.db;

import org.litepal.crud.DataSupport;

// 省           LitePal 类都要 继承 DataSupport 类的
public class Books extends DataSupport {
    private int id;
    private String bookName; // 书名
    private int bookCode;    // 代号
    private int firstI;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFirstI() {
        return firstI;
    }

    public void setFirstI(int firstI) {
        this.firstI = firstI;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookName() {
        return bookName;
    }

    public int getBookCode() {
        return bookCode;
    }

    public void setBookCode(int bookCode) {
        this.bookCode = bookCode;
    }
}









































