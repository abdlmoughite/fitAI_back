package com.hessati.hessati.services;

import com.hessati.hessati.dto.ProductDTO;
import com.hessati.hessati.entities.Category;
import com.hessati.hessati.entities.Images;
import com.hessati.hessati.entities.Product;
import com.hessati.hessati.repositories.CategoryRepository;
import com.hessati.hessati.repositories.ImagesRepository;
import com.hessati.hessati.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImagesRepository imagesRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public Page<Product> getAllProducts(Pageable pageable) {
        Pageable sortedByCreatedAt = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("id").descending()
        );
        return productRepository.findAll(sortedByCreatedAt);
    }

    public Page<Product> getProductsByCategory(String categoryName, Pageable pageable) {
        Pageable sortedByCreatedAt = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("id").descending()
        );
        return productRepository.findByCategory_NameOrderByIdDesc(categoryName, sortedByCreatedAt);
    }

    public Product createProduct(ProductDTO productDTO, List<MultipartFile> images) {
        Category category = categoryRepository.findById(productDTO.getCategory().getId()).orElse(null);
        if (category == null) return null;
        Product product = new Product();
        product.setTitle(productDTO.getTitle());
        product.setDescription(productDTO.getDescription());
        product.setCategory(category);
        return productRepository.save(product);
    }

    public Product updateProduct(ProductDTO productDTO, List<MultipartFile> newImages) {
        Optional<Product> optionalProduct = productRepository.findById(productDTO.getId());
        if (!optionalProduct.isPresent()) return null;

        Product product = optionalProduct.get();
        product.setTitle(productDTO.getTitle());
        product.setDescription(productDTO.getDescription());

        Category category = categoryRepository.findById(productDTO.getCategory().getId()).orElse(null);
        if (category != null) {
            product.setCategory(category);
        }
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getLastThreeProducts() {
        return productRepository.findTop3ByOrderByIdDesc();
    }

    public List<Product> getLastThreeRadiologieProducts() {
        return productRepository.findTop3ByCategory_NameOrderByIdDesc("Radiologie");
    }

    public boolean deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            return false;
        }
        productRepository.deleteById(id);
        return true;
    }
}
