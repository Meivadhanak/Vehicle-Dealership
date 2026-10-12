import java.util.concurrent.atomic.AtomicLong;

public class Vehicle implements VehicleInterface, Comparable<Vehicle> {
    private static final AtomicLong NEXT_SORT_ID = new AtomicLong();

    private final long sortId = NEXT_SORT_ID.getAndIncrement();
    private String brand;
    private String model;
    private double basePrice;
    private int age;
    private double km;
    private FuelType fuelType;

    public Vehicle(String brand, String model, double basePrice, int age, double km, FuelType fuelType) {
        this.brand = brand;
        this.model = model;
        this.basePrice = basePrice;
        this.age = age;
        this.km = km;
        this.fuelType = fuelType;
    }

    public Vehicle(String brand, double basePrice, int age, double km, FuelType fuelType) {
        this(brand, "", basePrice, age, km, fuelType);
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        this.km = km;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    @Override
    public void applyDiscount(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100.");
        }
        basePrice *= (100.0 - percentage) / 100.0;
    }

    @Override
    public int getFinalPrice() {
        double price = Math.max(basePrice - age * 150000.0, 200000.0);
        if (fuelType == FuelType.HYBRID) {
            price *= 1.15;
        } else if (fuelType == FuelType.DIESEL) {
            price *= 1.05;
        }
        return (int) price;
    }

    @Override
    public double averageKmPerYear() {
        return age == 0 ? km : km / age;
    }

    @Override
    public int compareTo(Vehicle other) {
        int priceComparison = Integer.compare(getFinalPrice(), other.getFinalPrice());
        return priceComparison != 0 ? priceComparison : Long.compare(sortId, other.sortId);
    }

    @Override
    public String toString() {
        String vehicleModel = model == null || model.isEmpty() ? "" : ", model='" + model + "'";
        return String.format(
                "Vehicle{brand='%s'%s, basePrice=%.2f, age=%d, km=%.2f, fuelType=%s, finalPrice=%d, averageKmPerYear=%.2f}",
                brand, vehicleModel, basePrice, age, km, fuelType, getFinalPrice(), averageKmPerYear());
    }
}
