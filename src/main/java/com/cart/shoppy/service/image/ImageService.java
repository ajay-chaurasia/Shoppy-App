package com.cart.shoppy.service.image;

import com.cart.shoppy.dto.ImageDto;
import com.cart.shoppy.exceptions.EntityNotFoundException;
import com.cart.shoppy.model.Image;
import com.cart.shoppy.model.Product;
import com.cart.shoppy.repository.ImageRepository;
import com.cart.shoppy.service.product.IProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final IProductService productService;
    private final ModelMapper modelMapper;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No image available with Id: " + id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id)
                .ifPresentOrElse(
                        imageRepository::delete,
                        () -> {
                            throw new EntityNotFoundException("No image available with Id: " + id);
                        }
                );
    }

    @Override
    public List<ImageDto> saveImages(Long productId, List<MultipartFile> files) {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImageDTOs = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(file.getBytes());
                image.setProduct(product);

                Image savedImage = imageRepository.save(image);

                savedImage.setDownloadUrl(String.format("/api/v1/images/image/download/%s", savedImage.getId()));

                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDTOs.add(imageDto);

            } catch (IOException exception) {
                throw new RuntimeException(exception.getMessage());
            }
        }
        return savedImageDTOs;
    }

    @Override
    public ImageDto updateImage(MultipartFile file, Long imageId) {
        return Optional.ofNullable(getImageById(imageId))
                .map(existingImage -> {
                    existingImage.setFileName(file.getOriginalFilename());
                    existingImage.setFileType(file.getContentType());
                    try {
                        existingImage.setImage(file.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                    return existingImage;
                })
                .map(imageRepository::save)
                .map(savedImage -> {
                    ImageDto imageDto = new ImageDto();
                    imageDto.setId(savedImage.getId());
                    imageDto.setFileName(savedImage.getFileName());
                    imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                    return imageDto;
                })
                .orElseThrow(() -> new EntityNotFoundException("Image not found!"));
    }

    @Override
    public List<ImageDto> getImageDtoByProductId(Long productId) {
        List<Image> images = imageRepository.findByProductId(productId);
        List<ImageDto> imageDTOs = images.stream()
                .map((image) -> modelMapper.map(image, ImageDto.class))
                .toList();
        return imageDTOs;
    }
}
