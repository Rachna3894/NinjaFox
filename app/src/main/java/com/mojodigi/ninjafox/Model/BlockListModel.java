package com.mojodigi.ninjafox.Model;

public class BlockListModel {


    private String siteUrl;

    long time;
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }



    public String getSiteUrl(){return siteUrl; }

   public BlockListModel(String siteTitle, String siteUrl, int siteLogo)
   {
       this.siteUrl=siteUrl;

   }
   public BlockListModel()
   {

   }


    public void setSiteUrl(String siteUrl)
    {
        this.siteUrl=siteUrl;
    }

}
