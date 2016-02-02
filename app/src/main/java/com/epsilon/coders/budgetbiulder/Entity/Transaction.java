package com.epsilon.coders.budgetbiulder.Entity;

/**
 * Created by Raufur on 9/12/14.
 */
public class Transaction {
    private int id;
    private double amount;
    private int category;
    private boolean isDone;
    private long date;
    private String commentary;
    private int account;

    public Transaction(){}

    public Transaction(int id, double amount, int category, boolean isDone, long date, String commentary, int account){
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.isDone = isDone;
        this.date = date;
        this.commentary = commentary;
        this.account = account;
    }

    public Transaction(double amount, int category, boolean isDone, long date, String commentary, int account){
        this.amount = amount;
        this.category = category;
        this.isDone = isDone;
        this.date = date;
        this.commentary = commentary;
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }
}
