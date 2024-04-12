package com.amit.customMethods;

import org.springframework.core.convert.converter.Converter;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Base64;

public class MultipartFileToStringConverter implements Converter<MultipartFile, String> {

    @Override
    public String convert(MultipartFile multipartFile) {
        try {
            byte[] bytes = multipartFile.getBytes();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            // Handle exception (e.g., log error)
            return null;
        }
    }
}

