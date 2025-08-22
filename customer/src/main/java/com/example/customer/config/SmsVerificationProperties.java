package com.example.customer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "sms.verification")
public class SmsVerificationProperties {

}
