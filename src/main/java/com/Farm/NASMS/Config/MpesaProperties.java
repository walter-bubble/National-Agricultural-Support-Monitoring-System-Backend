package com.Farm.NASMS.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mpesa")
public class MpesaProperties {
    private String consumerKey;
    private String consumerSecretKey;
    private String passKey;
    private String shortcode;
    private String oauthUrl;
    private String stkUrl;
    private String callbackUrl;
    private String marketPlaceCallbackUrl;

    public String getMarketPlaceCallbackUrl() {
        return marketPlaceCallbackUrl;
    }

    public void setMarketPlaceCallbackUrl(String marketPlaceCallbackUrl) {
        this.marketPlaceCallbackUrl = marketPlaceCallbackUrl;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecretKey() {
        return consumerSecretKey;
    }

    public void setConsumerSecretKey(String consumerSecretKey) {
        this.consumerSecretKey = consumerSecretKey;
    }

    public String getPassKey() {
        return passKey;
    }

    public void setPassKey(String passKey) {
        this.passKey = passKey;
    }

    public String getShortcode() {
        return shortcode;
    }

    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }

    public String getOauthUrl() {
        return oauthUrl;
    }

    public void setOauthUrl(String oauthUrl) {
        this.oauthUrl = oauthUrl;
    }

    public String getStkUrl() {
        return stkUrl;
    }

    public void setStkUrl(String stkUrl) {
        this.stkUrl = stkUrl;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
