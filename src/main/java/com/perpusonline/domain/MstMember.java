package com.perpusonline.domain;

import jakarta.persistence.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name = "MST_MEMBER")
public class MstMember {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "idMember")
    private String idMember;

    @Column(name = "userName")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "isLogin")
    private String isLogin;

    @Column(name = "isBorrowed")
    private boolean isBorrowed;

    @Column(name = "isAdmin")
    private boolean isAdmin;

    public MstMember(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public MstMember(String userName, String email, String password, String idMember, boolean isAdmin) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.idMember = idMember;
        this.isAdmin = isAdmin;
    }

    public MstMember() {
    }

    public boolean isValidEmail() {
        String regex = ".*@(gmail\\.com|hotmail\\.com|outlook\\.com||yahoo\\.com)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(getEmail());
        return matcher.matches();
    }

    public boolean isValidPassword() {
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(getPassword());
        return matcher.matches();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdMember() {
        return idMember;
    }

    public void setIdMember(String idMember) {
        this.idMember = idMember;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
