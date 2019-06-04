package com.example.castellane;

public class Users {
    private String iduser;
    private String lastname, firstname, email, password, age, phone, right;

    public Users (String iduser, String lastname, String firstname, String age, String phone, String email,
                  String password)
    {
        this.iduser = iduser;
        this.lastname = lastname;
        this.firstname = firstname;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public Users (String lastname, String firstname,String email, String password)
    {
        this.iduser = "";
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.password = password;
    }

    public Users(String email, String right)
    {
        this.iduser = "";
        this.lastname = "";
        this.firstname = "";
        this.age = "";
        this.phone = "";
        this.email = email;
        this.password = "";
        this.right = right;
    }

    public Users(String iduser, String lastname, String firstname, String age, String phone,
                 String email)
    {
        this.iduser = iduser;
        this.lastname = lastname;
        this.firstname = firstname;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.password = "";
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }
}
