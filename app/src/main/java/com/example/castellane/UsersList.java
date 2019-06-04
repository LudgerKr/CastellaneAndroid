package com.example.castellane;

public class UsersList {
    private int iduser, ban;
    private String lastname, firstname, email, password, age, phone, right;

    public UsersList(int iduser, String lastname, String firstname, String age, String phone,
                     String email, String right)
    {
        this.iduser = iduser;
        this.lastname = lastname;
        this.firstname = firstname;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.right = right;
    }

    public UsersList(int iduser, String lastname, String firstname, String email, String password,
                     String age, String phone, String right, int ban)
    {
        this.iduser = iduser;
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.password = password;
        this.age = age;
        this.phone = phone;
        this.right = right;
        this.ban = ban;
    }

    public UsersList(String email, String right)
    {
        this.email = email;
        this.right = right;
    }

    public UsersList(String email)
    {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Nom : " + getLastname() + "\n" + "Prénom : " + getFirstname() + "\n" + "Email : " + getEmail() + "\n";
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public int getBan() {
        return ban;
    }

    public void setBan(int ban) {
        this.ban = ban;
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

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }
}
