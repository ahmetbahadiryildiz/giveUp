package com.greemlock.ivApp.Objects;

public class Offer {

    private String offer_id;
    private String offer_company_name;
    private String offer_description;
    private int    offer_price;
    private String offer_code;
    private String offer_company_photo_name;

    public Offer() {
    }

    public Offer(String offer_id, String offer_company_name, String offer_description, int offer_price, String offer_code, String offer_company_photo_name) {
        this.offer_id = offer_id;
        this.offer_company_name = offer_company_name;
        this.offer_description = offer_description;
        this.offer_price = offer_price;
        this.offer_code = offer_code;
        this.offer_company_photo_name = offer_company_photo_name;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public String getOffer_company_name() {
        return offer_company_name;
    }

    public void setOffer_company_name(String offer_company_name) {
        this.offer_company_name = offer_company_name;
    }

    public String getOffer_description() {
        return offer_description;
    }

    public void setOffer_description(String offer_description) {
        this.offer_description = offer_description;
    }

    public int getOffer_price() {
        return offer_price;
    }

    public void setOffer_price(int offer_price) {
        this.offer_price = offer_price;
    }

    public String getOffer_code() {
        return offer_code;
    }

    public void setOffer_code(String offer_code) {
        this.offer_code = offer_code;
    }

    public String getOffer_company_photo_name() {
        return offer_company_photo_name;
    }

    public void setOffer_company_photo_name(String offer_company_photo_name) {
        this.offer_company_photo_name = offer_company_photo_name;
    }
}
