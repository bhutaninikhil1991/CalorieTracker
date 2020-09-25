package calorieapp;

/**
 * class to create instances of food items extractor
 */
public class FoodItemsExtractorFactory {
    /**
     * get food extractor based on the food data type
     * or null if the food data type doesn't match
     *
     * @param type
     * @return
     */
    public FoodItemsExtractor getFoodItemsExtractor(String type) {
        if (type.equals("Branded"))
            return new BrandedFoodItemsExtractor();
        else if (type.equals("SR Legacy"))
            return new SRLegacyFoodItemsExtractor();
        else if (type.equals("Survey (FNDDS)"))
            return new SurveyFoodItemsExtractor();
        else
            return null;
    }
}
