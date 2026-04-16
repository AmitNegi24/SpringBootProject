package com.amit.service;

import com.amit.dto.ProductDTO;
import com.amit.entity.Product;
import com.amit.exception.EKartException;
import com.amit.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.bson.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void contextLoads() {
    }

    @Test
    public void getProductByIdValidTestCase() throws Exception {
        Product product = new Product();
        product.setProductId(101);

        when(productRepository.findByProductId(101))
                .thenReturn(Optional.of(product));

        ProductDTO result = productService.getProductById(101);

        assertNotNull(result);
        assertEquals(101, result.getProductId());
    }

    @Test
    public void getProductByIdInValidTestCase() {
        when(productRepository.findByProductId(1))
                .thenReturn(Optional.empty());

        assertThrows(EKartException.class, () -> {
            productService.getProductById(1);
        });
    }

    @Test
    public void getAllProductsValidTestCase() throws EKartException {

        Product product = new Product();
        product.setProductId(1);
        product.setName("Phone");
        product.setPrice(10000.0);
        product.setBrand("Apple");

        List<Product> productList = List.of(product);

        when(productRepository.findAll(any(Sort.class)))
                .thenReturn(productList);

        List<ProductDTO> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getProductId());
    }

    @Test
    public void getAllProductsInValidTestCase() {

        when(productRepository.findAll(any(Sort.class)))
                .thenReturn(Collections.emptyList());

        assertThrows(EKartException.class, () -> {
            productService.getAllProducts();
        });
    }

    @Test
    public void createProductValidTestCase() throws EKartException {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(101);
        when(productRepository.findByProductId(101)).thenReturn(Optional.empty());

        Product product = new Product();
        product.setProductId(101);

        when(productRepository.save(any())).thenReturn(product);

        ProductDTO result = productService.createProduct(productDTO);

        assertNotNull(result);
        assertEquals(101, result.getProductId());
    }

    @Test
    public void createProductAlreadyExistTestCase(){
        Product existingProduct = new Product();
        existingProduct.setProductId(101);
        when(productRepository.findByProductId(101)).thenReturn(Optional.of(existingProduct));

        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(101);

        assertThrows(EKartException.class,()->{
            productService.createProduct(productDTO);
        });

    }

    @Test
    public void updateProductValidTestCase() throws EKartException {
        Product existingProduct = new Product();
        existingProduct.setProductId(101);
        existingProduct.setName("Phone");
        existingProduct.setPrice(10000.0);

        when(productRepository.findByProductId(101)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any())).thenReturn(existingProduct);

        ProductDTO productDTO = new ProductDTO();

        productDTO.setProductId(101);
        productDTO.setName("Phone");
        productDTO.setPrice(10000.0);

        ProductDTO result = productService.updateProduct(101,productDTO);

        assertNotNull(result);
    }

    @Test
    public void updateProductInvalidTestCase(){
        Product existingProduct = new Product();
        existingProduct.setProductId(101);
        when(productRepository.findByProductId(existingProduct.getProductId())).thenReturn(Optional.empty());

        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(101);

        assertThrows(EKartException.class,()->{
            productService.updateProduct(existingProduct.getProductId(),productDTO);
        });
    }

    @Test
    public void deleteProductValidTestCase() throws EKartException {
        Product existingProduct = new Product();
        existingProduct.setProductId(101);

        when(productRepository.findByProductId(101)).thenReturn(Optional.of(existingProduct));

        productService.deleteProduct(101);

        verify(productRepository).deleteByProductId(101);


    }

    @Test
    public void deleteProducIntValidTestCase(){
        Product existingProduct = new Product();
        existingProduct.setProductId(101);

        when(productRepository.findByProductId(101)).thenReturn(Optional.empty());

        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(101);

        assertThrows(EKartException.class,()->{
            productService.deleteProduct(101);
        });
    }
    @Test
    public void reduceAvailableQuantityValidTestCase() throws EKartException {
        Product existingProduct = new Product();
        existingProduct.setProductId(101);
        existingProduct.setAvailableQuantity(10);

        when(productRepository.findByProductId(101)).thenReturn(Optional.of(existingProduct));

        productService.reduceAvailableQuantity(101,3);

        assertEquals(7,existingProduct.getAvailableQuantity());
        verify(productRepository).save(existingProduct);
    }

    @Test
    public void reduceAvailableQuantityInValidTestCase() throws EKartException {
        Product existingProduct = new Product();
        existingProduct.setProductId(101);
        existingProduct.setAvailableQuantity(10);

        when(productRepository.findByProductId(101)).thenReturn(Optional.of(existingProduct));

        assertThrows(EKartException.class,()->{
            productService.reduceAvailableQuantity(101,15);
        });
        verify(productRepository, never()).save(any());
    }

    @Test
    void reduceAvailableQuantity_NegativeQuantity() {

        Product product = new Product();
        product.setProductId(101);
        product.setAvailableQuantity(10);

        when(productRepository.findByProductId(101))
                .thenReturn(Optional.of(product));

        assertThrows(EKartException.class, () -> {
            productService.reduceAvailableQuantity(101, -5);
        });
    }

}

