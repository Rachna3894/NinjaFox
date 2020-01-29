
package com.mojodigi.ninjafox.Model;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewsMainModel {

    @SerializedName("categoryId")
    @Expose
    private String categoryId;

    @SerializedName("categoryName")
    @Expose
    private String categoryName;

    @SerializedName("newsList")
    @Expose
    private ArrayList<NewsList> newsList = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public NewsMainModel() {
    }


    public NewsMainModel(String categoryName, ArrayList<NewsList> newsList) {
        super();
        this.categoryName = categoryName;
        this.newsList = newsList;
    }
    public NewsMainModel(String categoryId ,String categoryName, ArrayList<NewsList> newsList) {
        super();
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.newsList = newsList;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<NewsList> getNewsList() {
        return newsList;
    }

    public void setNewsList(ArrayList<NewsList> newsList) {
        this.newsList = newsList;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }




}
