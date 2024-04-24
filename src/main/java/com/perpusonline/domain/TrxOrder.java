package com.perpusonline.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "TRX_ORDER")
public class TrxOrder {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "idBook")
    private Integer idBook;

    @Column(name = "bookName")
    private String bookName;

    @Column(name = "author")
    private String author;

    @Column(name = "jumlahBuku")
    private Integer jumlahBuku;

    @Column(name = "idMember")
    private String idMember;

    @Column(name = "tglPeminjaman")
    private String tglPeminjaman;

    @Column(name = "returnDeadline")
    private String returnDeadline;

    @Column(name = "tglPengembalian")
    private String tglPengembalian;

    @Column(name = "isReturn")
    private boolean isReturn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdBook() {
        return idBook;
    }

    public void setIdBook(Integer idBook) {
        this.idBook = idBook;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getJumlahBuku() {
        return jumlahBuku;
    }

    public void setJumlahBuku(Integer jumlahBuku) {
        this.jumlahBuku = jumlahBuku;
    }

    public String getIdMember() {
        return idMember;
    }

    public void setIdMember(String idMember) {
        this.idMember = idMember;
    }

    public String getTglPeminjaman() {
        return tglPeminjaman;
    }

    public void setTglPeminjaman(String tglPeminjaman) {
        this.tglPeminjaman = tglPeminjaman;
    }

    public String getTglPengembalian() {
        return tglPengembalian;
    }

    public void setTglPengembalian(String tglPengembalian) {
        this.tglPengembalian = tglPengembalian;
    }

    public boolean isReturn() {
        return isReturn;
    }

    public void setReturn(boolean aReturn) {
        isReturn = aReturn;
    }

    public String getReturnDeadline() {
        return returnDeadline;
    }

    public void setReturnDeadline(String returnDeadline) {
        this.returnDeadline = returnDeadline;
    }
}
