package com.theelixrlabs.healthcare.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A generic success response class to wrap success API responses.
 * @param <T> Type of data contained in response.
 */
@Data
@AllArgsConstructor
public class SuccessResponse<T> {
    private Boolean success;
    private T responseData;
}
