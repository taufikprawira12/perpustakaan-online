package com.perpusonline;

import com.perpusonline.domain.MstBook;
import com.perpusonline.domain.MstMember;
import com.perpusonline.domain.TrxOrder;
import com.perpusonline.repository.MstBookRepository;
import com.perpusonline.repository.MstMemberRepository;
import com.perpusonline.repository.TrxOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@Controller
public class PerpusonlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(PerpusonlineApplication.class, args);
	}

	List<MstMember> mstMemberList = new ArrayList<>();

	@Autowired
	private MstBookRepository mstBookRepository;

	@Autowired
	private TrxOrderRepository trxOrderRepository;

	@Autowired
	private MstMemberRepository mstMemberRepository;

	int countBook1 = 3;
	int countBook2 = 3;
	int countBook3 = 3;

	List<MstBook> mstBookList = new ArrayList<>();
	MstBook mstBook = new MstBook(1, "Gefami", "Ujang", 2);
	MstBook mstBook2 = new MstBook(2,"Gefamo", "Ujan", 2);
	MstBook mstBook3 = new MstBook(3, "Gefama", "Uja", 2);


	@RequestMapping("/")
	public String registerPage(){
		return "registerPage";
	}

	@RequestMapping("/registerButton")
	public String registerPage(Model model,
							   @RequestParam(value = "userName") String userName,
							   @RequestParam(value = "email") String email,
							   @RequestParam(value = "password") String password){
		MstMember mstMember = new MstMember(userName, email, password);
		if (!mstMember.isValidEmail()){
			model.addAttribute("errorEmail", "Format email tidak valid!");
		} else {
			if (mstMemberList.size() > 0){
				boolean isDuplicateEmail = mstMemberList.stream().anyMatch(userEmail -> email.equalsIgnoreCase(userEmail.getEmail()));
				if (isDuplicateEmail){
					model.addAttribute("errorEmail", "Email sudah terdaftar");
				} else {
					model.addAttribute("", null);
				}
			}
		}

		if (!mstMember.isValidPassword()){
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
			mstMember.setUserName(userName);
			mstMember.setEmail(email);
			mstMember.setPassword(password);
			mstMember.setIdMember(UUID.randomUUID().toString().substring(0, 14));
			mstMemberRepository.save(mstMember);
			mstMemberList.add(mstMember);
			return loginPage();
		}
	}

	@RequestMapping("loginPage")
	public String loginPage(){
		return "loginPage";
	}

	@RequestMapping("loginButton")
	public String loginPage(Model model, @RequestParam(value = "email") String email, @RequestParam(value = "password") String password) {
		boolean isRegistered = mstMemberList.stream().anyMatch(
				user -> email.equalsIgnoreCase(user.getEmail()) &&
						password.equalsIgnoreCase(user.getPassword()));

		if (!isRegistered){
			model.addAttribute("email", email);
			model.addAttribute("password", password);
			model.addAttribute("errorLogin", "Email atau password tidak valid!");
		} else {
			model.addAttribute("", null);
			return dashboardPage(model.addAttribute("",null));
		}
		return "loginPage";
	}

	@RequestMapping("dashboardPage")
	public String dashboardPage(Model model){
		mstBookList.add(mstBook);
		mstBookList.add(mstBook2);
		mstBookList.add(mstBook3);
		mstBookRepository.saveAll(mstBookList);
		System.out.println("BookList : "+ mstBookList.size());
		List<MstBook> mstBook1 = mstBookRepository.findAll();

		for (MstBook item : mstBook1
			 ) {
			model.addAttribute("stock", item.getStock());
			model.addAttribute("name", item.getBookName());
			model.addAttribute("author", item.getAuthor());

		}
		return "dashboardPage";
	}

	@RequestMapping("pinjamBuku")
	public String borrowingBook( Model model, @RequestParam("bookId") Integer bookId) {
		MstBook borrowedBook = mstBookRepository.findById(bookId).orElse(null);

		if (borrowedBook != null) {
			model.addAttribute("name", borrowedBook.getBookName());
			model.addAttribute("author", borrowedBook.getAuthor());
			model.addAttribute("stock", borrowedBook.getStock() - 1);


			borrowedBook.setBookName(borrowedBook.getBookName());
			borrowedBook.setAuthor(borrowedBook.getAuthor());
			borrowedBook.setStock(borrowedBook.getStock() - 1);

			TrxOrder trxOrder = new TrxOrder();
			trxOrder.setBookName(borrowedBook.getBookName());
			trxOrder.setAuthor(borrowedBook.getAuthor());
			trxOrder.setJumlahBuku(1);
			trxOrder.setIdMember("12345");

			trxOrderRepository.save(trxOrder);

		} else {
			model.addAttribute("name", null);
			model.addAttribute("author", null);
			model.addAttribute("stock", null);
		}
		return "dashboardPage";
	}
}
