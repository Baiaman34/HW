package com.example.hw;

public class Contacts {
    private String contactName;
    private String phoneNumbers;

    public Contacts(String contactName, String phoneNumbers) {
        this.contactName = contactName;
        this.phoneNumbers = phoneNumbers;
    }

    public String getContactName() {
        return contactName;
    }

    public String getPhoneNumbers() {
        return phoneNumbers;
    }
}
