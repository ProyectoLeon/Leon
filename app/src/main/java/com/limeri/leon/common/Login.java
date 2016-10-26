package com.limeri.leon.common;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Login {
    private static final int LOGIN_TIME_OUT = 5;
    private String dispositivo;
    private Long timeStamp;

    public Login(){
        dispositivo = Application.getDeviceId();
        timeStamp = new Date().getTime();
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static boolean isLoginValido(Login login) {
        return isSameDevice(login) && !isLoginVencido(login);
    }

    public static boolean isLoginVencido(Login login) {
        Date today = new Date();
        long diff = TimeUnit.MILLISECONDS.toMinutes(today.getTime() - login.getTimeStamp());
        return diff >= LOGIN_TIME_OUT;
    }

    public static boolean isSameDevice(Login login) {
        return Application.getDeviceId().equals(login.getDispositivo());
    }
}
