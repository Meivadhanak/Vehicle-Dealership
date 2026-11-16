/**
 * Defines price-related operations supported by dealership vehicles.
 */
public interface VehicleInterface {
    /**
     * Reduces the vehicle's base price by a percentage.
     *
     * @param percentage discount percentage from 0 to 100
     * @throws IllegalArgumentException if the percentage is outside that range
     */
    void applyDiscount(int percentage);

    /**
     * Returns the price after depreciation and applicable adjustments.
     *
     * @return final price
     */
    int getFinalPrice();

    /**
     * Returns the average mileage driven per year.
     *
     * @return average kilometers per year
     */
    double averageKmPerYear();
}
