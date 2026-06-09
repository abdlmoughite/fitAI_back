package com.hessati.hessati.controllers;

import com.hessati.hessati.dto.ProductDTO;
import com.hessati.hessati.entities.Product;
import com.hessati.hessati.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @PageableDefault(size = 10) Pageable pageable) { // default 10 per page
        Page<Product> products = productService.getAllProducts(pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/by-category/{categoryName}")
    public ResponseEntity<Page<Product>> getProductsByCategory(@PathVariable String categoryName,
                           @PageableDefault(size = 10) Pageable pageable) { // default 10 per page
        Page<Product> products = productService.getProductsByCategory(categoryName, pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/last-three")
    public ResponseEntity<List<Product>> getLastThreeProducts() { // default 10 per page
        List<Product> products = productService.getLastThreeProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/last-three-radiologie")
    public ResponseEntity<List<Product>> getLastThreeRadiologieProducts() { // default 10 per page
        List<Product> products = productService.getLastThreeRadiologieProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> createProduct(
            @RequestPart("product") ProductDTO productDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        Product createdProduct = productService.createProduct(productDTO, images);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return product != null
                ? new ResponseEntity<>(product, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> updateProduct(
            @RequestPart("product") ProductDTO productDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        Product createdProduct = productService.updateProduct(productDTO, images);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        return new ResponseEntity<>(deleted, deleted ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
