package com.jvdw.product.unit.api;

import com.jvdw.product.api.ProductRestService;
import com.jvdw.product.api.dto.RestDto;
import com.jvdw.product.business.ProductBusinessResponse;
import com.jvdw.product.business.ProductBusinessService;
import com.jvdw.product.model.ProductModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * This unit test suite tests the API layer of the product module
 * No ApplicationContext is used here
 * Dependencies are mocked with Mockito
 */
public class ProductApiUnitTests
{
    // The REST service (REST Controller in Spring terms) to be tested
    ProductRestService restService;

    // This business service is used to simulate business logic and will be mocked
    ProductBusinessService businessService;

    // This BindingResult is used to simulate validation and will be mocked
    BindingResult bindingResult;

    /**
     * This method runs before the test suite
     */
    @BeforeEach
    void initUseCase()
    {
        // initialize REST service
        restService = new ProductRestService();
        // mock validation
        bindingResult = Mockito.mock(BindingResult.class);
        // mock business logic
        businessService = Mockito.mock(ProductBusinessService.class);
        // attach mocked business logic service to the REST service being tested
        restService.setService(businessService);
    }

    /**
     * Test API layer logic for stocking a valid product
     */
    @Test
    public void stockProductWithValidProduct()
    {
        // use the static when from mockito to control the return of mock object's methods
        // stock business logic method always returns a successful outcome
        when(businessService.stock(any(ProductModel.class))).thenReturn(ProductBusinessResponse.STOCKED);
        // validation passes in this test
        when(bindingResult.hasErrors()).thenReturn(false);

        // call REST service handleStock method
        ResponseEntity<RestDto<ProductModel>> responseEntity = restService.handleStock(new ProductModel("test_id"), bindingResult);

        // assertions
        // assert CREATED status code
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        // assert that the stocked Product is included in the response data
        assertEquals("test_id", responseEntity.getBody().getData().get(0).getId());
    }

    /**
     * Test API layer logic for attempting to stock an invalid product
     */
    @Test
    public void stockProductWithInvalidProduct()
    {
        // use the static when from mockito to control the return of mock object's methods
        // stock business logic method always returns a successful outcome
        when(businessService.stock(any(ProductModel.class))).thenReturn(ProductBusinessResponse.STOCKED);
        // validation fails in this test
        when(bindingResult.hasErrors()).thenReturn(true);

        // call REST service handleStock method
        ResponseEntity<RestDto<ProductModel>> responseEntity = restService.handleStock(new ProductModel(), bindingResult);

        // assertions
        // assert BAD REQUEST status code
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        // assert that the response data is null
        assertEquals(null, responseEntity.getBody().getData());
    }

    /**
     * Test that an existing product can be retrieved
     */
    @Test
    public void retrieveProductThatExists()
    {
        // use the static when from mockito to control the return of mock object's methods
        // retrieve one business logic method always returns a ProductModel to represent the product found
        when(businessService.retrieveOne(any(ProductModel.class))).thenReturn(new ProductModel("test_id"));
        // call REST service handleGetOne method
        ResponseEntity<RestDto<ProductModel>> entity = restService.handleGetOne("test_id");

        // assertions
        // assert OK status code
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        // assert that the requested Product is included in the response data
        assertEquals("test_id", entity.getBody().getData().get(0).getId());
    }

    /**
     * Test that a non-existent product cannot be retrieved
     */
    @Test
    public void retrieveProductThatDoesNotExist()
    {
        // use the static when from mockito to control the return of mock object's methods
        // retrieve one business logic method always returns null to signify that no product was found
        when(businessService.retrieveOne(any(ProductModel.class))).thenReturn(null);
        // call REST service handleGetOne method
        ResponseEntity<RestDto<ProductModel>> responseEntity = restService.handleGetOne("");
        // assertions
        // assert NOT FOUND status code
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        // assert that the response data is null
        assertEquals(null, responseEntity.getBody().getData());
    }

    /**
     * Test that all products can be retrieved
     */
    @Test
    public void retrieveAllProduct()
    {
        // use the static when from mockito to control the return of mock object's methods
        // retrieve all business logic method always returns ArrayList to signify successful retrieval of all products
        when(businessService.retrieveAll()).thenReturn(new ArrayList<>());

        // call REST service handleGetOne method
        ResponseEntity<RestDto<ProductModel>> responseEntity = restService.handleGetAll();

        // assertions
        // assert OK status code
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // assert that the response data is of type ArrayList
        assertEquals(ArrayList.class, responseEntity.getBody().getData().getClass());
    }

    /**
     * Test that an existing product's info can be updated with valid input
     */
    @Test
    public void correctProductThatExistsWithValidProduct()
    {
        // use the static when from mockito to control the return of mock object's methods
        // validation passes in this test
        when(bindingResult.hasErrors()).thenReturn(false);
        // when the correct() business logic method is called, indicate a successful operation
        when(businessService.correct(any(ProductModel.class))).thenReturn(ProductBusinessResponse.CORRECTED);

        // call REST service handleCorrect method
        ResponseEntity<RestDto<ProductModel>> responseEntity = restService.handleCorrect(new ProductModel("test_id"), bindingResult);

        // assertions
        // assert OK status code
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // assert the that the corrected product is included in the response data
        assertEquals("test_id", responseEntity.getBody().getData().get(0).getId());
    }

    /**
     * Test that an existing product can't be corrected with invalid data
     */
    @Test
    public void correctProductWithInvalidProduct()
    {
        // use the static when from mockito to control the return of mock object's methods
        // validation fails in this test
        when(bindingResult.hasErrors()).thenReturn(true);
        // the correct() business logic method will indicate success if called - shouldn't get called
        when(businessService.correct(any(ProductModel.class))).thenReturn(ProductBusinessResponse.CORRECTED);

        // call REST service handleCorrect method
        ResponseEntity<RestDto<ProductModel>> responseEntity = restService.handleCorrect(new ProductModel(), bindingResult);

        // assertions
        // assert BAD REQUEST status code
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        // assert response data is null
        assertEquals(null, responseEntity.getBody().getData());
    }

    /**
     * Test that a non-existent product cannot be corrected
     */
    @Test
    public void correctProductThatDoesNotExist()
    {
        // use the static when from mockito to control the return of mock object's methods
        // validation succeeds in this test
        when(bindingResult.hasErrors()).thenReturn(false);
        // correct() business logic method indicates that the requested product could not be found
        when(businessService.correct(any(ProductModel.class))).thenReturn(ProductBusinessResponse.NOT_FOUND);

        // call REST service handleCorrect method
        ResponseEntity<RestDto<ProductModel>> responseEntity = restService.handleCorrect(new ProductModel(), bindingResult);

        // assertions
        // assert NOT FOUND status code
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        // assert response data is null
        assertEquals(null, responseEntity.getBody().getData());
    }

    /**
     * Test that existing product can be unstocked
     */
    @Test
    public void unstockProductThatExists()
    {
        // use the static when from mockito to control the return of mock object's methods
        // the business logic method unstock() indicates a successful operation in this test
        when(businessService.unstock(any(ProductModel.class))).thenReturn(ProductBusinessResponse.UNSTOCKED);

        // call REST service handleUnstock method
        ResponseEntity<RestDto<ProductModel>> responseEntity = restService.handleUnstock("");

        // assertions
        // assert OK status code
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // assert the response data is null
        assertEquals(null, responseEntity.getBody().getData());
    }

    /**
     * Test that non-existent product cannot be unstocked
     */
    @Test
    public void unstockProductThatDoesNotExist()
    {
        // use the static when from mockito to control the return of mock object's methods
        // the business logic method unstock() indicates that the requested product could not be found in this test
        when(businessService.unstock(any(ProductModel.class))).thenReturn(ProductBusinessResponse.NOT_FOUND);

        // call REST service handleUnstock method
        ResponseEntity<RestDto<ProductModel>> responseEntity = restService.handleUnstock("");

        // assertions
        // assert NOT FOUND status code
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        // assert the response data is null
        assertEquals(null, responseEntity.getBody().getData());
    }
}
