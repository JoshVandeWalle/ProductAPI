package com.jvdw.product.unit.business;

import com.jvdw.product.business.ProductBusinessResponse;
import com.jvdw.product.business.ProductBusinessService;
import com.jvdw.product.data.entity.ProductEntity;
import com.jvdw.product.data.repository.ProductRepository;
import com.jvdw.product.model.ProductModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * This unit test suite tests the business logic layer of the product module
 * No ApplicationContext is used here
 * Dependencies are mocked with Mockito
 */
public class ProductBusinessUnitTests
{
    // The business service to be tested
    ProductBusinessService service;

    // the repository used to simulate database interactions this will be mocked
    ProductRepository repository;

    /**
     * This method runs before the test suite
     */
    @BeforeEach
    void initUseCase()
    {
        // initialize service to be tested
        service = new ProductBusinessService();
        // mock repository
        repository = Mockito.mock(ProductRepository.class);
        // attach mocked repository to service
        service.setRepository(repository);

        // use the static when from mockito to control the return of mock object's methods
        // findById repository method always returns a ProductEntity for the ID '620c8c44e136fd50c99323be' indicating that the product exists in the database
        when(repository.findById("620c8c44e136fd50c99323be")).thenReturn(Optional.of(new ProductEntity("620c8c44e136fd50c99323be", "The Lord of the Rings", "Featuring Tom Bombadil", BigDecimal.valueOf(14.99), 22)));
        // findById repository method always returns a ProductEntity for the ID 'notarealid' indicating that the product does not exist in the database
        when(repository.findById("notarealid")).thenReturn(Optional.ofNullable(null));
    }

    /**
     * Test that a new product can be stocked
     */
    @Test
    public void stockProduct()
    {
        // use the static when from mockito to control the return of mock object's methods
        // repository save method should return a product entity in this test indicating the product was saved
        when(repository.save(any(ProductEntity.class))).thenReturn(new ProductEntity());

        // call the business service method and capture the return value
        ProductBusinessResponse businessResponse = service.stock(new ProductModel());

        // assertions
        // assert the STOCKED outcome is returned
        assertEquals(ProductBusinessResponse.STOCKED, businessResponse);
    }

    /**
     * Test an existing product can be retrieved
     */
    @Test
    public void retrieveOneProductThatExists()
    {
        // initialize object to hold the ID expected to be in the database
        String existingProductId = "620c8c44e136fd50c99323be";

        // call the business service method and capture the return value
        ProductModel product = service.retrieveOne(new ProductModel(existingProductId));

        // assertions
        // assert the passed product ID matches the returned product ID
        assertEquals(existingProductId, product.getId());
    }

    /**
     * Test that a non-existent product cannot be retrieved
     */
    @Test
    public void retrieveOneProductThatDoesNotExist()
    {
        // call the business service method and capture the return value
        ProductModel product = service.retrieveOne(new ProductModel("notarealid"));

        // assertions
        // assert that no product is returned
        assertEquals(null, product);
    }

    /**
     * Test all products can be retrieved from the inventory
     */
    @Test
    public void retrieveAllProduct()
    {
        // use the static when from mockito to control the return of mock object's methods
        // repository findAll method should return an ArrayList in this test indicating the product was saved
        when(repository.findAll()).thenReturn(new ArrayList<>());

        // call the business service method and capture the return value
        List<ProductModel> products = service.retrieveAll();

        // assertions
        // assert that an ArrrayList is returned
        assertEquals(ArrayList.class, products.getClass());
    }

    /**
     * Test that a product that exists can have its info corrected
     */
    @Test
    public void correctProductThatExists()
    {
        // use the static when from mockito to control the return of mock object's methods
        // repository save method should return a product entity in this test indicating the product was saved
        when(repository.save(any(ProductEntity.class))).thenReturn(new ProductEntity());

        // call the business service method and capture the return value
        ProductBusinessResponse businessResponse = service.correct(new ProductModel("620c8c44e136fd50c99323be"));

        // assertions
        // assert that the CORRECTED is returned
        assertEquals(ProductBusinessResponse.CORRECTED, businessResponse);
    }

    /**
     * Test that a product that does not exist cannot have its info corrected
     */
    @Test
    public void correctProductThatDoesNotExist()
    {
        // use the static when from mockito to control the return of mock object's methods
        // repository save method should return a product entity in this test indicating the product was saved
        // the save() method shouldn't be called if the test goes as expected
        when(repository.save(any(ProductEntity.class))).thenReturn(new ProductEntity());
        // call the business service method and capture the return value
        ProductBusinessResponse businessResponse = service.correct(new ProductModel("notarealid"));

        // assertions
        // assert that the NOT FOUND is returned
        assertEquals(ProductBusinessResponse.NOT_FOUND, businessResponse);
    }

    /**
     * Test that a product that exists can be unstocked
     */
    @Test
    public void unstockProductThatExists()
    {
        // call the business service method and capture the return value
        ProductBusinessResponse businessResponse = service.unstock(new ProductModel("620c8c44e136fd50c99323be"));

        // assertions
        // assert that the UNSTOCKED is returned
        assertEquals(ProductBusinessResponse.UNSTOCKED, businessResponse);
    }

    /**
     * Test that a product that does not exist cannot be unstocked
     */
    @Test
    public void unstockProductThatDoesNotExist()
    {
        // call the business service method and capture the return value
        ProductBusinessResponse businessResponse = service.unstock(new ProductModel("notarealid"));

        // assertions
        // assert that the NOT FOUND is returned
        assertEquals(ProductBusinessResponse.NOT_FOUND, businessResponse);
    }
}
