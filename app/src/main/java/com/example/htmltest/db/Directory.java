package com.example.htmltest.db;

import org.litepal.crud.DataSupport;

public class Directory extends DataSupport {
    private int id;
    private String directoryName;
    private double directoryId;
    private int directoryCode;
    private int booksId;
    private String directoryContent;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDirectoryContent() {
        return directoryContent;
    }

    public void setDirectoryContent(String directoryContent) {
        this.directoryContent = directoryContent;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public double getDirectoryId() {
        return directoryId;
    }

    public void setDirectoryId(double directoryId) {
        this.directoryId = directoryId;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public int getDirectoryCode() {
        return directoryCode;
    }

    public void setDirectoryCode(int cityCode) {
        this.directoryCode = directoryCode;
    }

    public int getBooksId() {
        return booksId;
    }

    public void setBooksId(int booksId) {
        this.booksId = booksId;
    }
}






























