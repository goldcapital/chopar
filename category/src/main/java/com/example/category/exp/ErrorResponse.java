package com.example.category.exp;

import java.util.Map;

public record ErrorResponse(Map<String, String> errors) {
}
