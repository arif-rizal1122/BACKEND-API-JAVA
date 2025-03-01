package com.simple.api.simple_api.service.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.simple.api.simple_api.dto.helper.ImageDto;
import com.simple.api.simple_api.exception.ResponseNotFoundException;
import com.simple.api.simple_api.model.Image;
import com.simple.api.simple_api.model.Product;
import com.simple.api.simple_api.repository.ImageRepository;
import com.simple.api.simple_api.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{

    private final ImageRepository imageRepository;
    
    private final IProductService productService;

    @Override
    public Image getIMageById(Long id) {
        return imageRepository.findById(id).orElseThrow(
            () -> new ResponseNotFoundException("image is not found!! " + id)
        );
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, 
        () -> {
            new ResponseNotFoundException("image is not found!!" + id);
        });
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> saveImageDtos = new ArrayList<>();
    
        for (MultipartFile file : files) {
            try {
                // Buat objek Image baru
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);
    
                // Simpan pertama kali agar mendapatkan ID
                Image savedImage = imageRepository.save(image);
    
                // Set URL download dengan ID yang sudah ada
                String downloadURL = "/api/v1/images/image/download/" + savedImage.getId();
                savedImage.setDownloadURL(downloadURL);
    
                // Simpan ulang setelah downloadURL diperbarui
                imageRepository.save(savedImage);
    
                // Konversi ke DTO
                ImageDto imageDto = new ImageDto();
                imageDto.setImageId(savedImage.getId());
                imageDto.setImageName(savedImage.getFileName());
                imageDto.setDownloadURL(savedImage.getDownloadURL());
                saveImageDtos.add(imageDto);
    
            } catch (IOException | SQLException e) {
                throw new RuntimeException("Error saving image: " + e.getMessage());
            }
        }
        return saveImageDtos;
    }
    


    


    @Override
    public void updateImage(MultipartFile file, Long imageId) {
            Image image = getIMageById(imageId);
        try {
            image.setFileType(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
        
    }
    
}
