package com.theelixrlabs.healthcare.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

/**
 * A failure response class to wrap failure API responses.
 */
@Data
@AllArgsConstructor
public class FailureResponse {
   private Boolean success;
   private List<String> responseErrors;
}
