package utils;

import controllers.CalorieController;
import models.ServingSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class contains helper methods
 */
public class HelperUtils {
    private static final Logger log = LoggerFactory.getLogger(CalorieController.class);

    /**
     * helper method for parsing Serving Text
     *
     * @param servingText
     * @return ServingSize
     */
    public static ServingSize parseServingText(String servingText) {
        if (servingText.equals("Quantity not specified"))
            throw new IllegalArgumentException();
        int index = servingText.indexOf(' ');
        double quantity;
        try {
            quantity = Double.parseDouble(servingText.substring(0, index));
        } catch (NumberFormatException e) {
            throw new NumberFormatException();
        }
        String label = servingText.substring(index + 1);
        return new ServingSize(label, quantity);
    }

    public static void logErrorMessage(String errorDescription, Exception ex) {
        log.error(errorDescription);
        log.error(ex.getStackTrace().toString());
    }
}
