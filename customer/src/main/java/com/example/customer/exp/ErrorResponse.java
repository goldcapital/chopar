package com.example.customer.exp;

import java.util.Map;

public record ErrorResponse(Map<String, String> errors) {
}
