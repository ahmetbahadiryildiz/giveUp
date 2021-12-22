package com.greemlock.ivApp.Objects;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private String                user_key;
    private String                user_name;
    private String                user_surname;
    private int                   user_age;
    private String                user_user_name;
    private String                user_password;
    private String                user_date;
    private String                user_profile_photo_name;
    private int                   user_iv_para;
    private ArrayList<Punishment> user_punishment_list;
    private String                user_offer_1;
    private String                user_offer_2;
    private String                user_offer_3;
    private String                user_device;
    private boolean               user_is_admin;

    public User() {
    }

    public User(String user_key, String user_name, String user_surname, int user_age, String user_user_name, String user_password, String user_profile_photo_name, int user_iv_para, ArrayList<Punishment> user_punishment_list, String user_offer_1, String user_offer_2, String user_offer_3, String user_device, boolean user_is_admin) {
        this.user_name = user_name;
        this.user_surname = user_surname;
        this.user_age = user_age;
        this.user_user_name = user_user_name;
        this.user_password = user_password;
        this.user_profile_photo_name = user_profile_photo_name;
        this.user_iv_para = user_iv_para;
        this.user_punishment_list = user_punishment_list;
        this.user_offer_1 = user_offer_1;
        this.user_offer_2 = user_offer_2;
        this.user_offer_3 = user_offer_3;
        this.user_device = user_device;
        this.user_is_admin = user_is_admin;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_surname() {
        return user_surname;
    }

    public void setUser_surname(String user_surname) {
        this.user_surname = user_surname;
    }

    public int getUser_age() {
        return user_age;
    }

    public void setUser_age(int user_age) {
        this.user_age = user_age;
    }

    public String getUser_user_name() {
        return user_user_name;
    }

    public void setUser_user_name(String user_user_name) {
        this.user_user_name = user_user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_date() {
        return user_date;
    }

    public void setUser_date(String user_date) {
        this.user_date = user_date;
    }

    public String getUser_profile_photo_name() {
        return user_profile_photo_name;
    }

    public void setUser_profile_photo_name(String user_profile_photo_name) {
        this.user_profile_photo_name = user_profile_photo_name;
    }

    public int getUser_iv_para() {
        return user_iv_para;
    }

    public void setUser_iv_para(int user_iv_para) {
        this.user_iv_para = user_iv_para;
    }

    public ArrayList<Punishment> getUser_punishment_list() {
        return user_punishment_list;
    }

    public void setUser_punishment_list(ArrayList<Punishment> user_punishment_list) {
        this.user_punishment_list = user_punishment_list;
    }

    public String getUser_offer_1() {
        return user_offer_1;
    }

    public void setUser_offer_1(String user_offer_1) {
        this.user_offer_1 = user_offer_1;
    }

    public String getUser_offer_2() {
        return user_offer_2;
    }

    public void setUser_offer_2(String user_offer_2) {
        this.user_offer_2 = user_offer_2;
    }

    public String getUser_offer_3() {
        return user_offer_3;
    }

    public void setUser_offer_3(String user_offer_3) {
        this.user_offer_3 = user_offer_3;
    }

    public String getUser_device() {
        return user_device;
    }

    public void setUser_device(String user_device) {
        this.user_device = user_device;
    }

    public boolean isUser_is_admin() {
        return user_is_admin;
    }

    public void setUser_is_admin(boolean user_is_admin) {
        this.user_is_admin = user_is_admin;
    }
}