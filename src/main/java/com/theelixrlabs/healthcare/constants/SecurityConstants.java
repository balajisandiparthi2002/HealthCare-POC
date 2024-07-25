package com.theelixrlabs.healthcare.constants;

/**
 * Constants for spring security implementation
 */
public class SecurityConstants {
    public static final String DEV_PROFILE_CONSTANT = "dev";
    public static final String GOOGLE_JWK_SET_URI = "${spring.security.oauth2.client.provider.google.jwk-set-uri}";
    public static final String PROD_PROFILE_CONSTANT = "prod";
}
