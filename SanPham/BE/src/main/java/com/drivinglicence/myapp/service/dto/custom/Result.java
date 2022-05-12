package com.drivinglicence.myapp.service.dto.custom;

public class Result {
    private String messange;

    public Result(String messange) {
        this.messange = messange;
    }

    public String getMessange() {
        return messange;
    }

    public void setMessange(String messange) {
        this.messange = messange;
    }
}
