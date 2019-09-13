package com.vvmarkets.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthorizationBody {
    public String getEmail() {
        return Email;
    }

    @SerializedName("Email")
    @Expose
    private String Email;

    public String getPassword() {
        return Password;
    }

    @SerializedName("Password")
    @Expose
    private String Password;

    public AuthorizationBody(String email, String password) {
        this.Email = email;
        this.Password = password;
    }
}
