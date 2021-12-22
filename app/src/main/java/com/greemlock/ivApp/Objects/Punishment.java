package com.greemlock.ivApp.Objects;

import java.io.Serializable;

public class Punishment implements Serializable {

    private int punishment_day;
    private int punishment_month;
    private int punishment_year;
    private int punishment_number;

    public Punishment(int punishment_day, int punishment_month, int punishment_year, int punishment_number) {
        this.punishment_day = punishment_day;
        this.punishment_month = punishment_month;
        this.punishment_year = punishment_year;
        this.punishment_number = punishment_number;
    }

    public Punishment() {
    }

    public int getPunishment_day() {
        return punishment_day;
    }

    public void setPunishment_day(int punishment_day) {
        this.punishment_day = punishment_day;
    }

    public int getPunishment_month() {
        return punishment_month;
    }

    public void setPunishment_month(int punishment_month) {
        this.punishment_month = punishment_month;
    }

    public int getPunishment_year() {
        return punishment_year;
    }

    public void setPunishment_year(int punishment_year) {
        this.punishment_year = punishment_year;
    }

    public int getPunishment_number() {
        return punishment_number;
    }

    public void setPunishment_number(int punishment_number) {
        this.punishment_number = punishment_number;
    }
}
