package com.jvdw.product.integration;

import com.jvdw.product.api.dto.RestDto;
import com.jvdw.product.data.entity.ProductEntity;
import com.jvdw.product.data.repository.ProductRepository;
import com.jvdw.product.model.ProductModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * This integration test suite tests the application end-2-end
 * the @SpringBootTest annotation will bootstrap an ApplicationContext
 * (allowing the use of dependency injection and fully-fledged Spring beans)
 * and run the app on a random port
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class ProductIntegrationTests
{
    // capture the random port used
    @LocalServerPort
    int port;

    // TestRestTemplate that will be used to send requests to the application
    // NOTE not using WebClient here because I don't want webflux on the classpath
    TestRestTemplate restTemplate = new TestRestTemplate();

    // The HTTP header to send with requests
    HttpHeaders headers = new HttpHeaders();

    // use dependency injection to inject a repository bean at runtime
    // the database will be hit in these E2E tests
    @Autowired
    private ProductRepository repository;

    /**
     * Test that a valid product can be stocked
     */
    @Test
    public void stockProductWithValidProduct()
    {
        // instantiate a ProductModel to send with request
        ProductModel product = new ProductModel("The Hobbit", "Bilbo finds the balance between Took and Baggins", BigDecimal.valueOf(14.99), 14);
        // instantiate an HttpEntity to send with request
        HttpEntity<ProductModel> entity = new HttpEntity<>(product, headers);
        // send request and capture response
        ResponseEntity<RestDto> response = restTemplate.exchange(
                createURLWithPort("/product"), HttpMethod.POST, entity, RestDto.class);

        // assertions
        // assert CREATED response status code
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        // assert response data first model is not null
        assertNotEquals(null, response.getBody().getData().get(0));
    }

    /**
     * Test that invalid product can't be stocked
     */
    @Test
    public void stockProductWithInvalidProduct()
    {
        // instantiate a ProductModel to send with request
        ProductModel product = new ProductModel("", "Bilbo finds the balance between Took and Baggins", BigDecimal.valueOf(14.99), 14);
        // instantiate an HttpEntity to send with request
        HttpEntity<ProductModel> entity = new HttpEntity<>(product, headers);
        // send request and capture response
        ResponseEntity<RestDto> response = restTemplate.exchange(
                createURLWithPort("/product"), HttpMethod.POST, entity, RestDto.class);

        // assertions
        // assert BAD REQUEST response status code
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // assert response data is null
        assertEquals(null, response.getBody().getData());
    }

    /**
     * Test that an existing product can be retrieved
     */
    @Test
    public void retrieveProductThatExists()
    {
        // instantiate an HttpEntity to send with request
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        // send request and capture response
        ResponseEntity<RestDto> response = restTemplate.exchange(
                createURLWithPort("/product/63c3034e9399564091f8c722"), HttpMethod.GET, entity, RestDto.class);

        // assertions
        // assert OK response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // assert response data first model is not null
        assertNotEquals(null, response.getBody().getData().get(0));
    }

    /**
     * Test that a non-existent product can not be retrieved
     */
    @Test
    public void retrieveProductThatDoesNotExist()
    {
        // instantiate an HttpEntity to send with request
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        // send request and capture response
        ResponseEntity<RestDto> response = restTemplate.exchange(
                createURLWithPort("/product/620c8c44e136fd50c99323benotrealid"), HttpMethod.GET, entity, RestDto.class);

        // assertions
        // assert NOT FOUND response status code
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // assert response data is null
        assertEquals(null, response.getBody().getData());
    }


    /**
     * Test that all products can be retrieved from the inventory
     */
    @Test
    public void retrieveAllProduct()
    {
        // instantiate an HttpEntity to send with request
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        // send request and capture response
        ResponseEntity<RestDto> response = restTemplate.exchange(
                createURLWithPort("/product"), HttpMethod.GET, entity, RestDto.class);

        // assertions
        // assert OK response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // assert response data type is ArrayList
        assertEquals(ArrayList.class, response.getBody().getData().getClass());
    }

    /**
     * Test that an existing product's info can be corrected with valid input
     */
    @Test
    public void correctProductThatExistsWithValidProduct()
    {
        // instantiate a ProductModel to send with request
        ProductModel product = new ProductModel("63c3034e9399564091f8c722","Name Corrected by Integration Test", "Featuring Tom Bombadil", BigDecimal.valueOf(19.54), 22);
        // instantiate an HttpEntity to send with request
        HttpEntity<ProductModel> entity = new HttpEntity<>(product, headers);
        // send request and capture response
        ResponseEntity<RestDto> response = restTemplate.exchange(
                createURLWithPort("/product"), HttpMethod.PUT, entity, RestDto.class);

        // assertions
        // assert OK response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // assert the corrected product is not null
        assertNotEquals(null, response.getBody().getData().get(0));
        // assert that the edit was made
        assertEquals("Name Corrected by Integration Test", ((LinkedHashMap)response.getBody().getData().get(0)).get("name"));
    }

    /**
     * Test that an existing product's info cannot be corrected with invalid input
     */
    @Test
    public void correctProductWithInvalidProduct()
    {
        // instantiate a ProductModel to send with request
        ProductModel product = new ProductModel("620c8c44e136fd50c99323be","", "Featuring Tom Bombadil", BigDecimal.valueOf(19.54), 22);
        // instantiate an HttpEntity to send with request
        HttpEntity<ProductModel> entity = new HttpEntity<>(product, headers);
        // send request and capture response
        ResponseEntity<RestDto> response = restTemplate.exchange(
                createURLWithPort("/product"), HttpMethod.PUT, entity, RestDto.class);

        // assertions
        // assert BAD REQUEST response status code
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // assert that the response data is null
        assertEquals(null, response.getBody().getData());
    }

    /**
     * Test that invalid product can't be corrected
     */
    @Test
    public void correctProductThatDoesNotExist()
    {
        // instantiate a ProductModel to send with request
        ProductModel product = new ProductModel("notarealid","The Lord of the Rings", "Featuring Tom Bombadil", BigDecimal.valueOf(19.54), 22);
        // instantiate an HttpEntity to send with request
        HttpEntity<ProductModel> entity = new HttpEntity<>(product, headers);
        // send request and capture response
        ResponseEntity<RestDto> response = restTemplate.exchange(
                createURLWithPort("/product"), HttpMethod.PUT, entity, RestDto.class);

        // assertions
        // assert NOT FOUND response status code
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // assert the response data is null
        assertEquals(null, response.getBody().getData());
    }

    /**
     * Test that an existing product can be unstocked
     */
    @Test
    public void unstockProductThatExists()
    {
        repository.save(new ProductEntity("test_id", "test_name", "test_description", BigDecimal.valueOf(5.99), 20));
        // instantiate an HttpEntity to send with request
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        // send request and capture response
        ResponseEntity<RestDto> response = restTemplate.exchange(
                createURLWithPort("/product/test_id"), HttpMethod.DELETE, entity, RestDto.class);

        // assertions
        // assert OK response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // assert the response data is null
        assertEquals(null, response.getBody().getData());
    }

    /**
     * Test that a non-existent product can't be unstocked
     */
    @Test
    public void unstockProductThatDoesNotExist()
    {
        // instantiate an HttpEntity to send with request
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        // send request and capture response
        ResponseEntity<RestDto> response = restTemplate.exchange(
                createURLWithPort("/product/abc"), HttpMethod.DELETE, entity, RestDto.class);

        // assertions
        // assert NOT FOUND response status code
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // assert the response data is null
        assertEquals(null, response.getBody().getData());
    }

    /**
     * This testing utility method builds a full URL from a request URI
     * @param uri the URI to send request to
     * @return String full URL built from URI
     */
    private String createURLWithPort(String uri)
    {
        // build and return URL
        return "http://localhost:" + port + uri;
    }
}
