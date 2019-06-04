package com.example.castellane;

public class Annonces {

    private int userid, idnew;
    private String title, content;

    public Annonces(int idnew, int userid, String title, String content)
    {
        this.idnew = idnew;
        this.userid = userid;
        this.title = title;
        this.content = content;
    }

    public Annonces(String title, String content)
    {
        this.title = title;
        this.content = content;
    }

    public Annonces(String title)
    {
        this.title = title;
    }

    public Annonces(int idnew, String title, String content)
    {
        this.idnew = idnew;
        this.title = title;
        this.content = content;
    }


    @Override
    public String toString() {
        return "Numero :" +getIdnew()+ "\n" + "Titre : " + getTitle()+ "\n"  +
                "Content : " + getContent()+ "\n";
    }

    public int getIdnew() {
        return idnew;
    }

    public void setIdnew(int idnew) {
        this.idnew = idnew;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
