package com.cart.shoppy.service.image;

import com.cart.shoppy.dto.ImageDto;
import com.cart.shoppy.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(Long productId, List<MultipartFile> files);
    ImageDto updateImage(MultipartFile file, Long imageId);
}
