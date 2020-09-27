package calorieapp;

import io.micronaut.context.annotation.ConfigurationProperties;

/**
 * app configuration
 */
@ConfigurationProperties("USDA")
public class CalorieAppConfig {
    private String secretkeyid;

    /**
     * setter for api key
     *
     * @param secretkeyid
     */
    public void setSecretkeyid(String secretkeyid) {
        this.secretkeyid = secretkeyid;
    }

    /**
     * getter for api key
     *
     * @return
     */
    public String getSecretkeyid() {
        return secretkeyid;
    }

}
