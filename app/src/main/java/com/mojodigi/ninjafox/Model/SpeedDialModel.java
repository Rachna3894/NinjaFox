package com.mojodigi.ninjafox.Model;

import org.apache.http.auth.NTCredentials;

public class SpeedDialModel {

    private String siteTitle;
    private int siteLogo;
    private String siteUrl;

    public String getSiteTitle(){return siteTitle; }
    public int getSiteLog(){return siteLogo; }
    public String getSiteUrl(){return siteUrl; }

   public SpeedDialModel(String siteTitle, String siteUrl, int siteLogo)
   {
       this.siteUrl=siteUrl;
       this.siteLogo=siteLogo;
       this.siteTitle=siteTitle;
   }
   public SpeedDialModel()
   {

   }

    public void setSiteTitle(String siteTitle)
    {
        this.siteTitle=siteTitle;
    }
    public void setSiteLogo(int siteLogo)
    {
        this.siteLogo=siteLogo;
    }
    public void setSiteUrl(String siteUrl)
    {
        this.siteUrl=siteUrl;
    }

}
