import java.util.logging.Logger;

/**
 * Represents an electric vehicle with battery and heat-pump attributes.
 */
public class ElectricVehicle extends Vehicle implements ElectricInterface {
    private static final Logger LOGGER = AppLogger.getLogger(ElectricVehicle.class);
    private int batteryCapacity;
    private boolean hasHeatPump;

    /**
     * Creates an electric vehicle with full vehicle and electric-system details.
     *
     * @param brand vehicle brand
     * @param model vehicle model
     * @param basePrice original price before adjustments
     * @param age vehicle age in years
     * @param km vehicle mileage
     * @param fuelType vehicle fuel type
     * @param batteryCapacity battery capacity in kWh
     * @param hasHeatPump whether the vehicle has a heat pump
     */
    public ElectricVehicle(String brand, String model, double basePrice, int age, double km,
                           FuelType fuelType, int batteryCapacity, boolean hasHeatPump) {
        super(brand, model, basePrice, age, km, fuelType);
        LOGGER.info("ElectricVehicle constructor called");
        this.batteryCapacity = batteryCapacity;
        this.hasHeatPump = hasHeatPump;
    }

    /**
     * Creates an electric vehicle with no model and hybrid fuel type.
     *
     * @param brand vehicle brand
     * @param basePrice original price before adjustments
     * @param age vehicle age in years
     * @param km vehicle mileage
     * @param batteryCapacity battery capacity in kWh
     * @param hasHeatPump whether the vehicle has a heat pump
     */
    public ElectricVehicle(String brand, double basePrice, int age, double km,
                           int batteryCapacity, boolean hasHeatPump) {
        this(brand, "", basePrice, age, km, FuelType.HYBRID, batteryCapacity, hasHeatPump);
        LOGGER.info("ElectricVehicle constructor called");
    }

    /**
     * Creates an electric vehicle with no model and the specified fuel type.
     *
     * @param brand vehicle brand
     * @param basePrice original price before adjustments
     * @param age vehicle age in years
     * @param km vehicle mileage
     * @param fuelType vehicle fuel type
     * @param batteryCapacity battery capacity in kWh
     * @param hasHeatPump whether the vehicle has a heat pump
     */
    public ElectricVehicle(String brand, double basePrice, int age, double km, FuelType fuelType,
                           int batteryCapacity, boolean hasHeatPump) {
        this(brand, "", basePrice, age, km, fuelType, batteryCapacity, hasHeatPump);
        LOGGER.info("ElectricVehicle constructor called");
    }

    /**
     * Returns the battery capacity.
     *
     * @return battery capacity in kWh
     */
    public int getBatteryCapacity() {
        LOGGER.info("ElectricVehicle.getBatteryCapacity called");
        return batteryCapacity;
    }

    /**
     * Updates the battery capacity.
     *
     * @param batteryCapacity new capacity in kWh
     */
    public void setBatteryCapacity(int batteryCapacity) {
        LOGGER.info("ElectricVehicle.setBatteryCapacity called");
        this.batteryCapacity = batteryCapacity;
    }

    /**
     * Indicates whether the vehicle has a heat pump.
     *
     * @return {@code true} if a heat pump is installed
     */
    public boolean hasHeatPump() {
        LOGGER.info("ElectricVehicle.hasHeatPump called");
        return hasHeatPump;
    }

    /**
     * Sets whether the vehicle has a heat pump.
     *
     * @param hasHeatPump {@code true} if a heat pump is installed
     */
    public void setHasHeatPump(boolean hasHeatPump) {
        LOGGER.info("ElectricVehicle.setHasHeatPump called");
        this.hasHeatPump = hasHeatPump;
    }

    /**
     * Calculates the final price including battery and heat-pump adjustments.
     *
     * @return final price as an integer
     */
    @Override
    public int getFinalPrice() {
        LOGGER.info("ElectricVehicle.getFinalPrice called");
        double price = super.getFinalPrice();
        if (batteryCapacity >= 60) {
            price *= 1.10;
        }
        if (hasHeatPump) {
            price *= 1.05;
        }
        return (int) price;
    }

    /**
     * Checks whether another vehicle is electric and has a similar battery capacity.
     *
     * @param other vehicle to compare
     * @return {@code true} if both are electric and their capacities differ by at
     *         most 5 kWh
     */
    @Override
    public boolean hasSameRangeCategory(Vehicle other) {
        LOGGER.info("ElectricVehicle.hasSameRangeCategory called");
        return other instanceof ElectricVehicle
                && Math.abs(batteryCapacity - ((ElectricVehicle) other).batteryCapacity) <= 5;
    }

    /**
     * Calculates the battery's estimated share of the vehicle cost.
     *
     * @return estimated battery cost
     */
    @Override
    public int batteryCostShare() {
        LOGGER.info("ElectricVehicle.batteryCostShare called");
        return batteryCapacity * 25000;
    }

    /**
     * Returns a formatted description of the electric vehicle.
     *
     * @return electric vehicle details
     */
    @Override
    public String toString() {
        LOGGER.info("ElectricVehicle.toString called");
        return "ElectricVehicle{" + super.toString()
                + String.format(", batteryCapacity=%d kWh, hasHeatPump=%s}", batteryCapacity, hasHeatPump);
    }
}
