package com.perpusonline;

import com.perpusonline.domain.MstUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
@Controller
public class PerpusonlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(PerpusonlineApplication.class, args);
	}

	List<MstUser> mstUserList = new ArrayList<>();

	@RequestMapping("/")
	public String registerPage(){
		return "registerPage";
	}

	@RequestMapping("/registerButton")
	public String registerPage(Model model,
							   @RequestParam(value = "userName") String userName,
							   @RequestParam(value = "email") String email,
							   @RequestParam(value = "password") String password){
		MstUser mstUser = new MstUser(userName, email, password);
		if (!mstUser.isValidEmail()){
			model.addAttribute("errorEmail", "Format email tidak valid!");
		} else {
			if (mstUserList.size() > 0){
				boolean isDuplicateEmail = mstUserList.stream().anyMatch(userEmail -> email.equalsIgnoreCase(userEmail.getEmail()));
				if (isDuplicateEmail){
					model.addAttribute("errorEmail", "Email sudah terdaftar");
				} else {
					model.addAttribute("", null);
				}
			}
		}

		if (!mstUser.isValidPassword()){
			model.addAttribute("errorPassword",
					"Password harus terdiri dari minimal 8 karakter alfanumerik, setidaknya satu huruf kapital, dan tidak boleh mengandung karakter khusus!");
		} else {
			model.addAttribute("", null);
		}

		if (model.containsAttribute("errorEmail") || model.containsAttribute("errorPassword")) {
			model.addAttribute("userName", userName);
			model.addAttribute("email", email);
			model.addAttribute("password", password);
			return "registerPage";
		} else {
			mstUserList.add(mstUser);
			return loginPage();
		}
	}

	@RequestMapping("loginPage")
	public String loginPage(){
		return "loginPage";
	}

	@RequestMapping("loginButton")
	public String loginPage(Model model, @RequestParam(value = "email") String email, @RequestParam(value = "password") String password) {
		boolean isRegistered = mstUserList.stream().anyMatch(
				user -> email.equalsIgnoreCase(user.getEmail()) &&
						password.equalsIgnoreCase(user.getPassword()));

		if (!isRegistered){
			model.addAttribute("email", email);
			model.addAttribute("password", password);
			model.addAttribute("errorLogin", "Email atau password tidak valid!");
		} else {
			model.addAttribute("", null);
			return dashboardPage();
		}
		return "loginPage";
	}

	@RequestMapping("dashboardPage")
	public String dashboardPage(){
		return "dashboardPage";
	}
}
