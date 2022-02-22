package com.jvdw.product.api.dto;

import com.jvdw.product.model.ProductModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * REST DTO is the vehicle of response communication from the API
 * @param <T> The object model being operated on e.g. ProductModel
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestDto<T>
{
    // List of object models to include in response body
    // @Schema annotation is used to generate Swagger OAS 3 docs
    // When a new Object Model is added to the application add it to the oneOf attribute list
    @Schema(description = "Response data",
            oneOf = {ProductModel.class},
            example = "[{\"id\": \"620c8c44e136fd50c99323be\",\n" +
            "    \"name\": \"The Lord of the Rings\",\n" +
            "    \"description\": \"Featuring Tom Bombadil\",\n" +
            "    \"price\": 14.99,\n" +
            "    \"quantity\": 22}]")
    private List<T> data;
    // The response message
    // @Schema annotation is used to generate Swagger OAS 3 docs
    @Schema(description = "Response message", example = "Operation completed successfully")
    private String message;
}
