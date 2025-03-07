package com.simple.api.simple_api.dto.response;

import lombok.Data;

@Data
public class ImageDto {

    private Long imageId;

    private String imageName;

    private String downloadURL;
    
}