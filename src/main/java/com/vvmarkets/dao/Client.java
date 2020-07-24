package com.vvmarkets.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Client {
   @SerializedName("id")
   @Expose
   private String ID;

   @SerializedName("full_name")
   @Expose
   private String FullName;

   @SerializedName("discount_card_number")
   @Expose
   private String DiscountCardNumber;

   @SerializedName("discount_percent")
   @Expose
   private int DiscountPercent;

   @SerializedName("phone")
   @Expose
   private String Phone;

   @SerializedName("email")
   @Expose
   private String Email;

   @SerializedName("gender")
   @Expose
   private String Gender;

   @SerializedName("description")
   @Expose
   private String Description;

   @SerializedName("form")
   @Expose
   private String Form;

   @SerializedName("registration_address")
   @Expose
   private String RegistrationAddress;

   @SerializedName("balance")
   @Expose
   private String Balance;

   public String getID() {
      return ID;
   }

   public void setID(String ID) {
      this.ID = ID;
   }

   public String getFullName() {
      return FullName;
   }

   public void setFullName(String fullName) {
      FullName = fullName;
   }

   public String getDiscountCardNumber() {
      return DiscountCardNumber;
   }

   public void setDiscountCardNumber(String discountCardNumber) {
      DiscountCardNumber = discountCardNumber;
   }

   public int getDiscountPercent() {
      return DiscountPercent;
   }

   public void setDiscountPercent(int discountPercent) {
      DiscountPercent = discountPercent;
   }

   public String getPhone() {
      return Phone;
   }

   public void setPhone(String phone) {
      Phone = phone;
   }

   public String getEmail() {
      return Email;
   }

   public void setEmail(String email) {
      Email = email;
   }

   public String getGender() {
      return Gender;
   }

   public void setGender(String gender) {
      Gender = gender;
   }

   public String getDescription() {
      return Description;
   }

   public void setDescription(String description) {
      Description = description;
   }

   public String getForm() {
      return Form;
   }

   public void setForm(String form) {
      Form = form;
   }

   public String getRegistrationAddress() {
      return RegistrationAddress;
   }

   public void setRegistrationAddress(String registrationAddress) {
      RegistrationAddress = registrationAddress;
   }

   public String getBalance() {
      return Balance;
   }

   public void setBalance(String balance) {
      Balance = balance;
   }

}
