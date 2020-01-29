package com.mojodigi.ninjafox.Model;

import java.util.ArrayList;

public class LanguageCategoryModel {

    String languageCode,languageName;
    ArrayList <CategoryModel> cateGoryList;

    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean checked) {
        isChecked = checked;
    }

    boolean isChecked;
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

    public ArrayList<CategoryModel> getCateGoryList() {
        return cateGoryList;
    }

    public void setCateGoryList(ArrayList<CategoryModel> cateGoryList) {
        this.cateGoryList = cateGoryList;
    }
}
