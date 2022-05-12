package com.drivinglicence.myapp.service;

public class EmailAlreadyUsedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmailAlreadyUsedException() {
        super("Email này đã có người sử dụng!");
    }
}
