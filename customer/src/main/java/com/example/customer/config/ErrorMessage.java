package com.example.customer.config;

public class ErrorMessage {
    public static String USER_NOT_FOUND="Customer not Found with Id:: %s";
    public static final String KEYCLOAK_RESPONSE_STATUS_409="User already exists in Keycloak with this username or email %s";
    public static final String KEYCLOAK_RESPONSE_STATUS_201_AND_204="Unexpected response from Keycloak status code:%s and username %s";
    public final  static String USERNAME_NOT_FOUND ="No  user found with username: %s {}";
}
