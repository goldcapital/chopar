package com.example.fie_service.util;

import com.google.common.io.ByteStreams;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ofPattern;

@Slf4j
public class FilesUtils {
    public static byte[] toByteArray(InputStream file) {
        try (InputStream is = file) {
            return ByteStreams.toByteArray(is);
        } catch (IOException e) {
            log.error("Cannot convert to byte array -> {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static String createPath(String fileName) {
        var date = LocalDate.now().format(ofPattern("yyyy/MM/dd"));
        return date + "/" + fileName;
    }

    public static String getFileName(String originalName) {
        return System.currentTimeMillis() + originalName;
    }
}
