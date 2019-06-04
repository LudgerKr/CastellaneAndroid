package com.example.castellane;

public class Seances {
    private String idseance;
    private String date;
    private String time;

    public Seances(String idseance, String date, String time)
    {
        this.idseance = idseance;
        this.date = date;
        this.time = time;
    }

    public Seances(String date, String time)
    {
        this.date = date;
        this.time = time;
    }

    public Seances(String idseance)
    {
        this.idseance = idseance;
    }

    @Override
    public String toString() {
        return "Numéro : " + getIdseance()+ "\n" + "Date : " + getDate() + "\n"
                + "Heure : " + getTime() + "\n";
    }

    public String getIdseance() {
        return idseance;
    }

    public void setIdseance(String idseance) {
        this.idseance = idseance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
