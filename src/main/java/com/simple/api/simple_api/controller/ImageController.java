package com.simple.api.simple_api.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.simple.api.simple_api.dto.helper.ImageDto;
import com.simple.api.simple_api.dto.response.ApiResponse;
import com.simple.api.simple_api.exception.ResponseNotFoundException;
import com.simple.api.simple_api.model.Image;
import com.simple.api.simple_api.service.image.IImageService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
    
    private final IImageService imageService;


    @PostMapping("/upload/{productId}")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @PathVariable("productId") Long productId){
        try {
            List<ImageDto> imageDtos = imageService.saveImages(files, productId);
            return ResponseEntity.ok(new ApiResponse("Upload success!!", imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload failed!!", e.getMessage()));
        }
    }


    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable("imageId") Long imageId) throws SQLException {
            Image image = imageService.getIMageById(imageId);

            ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
            .header(HttpHeaders.CONTENT_DISPOSITION, "Attachment:  filename=\"" +image.getFileName() + "\"")
            .body(resource);
    }


    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable("imageId") Long imageId, @RequestBody MultipartFile file) {
        try {
            Image image = imageService.getIMageById(imageId);
            if (image != null) {  
                imageService.updateImage(file, imageId);
              return ResponseEntity.ok(new ApiResponse("Updated success", image));
            }  
        } catch (ResponseNotFoundException e) {  
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Image not found!", HttpStatus.NOT_FOUND));
        }  
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Update failed!", HttpStatus.INTERNAL_SERVER_ERROR));
    }
    

     @DeleteMapping("/image/{imageId}/delete")
     public ResponseEntity<ApiResponse> deleteImage(@PathVariable("imageId") Long imageId){
        try {
            Image image = imageService.getIMageById(imageId);
            if (image != null) {
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("deleted success", image));
            }
        } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("image not found", HttpStatus.NOT_FOUND));
            }
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("updated failed", HttpStatus.INTERNAL_SERVER_ERROR));
     }





}
