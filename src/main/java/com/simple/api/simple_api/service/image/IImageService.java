package com.simple.api.simple_api.service.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.simple.api.simple_api.dto.response.ImageDto;
import com.simple.api.simple_api.model.Image;

public interface IImageService {
    
    Image getIMageById(Long id);

    void deleteImageById(Long id);

    List<ImageDto> saveImages(List<MultipartFile> files, Long productId);

    void updateImage(MultipartFile file, Long imageId);

}
