package com.app.tanyahukum.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

/**
 * Created by emerio on 4/8/17.
 */

public class User {
    String name;
    String imagePath;
    int totalConsultation;

    public int getTotalConsultation() {
        return totalConsultation;
    }

    public void setTotalConsultation(int totalConsultation) {
        this.totalConsultation = totalConsultation;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String bornDate;
    public String getFirebaseToken() {
        return firebaseToken;
    }
    private String lawFirm;
    private String lawFirmCity;

    public String getLawFirm() {
        return lawFirm;
    }
    private ArrayList<String> specialization;

    public ArrayList<String> getSpecialization() {
        return specialization;
    }

    public void setSpecialization(ArrayList<String> specialization) {
        this.specialization = specialization;
    }

    public void setLawFirm(String lawFirm) {
        this.lawFirm = lawFirm;
    }



    public String getLawFirmCity() {
        return lawFirmCity;
    }

    public void setLawFirmCity(String lawFirmCity) {
        this.lawFirmCity = lawFirmCity;
    }

    public String getLawFirmAddress() {
        return lawFirmAddress;
    }

    public void setLawFirmAddress(String lawFirmAddress) {
        this.lawFirmAddress = lawFirmAddress;
    }

    public String getLawFirmPhone() {
        return lawFirmPhone;
    }

    public void setLawFirmPhone(String lawFirmPhone) {
        this.lawFirmPhone = lawFirmPhone;
    }

    private String lawFirmAddress;
    private String lawFirmPhone;
    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    private String firebaseToken;

    public String getUsertype() {
        return usertype;
    }

    public User() {
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;

    }

    private String usertype;
    public enum Gender{
        MALE, FEMALE, TRANSGENDER
    };
    public Gender gender;
    private String email;
    private String fileName;
    private String id;
    private String province;

    public String getBornDate() {
        return bornDate;
    }

    public void setBornDate(String bornDate) {
        this.bornDate = bornDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String city;
    private String address;
    private String phone;

    @Exclude
    private String password;
    @Exclude
    public Gender getGenderVal() {
        return gender;
    }

    public String getGender() {
        if(gender == null)
            return null;
        else
            return gender.name();
    }

    public void setGender(String genderString) {

        if(genderString == null)
            this.gender = null;
        else
            this.gender = Gender.valueOf(genderString);
    }
}
