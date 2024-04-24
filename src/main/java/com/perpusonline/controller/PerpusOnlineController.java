package com.perpusonline.controller;

import com.perpusonline.services.PerpusOnlineServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PerpusOnlineController {
    @Autowired
    private PerpusOnlineServices perpusOnlineServices;

    @RequestMapping("/")
    public String homePage(){
        String homePage = perpusOnlineServices.homePage();
        return homePage;
    }

    @RequestMapping("/registerPage")
    public String registerPage(){
        return "registerPage";
    }

    @RequestMapping("/registerButton")
    public String registerPageButton(Model model,
                               @RequestParam(value = "userName") String userName,
                               @RequestParam(value = "email") String email,
                               @RequestParam(value = "password") String password){
        String registerPageButton = perpusOnlineServices.registerPageButton(model, userName, email, password);
        return registerPageButton;
    }

    @RequestMapping("loginPage")
    public String loginPage(){
        return "loginPage";
    }

    @RequestMapping("loginButton")
    public String loginPage(Model model, @RequestParam(value = "email") String email, @RequestParam(value = "password") String password) {
        String loginPage = perpusOnlineServices.loginPage(model, email, password);
        return loginPage;
    }

    @RequestMapping("dashboardPage")
    public String dashboardPage(Model model){
        String dashboardPage = perpusOnlineServices.dashboardPage(model);
        return dashboardPage;
    }

    @RequestMapping("pinjamBuku")
    public String borrowingBook(Model model, @RequestParam("bookId") Integer bookId) {
        String borrowingBook = perpusOnlineServices.borrowingBook(model, bookId);
        return borrowingBook;
    }

    @RequestMapping("returnBook")
    public String returnBook(Model model, @RequestParam("bookId") Integer bookId) {
        String returnBook = perpusOnlineServices.returnBook(model, bookId);
        return returnBook;
    }

    @RequestMapping("logoutPage")
    public String logout(@RequestParam("userLogin") String userLogin) {
        String logout = perpusOnlineServices.logoutPage(userLogin);
        return logout;
    }

    @RequestMapping("adminPage")
    public String adminPage(Model model){
        String adminPage = perpusOnlineServices.adminPage(model);
        return adminPage;
    }
}
