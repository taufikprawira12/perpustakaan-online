package com.perpusonline.services;

import com.perpusonline.domain.MstBook;
import com.perpusonline.domain.MstMember;
import com.perpusonline.domain.TrxOrder;
import com.perpusonline.repository.MstBookRepository;
import com.perpusonline.repository.MstMemberRepository;
import com.perpusonline.repository.TrxOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PerpusOnlineServices {

    @Autowired
    private MstBookRepository mstBookRepository;

    @Autowired
    private TrxOrderRepository trxOrderRepository;

    @Autowired
    private MstMemberRepository mstMemberRepository;

    List<MstMember> mstMemberList = new ArrayList<>();
    List<MstBook> mstBookList = new ArrayList<>();
    MstBook mstBook = new MstBook(1, "Gefami", "Ujang", 2);
    MstBook mstBook2 = new MstBook(2,"Gefamo", "Ujan", 2);
    MstBook mstBook3 = new MstBook(3, "Gefama", "Uja", 2);

    public String registerPageButton(Model model, String userName, String email, String password){
        MstMember mstMember = new MstMember(userName, email, password);
        if (!mstMember.isValidEmail()){
            model.addAttribute("errorEmail", "Format email tidak valid!");
        } else {
            MstMember isExistMember = mstMemberRepository.findMstMemberByEmail(email);
            if (isExistMember != null){
                model.addAttribute("errorEmail", "Email sudah terdaftar");
            } else {
                model.addAttribute("", null);
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
            mstMember.setIdMember(UUID.randomUUID().toString().substring(0, 13));
            mstMemberRepository.save(mstMember);
            mstMemberList.add(mstMember);
            return loginPage();
        }
    }

    public String loginPage(){
        return "loginPage";
    }

    public String loginPage(Model model, @RequestParam(value = "email") String email, @RequestParam(value = "password") String password) {
        MstMember mstMember = mstMemberRepository.findMstMemberByEmailAndPassword(email, password);
        if (mstMember == null){
            model.addAttribute("email", email);
            model.addAttribute("password", password);
            model.addAttribute("errorLogin", "Email atau password tidak valid!");
        } else {
            mstMember.setIsLogin("ACTIVE");
            model.addAttribute("", null);
            return dashboardPage(model.addAttribute("",null));
        }
        return "loginPage";
    }

    public String dashboardPage(Model model){
        mstBookList.add(mstBook);
        mstBookList.add(mstBook2);
        mstBookList.add(mstBook3);
        mstBookRepository.saveAll(mstBookList);
        List<MstBook> mstBook1 = mstBookRepository.findAll();

        for (MstBook item : mstBook1
        ) {
            model.addAttribute("stock", item.getStock());
            model.addAttribute("name", item.getBookName());
            model.addAttribute("author", item.getAuthor());

        }
        return "dashboardPage";
    }

    public String borrowingBook( Model model, @RequestParam("bookId") Integer bookId) {
        MstBook borrowedBook = mstBookRepository.findById(bookId).orElse(null);
        MstMember mstMember = mstMemberRepository.findByIsLogin("ACTIVE");

        if (mstMember.isBorrowed()){
            model.addAttribute("isBorrowed", "Maximal peminjaman 1 buku, mohon kembalikan dulu buku yang telah anda pinjam");
        } else {
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
                trxOrder.setIdBook(mstBook.getId());
                if (mstMember != null){
                    trxOrder.setIdMember(mstMember.getIdMember());
                    mstMember.setBorrowed(true);
                }

                trxOrderRepository.save(trxOrder);

            } else {
                model.addAttribute("name", null);
                model.addAttribute("author", null);
                model.addAttribute("stock", null);
            }
        }
        return "dashboardPage";
    }
}
