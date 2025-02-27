package com.simple.api.simple_api.service.image;

import org.springframework.web.multipart.MultipartFile;

import com.simple.api.simple_api.model.Image;

public interface IImageService {
    
    Image getIMageById(Long id);

    void deleteImageById(Long id);

    Image saveImage(MultipartFile file, Long productId);

    void updateImage(MultipartFile file, Long imageId);

}
