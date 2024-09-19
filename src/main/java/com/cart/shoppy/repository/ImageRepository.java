package com.cart.shoppy.repository;

import com.cart.shoppy.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
