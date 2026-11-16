import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * Represents a vehicle available in the dealership.
 */
public class Vehicle implements VehicleInterface, Comparable<Vehicle> {
    private static final Logger LOGGER = AppLogger.getLogger(Vehicle.class);
    private static final AtomicLong NEXT_SORT_ID = new AtomicLong();

    private final long sortId = NEXT_SORT_ID.getAndIncrement();
    private String brand;
    private String model;
    private double basePrice;
    private int age;
    private double km;
    private FuelType fuelType;

    /**
     * Creates a vehicle with its brand, model, price, age, mileage, and fuel type.
     *
     * @param brand vehicle brand
     * @param model vehicle model
     * @param basePrice original price before depreciation and adjustments
     * @param age vehicle age in years
     * @param km vehicle mileage
     * @param fuelType vehicle fuel type
     */
    public Vehicle(String brand, String model, double basePrice, int age, double km, FuelType fuelType) {
        LOGGER.info("Vehicle constructor called");
        this.brand = brand;
        this.model = model;
        this.basePrice = basePrice;
        this.age = age;
        this.km = km;
        this.fuelType = fuelType;
    }

    /**
     * Creates a vehicle without a specified model.
     *
     * @param brand vehicle brand
     * @param basePrice original price before depreciation and adjustments
     * @param age vehicle age in years
     * @param km vehicle mileage
     * @param fuelType vehicle fuel type
     */
    public Vehicle(String brand, double basePrice, int age, double km, FuelType fuelType) {
        this(brand, "", basePrice, age, km, fuelType);
        LOGGER.info("Vehicle constructor called");
    }

    /**
     * Returns the vehicle brand.
     *
     * @return vehicle brand
     */
    public String getBrand() {
        LOGGER.info("Vehicle.getBrand called");
        return brand;
    }

    /**
     * Updates the vehicle brand.
     *
     * @param brand new vehicle brand
     */
    public void setBrand(String brand) {
        LOGGER.info("Vehicle.setBrand called");
        this.brand = brand;
    }

    /**
     * Returns the vehicle model.
     *
     * @return vehicle model
     */
    public String getModel() {
        LOGGER.info("Vehicle.getModel called");
        return model;
    }

    /**
     * Updates the vehicle model.
     *
     * @param model new vehicle model
     */
    public void setModel(String model) {
        LOGGER.info("Vehicle.setModel called");
        this.model = model;
    }

    /**
     * Returns the vehicle's base price.
     *
     * @return base price
     */
    public double getBasePrice() {
        LOGGER.info("Vehicle.getBasePrice called");
        return basePrice;
    }

    /**
     * Updates the vehicle's base price.
     *
     * @param basePrice new base price
     */
    public void setBasePrice(double basePrice) {
        LOGGER.info("Vehicle.setBasePrice called");
        this.basePrice = basePrice;
    }

    /**
     * Returns the vehicle age in years.
     *
     * @return vehicle age
     */
    public int getAge() {
        LOGGER.info("Vehicle.getAge called");
        return age;
    }

    /**
     * Updates the vehicle age.
     *
     * @param age new age in years
     */
    public void setAge(int age) {
        LOGGER.info("Vehicle.setAge called");
        this.age = age;
    }

    /**
     * Returns the vehicle mileage.
     *
     * @return mileage in kilometers
     */
    public double getKm() {
        LOGGER.info("Vehicle.getKm called");
        return km;
    }

    /**
     * Updates the vehicle mileage.
     *
     * @param km new mileage in kilometers
     */
    public void setKm(double km) {
        LOGGER.info("Vehicle.setKm called");
        this.km = km;
    }

    /**
     * Returns the vehicle fuel type.
     *
     * @return vehicle fuel type
     */
    public FuelType getFuelType() {
        LOGGER.info("Vehicle.getFuelType called");
        return fuelType;
    }

    /**
     * Updates the vehicle fuel type.
     *
     * @param fuelType new vehicle fuel type
     */
    public void setFuelType(FuelType fuelType) {
        LOGGER.info("Vehicle.setFuelType called");
        this.fuelType = fuelType;
    }

    /**
     * Reduces the base price by the requested percentage.
     *
     * @param percentage discount percentage from 0 to 100
     * @throws IllegalArgumentException if the percentage is outside that range
     */
    @Override
    public void applyDiscount(int percentage) {
        LOGGER.info("Vehicle.applyDiscount called");
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100.");
        }
        basePrice *= (100.0 - percentage) / 100.0;
    }

    /**
     * Calculates the vehicle's price after depreciation and fuel-type adjustment.
     *
     * @return final price as an integer
     */
    @Override
    public int getFinalPrice() {
        LOGGER.info("Vehicle.getFinalPrice called");
        double price = Math.max(basePrice - age * 150000.0, 200000.0);
        if (fuelType == FuelType.HYBRID) {
            price *= 1.15;
        } else if (fuelType == FuelType.DIESEL) {
            price *= 1.05;
        }
        return (int) price;
    }

    /**
     * Calculates the average distance driven per year.
     *
     * @return average kilometers per year
     */
    @Override
    public double averageKmPerYear() {
        LOGGER.info("Vehicle.averageKmPerYear called");
        return age == 0 ? km : km / age;
    }

    /**
     * Compares vehicles by final price, then by their creation order.
     *
     * @param other vehicle to compare with
     * @return a negative, zero, or positive value as this vehicle is less than,
     *         equal to, or greater than the other vehicle
     */
    @Override
    public int compareTo(Vehicle other) {
        LOGGER.info("Vehicle.compareTo called");
        int priceComparison = Integer.compare(getFinalPrice(), other.getFinalPrice());
        return priceComparison != 0 ? priceComparison : Long.compare(sortId, other.sortId);
    }

    /**
     * Returns a formatted description of the vehicle.
     *
     * @return vehicle details
     */
    @Override
    public String toString() {
        LOGGER.info("Vehicle.toString called");
        String vehicleModel = model == null || model.isEmpty() ? "" : ", model='" + model + "'";
        return String.format(
                "Vehicle{brand='%s'%s, basePrice=%.2f, age=%d, km=%.2f, fuelType=%s, finalPrice=%d, averageKmPerYear=%.2f}",
                brand, vehicleModel, basePrice, age, km, fuelType, getFinalPrice(), averageKmPerYear());
    }
}
