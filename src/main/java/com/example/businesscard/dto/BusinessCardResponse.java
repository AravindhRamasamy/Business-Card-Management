package com.example.businesscard.dto;

public class BusinessCardResponse {

    private Long id;
    private String fullName;
    private String company;
    private String jobTitle;
    private String email;
    private String phone;
    private String address;

    public BusinessCardResponse() {
    }

    public BusinessCardResponse(Long id, String fullName, String company, String jobTitle, String email, String phone, String address) {
        this.id = id;
        this.fullName = fullName;
        this.company = company;
        this.jobTitle = jobTitle;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
