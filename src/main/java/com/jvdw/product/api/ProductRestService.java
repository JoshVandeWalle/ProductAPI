package com.jvdw.product.api;

import com.jvdw.product.api.dto.RestDto;
import com.jvdw.product.business.ProductBusinessInterface;
import com.jvdw.product.business.ProductBusinessResponse;
import com.jvdw.product.model.ProductModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * The ProductRestService is an API facade over the application's bussines logic
 * This class manages API behavior
 * @Setter used here to support Unit
 */
@RestController
@RequestMapping("/product")
@Tag(name = "product", description = "the Product API")
@Setter
public class ProductRestService
{
    // the business service class used to execute business logic
    @Autowired
    ProductBusinessInterface service;

    /**
     * Stocks a new product into the inventory
     * @param product the new product to be stocked
     * @param bindingResult the result of product validation
     * @return ResponseEntity a wrapper object including the response data, message, and status code
     */
    @PostMapping
    // @Operation is used to define this endpoint in automatically generated Swagger OAS 3 documentation
    @Operation(summary = "Stock a Product", description = "Add a new product to inventory. Do use to add quantity.", tags = { "product" })
    // @ApiResponses is used to define the set of status codes that may result when calling the endpoint in automatically generated Swagger OAS 3 documentation
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product stocked",
                content = @Content(schema = @Schema(implementation = RestDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid Product provided",
                content = @Content(schema = @Schema(implementation = RestDto.class))),
        @ApiResponse(responseCode = "500", description = "Internal error",
                content = @Content(schema = @Schema(implementation = RestDto.class)))
    })
    public ResponseEntity<RestDto<ProductModel>> handleStock(@Valid @RequestBody ProductModel product, BindingResult bindingResult)
    {
        // check for validation errors
        if (bindingResult.hasErrors())
        {
            // invalid product return status code 400
            return new ResponseEntity<>(new RestDto<>(null, "Invalid Product"), HttpStatus.BAD_REQUEST);
        }

        // call business service to stock product
        service.stock(product);

        // initialize response data
        List<ProductModel> data = new ArrayList<>();
        // add the new product to the data
        data.add(product);
        // successful operation return status code 201
        return new ResponseEntity<>(new RestDto<>(data, "Stocked Successfully"), HttpStatus.CREATED);
    }

    /**
     * Get one product by ID
     * @param id unique ID of the requested product
     * @return ResponseEntity a wrapper object including the response data, message, and status code
     */
    @GetMapping("/{id}")
    // @Operation is used to define this endpoint in automatically generated Swagger OAS 3 documentation
    @Operation(summary = "Get a Product", description = "Get a Product by ID", tags = { "product" })
    // @ApiResponses is used to define the set of status codes that may result when calling the endpoint in automatically generated Swagger OAS 3 documentation
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Got product successfully",
                content = @Content(schema = @Schema(implementation = RestDto.class))),
        @ApiResponse(responseCode = "404", description = "Provided Product ID doesn't exist in inventory",
                content = @Content(schema = @Schema(implementation = RestDto.class))),
        @ApiResponse(responseCode = "500", description = "Internal error",
                content = @Content(schema = @Schema(implementation = RestDto.class)))
    })
    public ResponseEntity<RestDto<ProductModel>> handleGetOne(@PathVariable("id") String id)
    {
        // call business service to get product by ID
        ProductModel productFound = service.retrieveOne(new ProductModel(id));

        // if the requested product could not be found
        if (productFound == null)
        {
            // return status code 404
            return new ResponseEntity<>(new RestDto<>(null, "Product Not Found"), HttpStatus.NOT_FOUND);
        }

        // requested product was found
        else
        {
            // initialize response data
            List<ProductModel> data = new ArrayList<>();
            // add the requested product to the response
            data.add(productFound);
            // successful operation return status code 200
            return new ResponseEntity<>(new RestDto<>(data, "Success"), HttpStatus.OK);
        }
    }

    /**
     * Get all products in the inventory
     * @return ResponseEntity a wrapper object including the response data, message, and status code
     */
    @GetMapping
    // @Operation is used to define this endpoint in automatically generated Swagger OAS 3 documentation
    @Operation(summary = "Get all Products", description = "Get all Product in the inventory", tags = { "product" })
    // @ApiResponses is used to define the set of status codes that may result when calling the endpoint in automatically generated Swagger OAS 3 documentation
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Got all products successfully",
                content = @Content(schema = @Schema(implementation = RestDto.class))),
        @ApiResponse(responseCode = "500", description = "Internal error",
                content = @Content(schema = @Schema(implementation = RestDto.class)))
    })
    public ResponseEntity<RestDto<ProductModel>> handleGetAll()
    {
        // call business service to get all products
        List<ProductModel> products = service.retrieveAll();

        // successful operation return status code 201
        return new ResponseEntity<>(new RestDto<>(products, "Success"), HttpStatus.OK);
    }

    /**
     * Correct the information of a product by it's unique ID
     * @param product the corrected product
     * @param bindingResult the result of product validation
     * @return ResponseEntity a wrapper object including the response data, message, and status code
     */
    @PutMapping
    // @Operation is used to define this endpoint in automatically generated Swagger OAS 3 documentation
    @Operation(summary = "Correct a Product", description = "Correct (adjust) a Product in the inventory by its ID", tags = { "product" })
    // @ApiResponses is used to define the set of status codes that may result when calling the endpoint in automatically generated Swagger OAS 3 documentation
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Corrected Product information",
                content = @Content(schema = @Schema(implementation = RestDto.class))),
        @ApiResponse(responseCode = "404", description = "Provided Product ID doesn't exist in inventory",
                content = @Content(schema = @Schema(implementation = RestDto.class))),
        @ApiResponse(responseCode = "500", description = "Internal error",
                content = @Content(schema = @Schema(implementation = RestDto.class)))
    })
    public ResponseEntity<RestDto<ProductModel>> handleCorrect(@Valid @RequestBody ProductModel product, BindingResult bindingResult)
    {
        // check for validation errors
        if (bindingResult.hasErrors())
        {
            // invalid product return status code 400
            return new ResponseEntity<>(new RestDto<>(null, "Invalid Product"), HttpStatus.BAD_REQUEST);
        }
        // call business service to correct product
        if (service.correct(product) == ProductBusinessResponse.NOT_FOUND)
        {
            // requested product not found return status code 404
            return new ResponseEntity<>(new RestDto<>(null, "Product Not Found"), HttpStatus.NOT_FOUND);
        }

        // initialize response data
        List<ProductModel> data = new ArrayList<>();
        // add requested and corrected product to response
        data.add(product);
        // successful operation return status code 201
        return new ResponseEntity<>(new RestDto<>(data, "Stocked Successfully"), HttpStatus.OK);
    }

    /**
     * Unstock a product from the inventory
     * @param id the unique ID of the product to unstock
     * @return ResponseEntity a wrapper object including the response data, message, and status code
     */
    @DeleteMapping("/{id}")
    // @Operation is used to define this endpoint in automatically generated Swagger OAS 3 documentation
    @Operation(summary = "Unstock a Product", description = "Unstock a Product from the inventory by its ID", tags = { "product" })
    // @ApiResponses is used to define the set of status codes that may result when calling the endpoint in automatically generated Swagger OAS 3 documentation
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Unstocked product successfully",
                content = @Content(schema = @Schema(implementation = RestDto.class))),
        @ApiResponse(responseCode = "404", description = "Provided Product ID doesn't exist in inventory",
                content = @Content(schema = @Schema(implementation = RestDto.class))),
        @ApiResponse(responseCode = "500", description = "Internal error",
                content = @Content(schema = @Schema(implementation = RestDto.class)))
    })
    public ResponseEntity<RestDto<ProductModel>> handleUnstock(@PathVariable("id") String id)
    {
        // call business service to unstock product
        if (service.unstock(new ProductModel(id)) == ProductBusinessResponse.NOT_FOUND)
        {
            // requested product not found return status code 404
            return new ResponseEntity<>(new RestDto<>(null, "Product Not Found"), HttpStatus.NOT_FOUND);
        }

        // requested product was found and unstocked
        else
        {
            // successful operation return status code 201
            return new ResponseEntity<>(new RestDto<>(null, "Success"), HttpStatus.OK);
        }
    }
}
