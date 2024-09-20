package com.cart.shoppy.service.product;

import com.cart.shoppy.exceptions.EntityNotFoundException;
import com.cart.shoppy.model.Category;
import com.cart.shoppy.model.Product;
import com.cart.shoppy.repository.CategoryRepository;
import com.cart.shoppy.repository.ProductRepository;
import com.cart.shoppy.request.AddProductRequest;
import com.cart.shoppy.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product addProduct(AddProductRequest request) {
        /* check if the category is found in the DB
        If Yes, set it as the new product category
        If No, the save it as a new category
        The set as the new product category. */

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
//        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete, () -> { throw new EntityNotFoundException("Product not found!"); });
    }

    private Product createUpdatedProduct(Product existingProduct, ProductUpdateRequest request) {
        Category category = categoryRepository.findByName(request.getCategory().getName());
        return new Product(
                existingProduct.getId(),
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long id) {
        return productRepository.findById(id)
            .map(existingProduct -> createUpdatedProduct(existingProduct, request))
            .map(productRepository::save)
            .orElseThrow(() -> new EntityNotFoundException("Product not found!"));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
}
