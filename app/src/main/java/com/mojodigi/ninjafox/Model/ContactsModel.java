package com.mojodigi.ninjafox.Model;

public class ContactsModel {

    public String phoneName;
    public String phoneEmail;
    public String phoneNumber;

    public ContactsModel( ) {

    }

    public ContactsModel(String phoneName, String phoneEmail, String phoneNumber) {
        this.phoneName = phoneName;
        this.phoneEmail = phoneEmail;
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getPhoneEmail() {
        return phoneEmail;
    }

    public void setPhoneEmail(String phoneEmail) {
        this.phoneEmail = phoneEmail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "ContactsModel{" +
                "phoneName='" + phoneName + '\'' +
                ", phoneEmail='" + phoneEmail + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }


}
