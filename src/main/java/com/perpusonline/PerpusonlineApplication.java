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

	String userNameGlobal = "";
	String emailGlobal = "";
	String passwordGlobal = "";

	List<MstUser> mstUsers = new ArrayList<>();

	@RequestMapping("/")
	public String registerPage(){
		return "registerPage";
	}

	@RequestMapping("/registerButton")
	public String registerPage(Model model, @RequestParam(value = "userName") String userName,
							   @RequestParam(value = "email") String email,
							   @RequestParam(value = "password") String password){
//		System.out.println("UserName : " + userName);
//		System.out.println("Email : " + email);
//		System.out.println("Password : " + password);
		MstUser newMstUser = new MstUser();

		userNameGlobal = userName;
		newMstUser.setUserName(userName);

		if (!isValidEmail(email)){
			System.out.println("Format email tidak valid!");
			model.addAttribute("errorEmail", "Format email tidak valid!");
		} else {
			if (email.equalsIgnoreCase(emailGlobal)){
				model.addAttribute("errorEmail", "Email sudah terdaftar");
			} else {
				emailGlobal = email;
				newMstUser.setEmail(email);
				model.addAttribute("", null);
			}
		}

		if (!isValidPassword(password)){
			System.out.println("Password tidak valid!");
			model.addAttribute("errorPassword",
					"Password harus terdiri dari minimal 8 karakter alfanumerik, setidaknya satu huruf kapital, dan tidak boleh mengandung karakter khusus!");
		} else {
			passwordGlobal = password;
			newMstUser.setPassword(password);
			model.addAttribute("", null);
		}

		if (model.containsAttribute("errorEmail") || model.containsAttribute("errorPassword")) {
			model.addAttribute("userName", userName);
			model.addAttribute("email", email);
			model.addAttribute("password", password);
			return "registerPage";
		} else {
			mstUsers.add(newMstUser);
			for (MstUser user : mstUsers) {
				System.out.println("User Name: " + user.getUserName());
				System.out.println("Email: " + user.getEmail());
				System.out.println("Password: " + user.getPassword());
			}
			System.out.println("Valid");
			return loginPage();
		}
	}

	@RequestMapping("loginPage")
	public String loginPage(){
		return "loginPage";
	}


	@RequestMapping("loginButton")
	public String loginPage(Model model, @RequestParam(value = "email") String email, @RequestParam(value = "password") String password) {
		System.out.println("Email Login : " + emailGlobal);
		System.out.println("Password Login : " + passwordGlobal);

		if (!email.equalsIgnoreCase(emailGlobal) || !password.equalsIgnoreCase(passwordGlobal)){
			System.out.println("Email atau password tidak valid!");
			model.addAttribute("email", email);
			model.addAttribute("password", password);
			model.addAttribute("errorLogin", "Email atau password tidak valid!");
		} else {
			model.addAttribute("", null);
			System.out.println("Valid");
			return dashboardPage();
		}
		return "loginPage";
	}

	@RequestMapping("dashboardPage")
	public String dashboardPage(){
		return "dashboardPage";
	}

	private boolean isValidEmail(String email) {
		String regex = ".*@(gmail\\.com|hotmail\\.com|outlook\\.com||yahoo\\.com)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	private boolean isValidPassword(String password) {
		String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}
}
