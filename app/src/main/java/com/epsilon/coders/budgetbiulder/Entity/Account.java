package com.epsilon.coders.budgetbiulder.Entity;

/**
 * Created by Raufur on 9/12/14.
 */
public class Account {
    private int id;
    private String name;
    private String thumbUrl;
    private String bank;

    public Account(){}

    public Account(int id, String name, String bank, String thumbUrl){
        this.id = id;
        this.name = name;
        this.bank = bank;
        this.thumbUrl = thumbUrl;
    }

    public Account(String name, String bank, String thumbUrl){
        this.name = name;
        this.bank = bank;
        this.thumbUrl = thumbUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}
