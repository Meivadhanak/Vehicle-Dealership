public class ElectricVehicle extends Vehicle implements ElectricInterface {
    private int batteryCapacity;
    private boolean hasHeatPump;

    public ElectricVehicle(String brand, String model, double basePrice, int age, double km,
                           FuelType fuelType, int batteryCapacity, boolean hasHeatPump) {
        super(brand, model, basePrice, age, km, fuelType);
        this.batteryCapacity = batteryCapacity;
        this.hasHeatPump = hasHeatPump;
    }

    public ElectricVehicle(String brand, double basePrice, int age, double km,
                           int batteryCapacity, boolean hasHeatPump) {
        this(brand, "", basePrice, age, km, FuelType.HYBRID, batteryCapacity, hasHeatPump);
    }

    public ElectricVehicle(String brand, double basePrice, int age, double km, FuelType fuelType,
                           int batteryCapacity, boolean hasHeatPump) {
        this(brand, "", basePrice, age, km, fuelType, batteryCapacity, hasHeatPump);
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public boolean hasHeatPump() {
        return hasHeatPump;
    }

    public void setHasHeatPump(boolean hasHeatPump) {
        this.hasHeatPump = hasHeatPump;
    }

    @Override
    public int getFinalPrice() {
        double price = super.getFinalPrice();
        if (batteryCapacity >= 60) {
            price *= 1.10;
        }
        if (hasHeatPump) {
            price *= 1.05;
        }
        return (int) price;
    }

    @Override
    public boolean hasSameRangeCategory(Vehicle other) {
        return other instanceof ElectricVehicle
                && Math.abs(batteryCapacity - ((ElectricVehicle) other).batteryCapacity) <= 5;
    }

    @Override
    public int batteryCostShare() {
        return batteryCapacity * 25000;
    }

    @Override
    public String toString() {
        return "ElectricVehicle{" + super.toString()
                + String.format(", batteryCapacity=%d kWh, hasHeatPump=%s}", batteryCapacity, hasHeatPump);
    }
}
