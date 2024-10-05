package com.cart.shoppy.controller;

import com.cart.shoppy.dto.ProductDto;
import com.cart.shoppy.exceptions.EntityNotFoundException;
import com.cart.shoppy.model.Product;
import com.cart.shoppy.request.AddProductRequest;
import com.cart.shoppy.request.ProductUpdateRequest;
import com.cart.shoppy.response.ApiResponse;
import com.cart.shoppy.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND)
                        .body(new ApiResponse("No product found!", null));
            }
            List<ProductDto> productDTOs = productService.getAllProductDTOs(products);
            return ResponseEntity.ok(new ApiResponse("Success!", productDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error!", e.getMessage()));
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            ProductDto productDto = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Product found!", productDto));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Product not found!", null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            Product newProduct = productService.addProduct(product);
            ProductDto newProductDto = productService.convertToDto(newProduct);
            return ResponseEntity.ok(new ApiResponse("Success!", newProductDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductUpdateRequest product
    ) {
        try {
            Product newProduct = productService.updateProduct(product, productId);
            ProductDto newProductDto = productService.convertToDto(newProduct);
            return ResponseEntity.ok(new ApiResponse("Success!", newProductDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Delete successful!", productId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/params/name-and-brand")
    public ResponseEntity<ApiResponse> getProductByNameAndBrand(
            @RequestParam String productName,
            @RequestParam String brandName
    ) {
        try {
            List<Product> products = productService.getProductsByBrandAndName(brandName, productName);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND)
                        .body(new ApiResponse("No product found!", null));
            }
            List<ProductDto> productDTOs = productService.getAllProductDTOs(products);
            return ResponseEntity.ok(new ApiResponse("Product found!", productDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error!", e.getMessage()));
        }
    }

    @GetMapping("/params/category-and-brand")
    public ResponseEntity<ApiResponse> getAllProductByCategoryAndBrand(
            @RequestParam String categoryName,
            @RequestParam String brandName
    ) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(categoryName, brandName);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND)
                        .body(new ApiResponse("No product found!", null));
            }
            List<ProductDto> productDTOs = productService.getAllProductDTOs(products);
            return ResponseEntity.ok(new ApiResponse("Product found!", productDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error!", e.getMessage()));
        }
    }

    @GetMapping("/params/product")
    public ResponseEntity<ApiResponse> getAllProductsByName(@RequestParam String productName) {
        try {
            List<Product> products = productService.getProductsByName(productName);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND)
                        .body(new ApiResponse("No product found!", null));
            }
            List<ProductDto> productDTOs = productService.getAllProductDTOs(products);
            return ResponseEntity.ok(new ApiResponse("Product found!", productDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error!", e.getMessage()));
        }
    }

    @GetMapping("/params/brand")
    public ResponseEntity<ApiResponse> findProductsByBrand(@RequestParam String brandName) {
        try {
            List<Product> products = productService.getProductsByBrand(brandName);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND)
                        .body(new ApiResponse("No product found!", null));
            }
            List<ProductDto> productDTOs = productService.getAllProductDTOs(products);
            return ResponseEntity.ok(new ApiResponse("Product found!", productDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error!", e.getMessage()));
        }
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<ApiResponse> findProductsByCategory(@PathVariable String categoryName) {
        try {
            List<Product> products = productService.getProductsByCategory(categoryName);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND)
                        .body(new ApiResponse("No product found!", null));
            }
            List<ProductDto> productDTOs = productService.getAllProductDTOs(products);
            return ResponseEntity.ok(new ApiResponse("Product found!", productDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error!", e.getMessage()));
        }
    }

    @GetMapping("/product-count/brand-and-name")
    public ResponseEntity<ApiResponse> countProductsByNameAndBrand(
            @RequestParam String categoryName,
            @RequestParam String brandName
    ) {
        try {
            Long count = productService.countProductsByBrandAndName(brandName, categoryName);
            return ResponseEntity.ok(new ApiResponse("There are " + count + "product(s) available.", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error!", e.getMessage()));
        }
    }
}
