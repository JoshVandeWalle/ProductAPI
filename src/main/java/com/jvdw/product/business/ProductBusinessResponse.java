package com.jvdw.product.business;

/**
 * The ProductBusinessResponse enumerates possible outcomes of Product business logic
 */
public enum ProductBusinessResponse
{
    // new product stocked successfully
    STOCKED,
    // requested product not found
    NOT_FOUND,
    // product information corrected successfully
    CORRECTED,
    // product unstocked successfully
    UNSTOCKED
}
