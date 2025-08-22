package com.example.fie_service.exp;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors
) {
}
