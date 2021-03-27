package com.org.cafemgmt.service;

public class Testregex {
    public static void main(String[] args) {
        String url  = "ww.goog.coe?error=invlid";

        String[] urlarr = url.split("\\?");
        System.out.println(urlarr[0]);
    }
}
