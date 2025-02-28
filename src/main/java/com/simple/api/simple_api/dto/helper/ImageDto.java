package com.simple.api.simple_api.dto.helper;

import lombok.Data;

@Data
public class ImageDto {

    private Long imageId;

    private String imageName;

    private String downloadURL;
    
}