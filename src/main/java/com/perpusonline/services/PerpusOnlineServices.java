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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
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


    public String homePage(){
        List<MstMember> mstMemberList = new ArrayList<>();
        List<MstBook> mstBookList = new ArrayList<>();
        MstBook mstBook = new MstBook(1, "Gefami", "Ujang", 2);
        MstBook mstBook2 = new MstBook(2,"Gefamo", "Ujan", 2);
        MstBook mstBook3 = new MstBook(3, "Gefama", "Uja", 2);
        MstMember mstMember = new MstMember("admin", "admin@admin.com", "admin", UUID.randomUUID().toString().substring(0,13), true);

        mstBookList.add(mstBook);
        mstBookList.add(mstBook2);
        mstBookList.add(mstBook3);
        mstMemberRepository.save(mstMember);
        mstBookRepository.saveAll(mstBookList);

        return "homePage";
    }

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
            return loginPage();
        }
    }

    public String loginPage(){
        return "loginPage";
    }

    public String loginPage(Model model, String email, String password) {
        MstMember mstMember = mstMemberRepository.findMstMemberByEmailAndPassword(email, password);
        if (mstMember == null){
            model.addAttribute("email", email);
            model.addAttribute("password", password);
            model.addAttribute("errorLogin", "Email atau password tidak valid!");
        } else {
            if (mstMember.isAdmin()){
                mstMember.setIsLogin("ACTIVE");
                mstMemberRepository.save(mstMember);
                model.addAttribute("userLogin", mstMember.getIdMember());
                model.addAttribute("", null);
                return adminPage(model.addAttribute("",null));
            }
            mstMember.setIsLogin("ACTIVE");
            mstMemberRepository.save(mstMember);
            model.addAttribute("userLogin", mstMember.getIdMember());
            model.addAttribute("", null);
            return dashboardPage(model.addAttribute("",null));
        }
        return "loginPage";
    }

    public String dashboardPage(Model model){
        List<MstBook> mstBook1 = mstBookRepository.findAll();

        for (MstBook item : mstBook1
        ) {
            model.addAttribute("stock", item.getStock());
            model.addAttribute("name", item.getBookName());
            model.addAttribute("author", item.getAuthor());

        }
        return "dashboardPage";
    }

    public String borrowingBook(Model model, Integer bookId) {
        MstBook borrowedBook = mstBookRepository.findById(bookId).orElse(null);
        MstMember mstMember = mstMemberRepository.findByIsLogin("ACTIVE");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = LocalDate.now().format(formatter);

        if (mstMember.isBorrowed()){
            model.addAttribute("isBorrowed", "Maximal peminjaman 1 buku, mohon kembalikan dulu buku yang telah anda pinjam");
            model.addAttribute("name", borrowedBook.getBookName());
            model.addAttribute("author", borrowedBook.getAuthor());
            model.addAttribute("stock", borrowedBook.getStock());
            model.addAttribute("userLogin", mstMember.getIdMember());
        } else {
            if (borrowedBook != null) {
                model.addAttribute("name", borrowedBook.getBookName());
                model.addAttribute("author", borrowedBook.getAuthor());
                model.addAttribute("stock", borrowedBook.getStock() - 1);
                model.addAttribute("userLogin", mstMember.getIdMember());

                borrowedBook.setBookName(borrowedBook.getBookName());
                borrowedBook.setAuthor(borrowedBook.getAuthor());
                borrowedBook.setStock(borrowedBook.getStock() - 1);

                TrxOrder trxOrder = new TrxOrder();
                trxOrder.setBookName(borrowedBook.getBookName());
                trxOrder.setAuthor(borrowedBook.getAuthor());
                trxOrder.setJumlahBuku(1);
                trxOrder.setIdBook(borrowedBook.getId());
                trxOrder.setIdMember(mstMember.getIdMember());
                mstMember.setBorrowed(true);
                trxOrder.setTglPeminjaman(formattedDate);
                trxOrder.setReturnDeadline(LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                trxOrder.setReturn(false);
                trxOrderRepository.save(trxOrder);
            } else {
                model.addAttribute("name", null);
                model.addAttribute("author", null);
                model.addAttribute("stock", null);
            }
        }
        return "dashboardPage";
    }

    public String returnBook(Model model, Integer bookId) {
        MstBook borrowedBook = mstBookRepository.findById(bookId).orElse(null);
        MstMember mstMember = mstMemberRepository.findByIsLogin("ACTIVE");
        TrxOrder trxOrder = trxOrderRepository.findByIdBook(bookId, mstMember.getIdMember());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = LocalDate.now().format(formatter);

        borrowedBook.setStock(borrowedBook.getStock() + 1);
        mstMember.setBorrowed(false);
        trxOrder.setReturn(true);
        trxOrder.setTglPengembalian(formattedDate);
        trxOrderRepository.save(trxOrder);
        mstBookRepository.save(borrowedBook);
        mstMemberRepository.save(mstMember);

        model.addAttribute("name", borrowedBook.getBookName());
        model.addAttribute("author", borrowedBook.getAuthor());
        model.addAttribute("stock", borrowedBook.getStock());
        model.addAttribute("userLogin", mstMember.getIdMember());

        return "dashboardPage";
    }

    public String logoutPage(String logoutUser) {
        MstMember mstMember = mstMemberRepository.findByIdMember(logoutUser);
        if (mstMember != null){
            mstMember.setIsLogin("INACTIVE");
            mstMemberRepository.save(mstMember);
        }
        return "loginPage";
    }

    public String adminPage(Model model){
        List<TrxOrder> trxOrders = trxOrderRepository.findAll();

        for (TrxOrder item : trxOrders
        ) {
            String tglPeminjamanString = item.getTglPeminjaman();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate tglPeminjaman = LocalDate.parse(tglPeminjamanString, formatter);
            LocalDate today = LocalDate.now();
            long keterlambatan = ChronoUnit.DAYS.between(tglPeminjaman, today);

            model.addAttribute("name", item.getBookName());
            model.addAttribute("author", item.getAuthor());
            model.addAttribute("idMember", item.getIdMember());
            model.addAttribute("tglPeminjaman", item.getTglPeminjaman());
            model.addAttribute("returnDeadline", item.getReturnDeadline());
            model.addAttribute("keterlambatan", keterlambatan);
        }
        return "adminPage";
    }
}
