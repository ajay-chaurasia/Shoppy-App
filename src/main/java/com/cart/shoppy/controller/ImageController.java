package com.cart.shoppy.controller;

import com.cart.shoppy.dto.ImageDto;
import com.cart.shoppy.exceptions.EntityNotFoundException;
import com.cart.shoppy.model.Image;
import com.cart.shoppy.response.ApiResponse;
import com.cart.shoppy.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {
        try {
            List<ImageDto> imageDTOs = imageService.saveImages(productId, files);
            return ResponseEntity.ok(new ApiResponse("Upload successful!", imageDTOs));
        } catch (Exception exception) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload failed!", exception.getMessage()));
        }
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<ApiResponse> downloadImageById(@PathVariable Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            return ResponseEntity.ok(new ApiResponse("Found!", image));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(exception.getMessage(), null));
        }
    }

    @PutMapping("/{imageId}")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestParam MultipartFile file) {
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null) {
                ImageDto imageDto = imageService.updateImage(file, imageId);
                return ResponseEntity.ok(new ApiResponse("Update successful!", imageDto));
            }
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Upload failed!", null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload failed!", INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null) {
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("Delete successful!", null));
            }
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Delete failed!", null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload failed!", INTERNAL_SERVER_ERROR));
    }
}
