package com.example.notification.util;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class RandomUtil {

    private final static Random random=new Random();
    public  static String getRandomSmsCode(){
        String parol="123456789";
        StringBuilder password=new StringBuilder();
        for (int i=0;i<5;i++){
            int index=random.nextInt(parol.length());
            password.append(parol.charAt(index));
        }
        return password.toString();
    }
}
