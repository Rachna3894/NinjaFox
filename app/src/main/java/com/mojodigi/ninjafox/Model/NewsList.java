
package com.mojodigi.ninjafox.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewsList implements Serializable {

//    @SerializedName("categoryId")
//    @Expose
//    private String categoryId;



    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("canonical_url")
    @Expose
    private String canonicalUrl;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("publishedDate")
    @Expose
    private String publishedDate;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;

//
//    public String getCategoryId() {
//        return categoryId;
//    }
//
//    public void setCategoryId(String categoryId) {
//        this.categoryId = categoryId;
//    }

    public NewsList() {
    }


//    public NewsList(String categoryId, String title, String description, String canonicalUrl, String source, String publishedDate, String tags, String imageUrl) {
//        super();
//        //this.categoryId = categoryId;
//        this.title = title;
//        this.description = description;
//        this.canonicalUrl = canonicalUrl;
//        this.source = source;
//        this.publishedDate = publishedDate;
//        this.tags = tags;
//        this.imageUrl = imageUrl;
//    }


    public NewsList(String title, String description, String canonicalUrl, String source, String publishedDate, String tags, String imageUrl) {
        super();
        this.title = title;
        this.description = description;
        this.canonicalUrl = canonicalUrl;
        this.source = source;
        this.publishedDate = publishedDate;
        this.tags = tags;
        this.imageUrl = imageUrl;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCanonicalUrl() {
        return canonicalUrl;
    }

    public void setCanonicalUrl(String canonicalUrl) {
        this.canonicalUrl = canonicalUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



}
