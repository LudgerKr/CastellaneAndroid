package com.example.castellane;

public class Shops {
    private int idshop, price;
    private String title, content, content2, content3, content4, content5;

    public Shops(int idshop, String title, int price, String content, String content4, String content5, String content2,
                 String content3)
    {
        this.idshop = idshop;
        this.title = title;
        this.price = price;
        this.content = content;
        this.content4 = content4;
        this.content5 = content5;
        this.content2 = content2;
        this.content3 = content3;
    }

    public int getIdshop() {
        return idshop;
    }

    public void setIdshop(int idshop) {
        this.idshop = idshop;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public String getContent3() {
        return content3;
    }

    public void setContent3(String content3) {
        this.content3 = content3;
    }

    public String getContent4() {
        return content4;
    }

    public void setContent4(String content4) {
        this.content4 = content4;
    }

    public String getContent5() {
        return content5;
    }

    public void setContent5(String content5) {
        this.content5 = content5;
    }

    @Override
    public String toString() {
        return getTitle()+ "\n" +getPrice() + "\n " + getContent()+ "\n" +getContent2() + "\n " +
                getContent3()+ "\n" +getContent4() + "\n " +getContent5();
    }
}
