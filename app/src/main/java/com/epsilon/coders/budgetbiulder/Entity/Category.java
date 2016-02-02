package com.epsilon.coders.budgetbiulder.Entity;

/**
 * Created by Raufur on 9/12/14.
 */
public class Category {
    private int id;
    private String name;
    private String thumbUrl;

    public Category(){}

    public Category(int id, String name, String thumbUrl){
        this.id = id;
        this.name = name;
        this.thumbUrl = thumbUrl;
    }

    public Category(String name, String thumbUrl){
        this.name = name;
        this.thumbUrl = thumbUrl;
    }

    public Category(String name){
        this.name = name;
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
}
