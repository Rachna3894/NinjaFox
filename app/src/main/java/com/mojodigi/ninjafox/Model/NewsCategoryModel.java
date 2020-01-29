package com.mojodigi.ninjafox.Model;

import java.io.Serializable;
import java.util.Objects;

public class NewsCategoryModel implements Serializable {

    private String newsCategoryId ;
    private String newsCategoryName ;
    private boolean isSelected;

    public NewsCategoryModel( ) {
    }

    public NewsCategoryModel(String newsCategoryId, String newsCategoryName) {
        this.newsCategoryId = newsCategoryId;
        this.newsCategoryName = newsCategoryName;
    }

    public NewsCategoryModel(String newsCategoryId, String newsCategoryName , boolean isSelected) {
        this.newsCategoryId = newsCategoryId;
        this.newsCategoryName = newsCategoryName;
        this.isSelected = isSelected;
    }

    public String getNewsCategoryId() {
        return newsCategoryId;
    }

    public void setNewsCategoryId(String newsCategoryId) {
        this.newsCategoryId = newsCategoryId;
    }

    public String getNewsCategoryName() {
        return newsCategoryName;
    }

    public void setNewsCategoryName(String newsCategoryName) {
        this.newsCategoryName = newsCategoryName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
