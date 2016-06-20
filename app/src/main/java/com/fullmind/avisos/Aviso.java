package com.fullmind.avisos;

/**
 * Created by jaime on 05/06/2016.
 */
public class Aviso {

    private int mId;
    private String mContent;
    private int mImportant;

    public Aviso(int important, int id, String content) {
        mImportant = important;
        mId = id;
        mContent = content;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public int getImportant() {
        return mImportant;
    }

    public void setImportant(int important) {
        mImportant = important;
    }
}
