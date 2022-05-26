package com.example.petsplace.auxiliary;

public class Upload {
    private String text;
    private String author;
    private String nName;
    private String mImageUrl;

    public Upload(){
        //Empty Constructor
    }

    public Upload(String text,String author,String nName, String mImageUrl){
        if (nName.trim().equals("")){
            this.nName = "No name";
        }
        else{
            this.nName = nName;
        }
        if (text.trim().equals("")){
            this.text = "Текст отсутствует";
        }
        else{
            this.text = text;
        }
        this.author = author;
        this.mImageUrl = mImageUrl;
    }

    public String getnName() {
        return nName;
    }

    public void setnName(String nName) {
        this.nName = nName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
