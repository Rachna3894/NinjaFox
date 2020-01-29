package com.mojodigi.ninjafox.Model;

import java.util.ArrayList;

public class NewsSuperParentModel {


    String languageCode;
    String languageName;
    ArrayList<NewsMainModel> categoryList;

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public ArrayList<NewsMainModel> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<NewsMainModel> categoryList) {
        this.categoryList = categoryList;
    }


}
