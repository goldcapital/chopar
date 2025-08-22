package com.example.region.exp;

import java.util.Map;

public record ErrorResponse(Map<String, String> errors) {
}
