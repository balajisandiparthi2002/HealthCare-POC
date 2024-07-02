package com.theelixrlabs.healthcare.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a generic success response structure for API responses.
 *
 * @param <T> the type of data included in the response.
 */
@Data
@AllArgsConstructor
public class SuccessResponse<T> {
    /**
     * Indicates whether the operation was successful.
     */
    private boolean success;
    /**
     * The data payload returned as part of the success response.
     */
    private T responseData;
}
