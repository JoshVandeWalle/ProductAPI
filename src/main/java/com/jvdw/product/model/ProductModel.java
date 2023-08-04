package com.jvdw.product.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * This Object Model represents a product
 */

// lombok annotations are used to reduce the amount of boiulerplate code
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class ProductModel
{
    // the Product's unique ID
    @Schema(description = "Product ID", example = "620c8c44e136fd50c99323be")
    private String id;

    // the product name
    // NonNull is a lombok annotation used to include the field in the "Required Args Constructor" and ensure it isn't null
    @NonNull
    @NotBlank
    @Schema(description = "Product name", example = "The Lord of the Rings")
    private String name;

    // the product description
    // NonNull is a lombok annotation used to include the field in the "Required Args Constructor" and ensure it isn't null
    @NonNull
    @Schema(description = "Product description", example = "Featuring Tom Bombadil")
    private String description;

    // the product price
    // NonNull is a lombok annotation used to include the field in the "Required Args Constructor" and ensure it isn't null
    @NonNull
    @NotNull
    @Schema(description = "Product price", example = "14.99")
    private BigDecimal price;

    // the product quantity
    // NonNull is a lombok annotation used to include the field in the "Required Args Constructor" and ensure it isn't null
    @NonNull
    @Positive
    @Schema(description = "Available quantity", example = "22")
    private int quantity;

    /**
     * This constructor instantiates a ProductModel with just an ID
     * @param id the unique Product ID
     */
    public ProductModel(String id)
    {
        // set field from parameter
        this.id = id;
    }
}
