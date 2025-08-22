package com.example.customer.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CacheKeyGenerator {
    public static String generateCustomerKey(String email, String phone) {
        return "customer:" + (email != null ? email : "") + ":" + (phone != null ? phone : "");
    }
    public static String generateProfileKeyAll(Integer page, Integer
                                               size) {
        return "profile:" + (page != null ? page : "") + (size != null ? size : "");
    }
}
