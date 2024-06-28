package com.theelixrlabs.healthcare.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a generic success response structure for API responses.
 *
 * @param <User> the type of data included in the response.
 */
@Data
@AllArgsConstructor
public class SuccessResponse<User> {
    /**
     * Indicates whether the operation was successful.
     */
    private boolean success;
    /**
     * The data payload returned as part of the success response.
     */
    private User responseData;
}
