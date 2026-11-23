import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the vehicle inventory and produces dealership reports.
 */
public class Dealership {
    private static final Logger LOGGER = AppLogger.getLogger(Dealership.class);

    private final TreeSet<Vehicle> vehicles = new TreeSet<Vehicle>();

    /**
     * Creates an empty dealership inventory.
     */
    public Dealership() {
        LOGGER.info("Dealership constructor called");
    }

    /**
     * Returns the dealership's vehicle inventory.
     *
     * @return the sorted inventory
     */
    public TreeSet<Vehicle> getVehicles() {
        LOGGER.info("Dealership.getVehicles called");
        return vehicles;
    }

    /**
     * Loads vehicles from a text file, using built-in inventory if loading fails
     * or the file contains no valid vehicles.
     *
     * @param file path to the inventory file
     */
    public void loadVehicles(Path file) {
        LOGGER.info("Dealership.loadVehicles called for " + file);
        try {
            List<String> lines = Files.readAllLines(file);
            int lineNumber = 0;
            for (String line : lines) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    vehicles.add(parseVehicle(line));
                } catch (IllegalArgumentException exception) {
                    LOGGER.log(Level.SEVERE, "Skipping invalid vehicle on line " + lineNumber
                            + " in " + file + ".", exception);
                    System.err.println("Skipping invalid vehicle on line " + lineNumber + ": "
                            + exception.getMessage());
                }
            }
            if (vehicles.isEmpty()) {
                System.err.println("No valid vehicles found in " + file + "; using built-in inventory.");
                loadDefaultVehicles();
            }
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Could not read vehicle inventory from " + file + ".", exception);
            System.err.println("Could not read " + file + ": " + exception.getMessage()
                    + "; using built-in inventory.");
            loadDefaultVehicles();
        }
    }

    /**
     * Parses one vehicle record from the inventory file.
     *
     * @param line record containing vehicle fields separated by {@code #}
     * @return the parsed vehicle
     * @throws IllegalArgumentException if the record is malformed
     */
    private Vehicle parseVehicle(String line) {
        LOGGER.info("Dealership.parseVehicle called");
        String[] fields = line.split("#", -1);
        try {
            String type = fields[0].trim();
            String brand = fields[1].trim();
            double basePrice = Double.parseDouble(fields[2].trim());
            int age = Integer.parseInt(fields[3].trim());
            double km = Double.parseDouble(fields[4].trim());
            FuelType fuelType = FuelType.valueOf(fields[5].trim().toUpperCase(Locale.ROOT));

            if ("VEHICLE".equals(type) && fields.length == 6) {
                return new Vehicle(brand, basePrice, age, km, fuelType);
            }
            if ("ELECTRIC".equals(type) && fields.length == 8) {
                int batteryCapacity = Integer.parseInt(fields[6].trim());
                String heatPumpValue = fields[7].trim().toLowerCase(Locale.ROOT);
                if (!"yes".equals(heatPumpValue) && !"no".equals(heatPumpValue)) {
                    throw new IllegalArgumentException("heatPump must be 'yes' or 'no'.");
                }
                return new ElectricVehicle(brand, basePrice, age, km, fuelType,
                        batteryCapacity, "yes".equals(heatPumpValue));
            }
            throw new IllegalArgumentException("unrecognized row type or incorrect number of fields.");
        } catch (IndexOutOfBoundsException | NumberFormatException exception) {
            LOGGER.log(Level.SEVERE, "Invalid field while parsing a vehicle record.", exception);
            throw new IllegalArgumentException("missing or invalid field.", exception);
        }
    }

    /**
     * Adds the built-in inventory to the dealership.
     */
    private void loadDefaultVehicles() {
        LOGGER.info("Dealership.loadDefaultVehicles called");
        vehicles.add(new Vehicle("Toyota Corolla", 5500000, 4, 75000, FuelType.HYBRID));
        vehicles.add(new Vehicle("Volkswagen Golf", 4200000, 6, 120000, FuelType.DIESEL));
        vehicles.add(new ElectricVehicle("Tesla Model 3", 11500000, 2, 38000, 75, true));
    }

    /**
     * Builds a report containing inventory statistics and a discount simulation.
     *
     * @return formatted dealership report
     */
    private String createReport() {
        LOGGER.info("Dealership.createReport called");
        StringBuilder report = new StringBuilder();
        if (vehicles.isEmpty()) {
            return "Fleet Average Mileage: N/A (no vehicles)\n"
                    + "Cheapest Vehicle: N/A\n"
                    + "Top Electric Vehicle: N/A\n"
                    + "Total Fleet Inventory Value: 0\n"
                    + "Filtered Hybrid Inventory:\n  None\n"
                    + "Young Inventory Count: 0\n"
                    + "Discount Simulation: N/A (no vehicles)\n";
        }

        double totalKm = 0;
        long totalValue = 0;
        int youngCount = 0;
        List<Vehicle> hybrids = new ArrayList<Vehicle>();
        ElectricVehicle topElectric = null;
        for (Vehicle vehicle : vehicles) {
            totalKm += vehicle.getKm();
            totalValue += vehicle.getFinalPrice();
            if (vehicle.getAge() < 5) {
                youngCount++;
            }
            if (vehicle.getFuelType() == FuelType.HYBRID) {
                hybrids.add(vehicle);
            }
            if (vehicle instanceof ElectricVehicle
                    && (topElectric == null || vehicle.getFinalPrice() > topElectric.getFinalPrice())) {
                topElectric = (ElectricVehicle) vehicle;
            }
        }

        double averagePrice = (double) totalValue / vehicles.size();
        report.append(String.format(Locale.ROOT, "Fleet Average Mileage: %.2f km%n",
                totalKm / vehicles.size()));
        report.append("Cheapest Vehicle: ").append(vehicles.first()).append(System.lineSeparator());
        if (topElectric == null) {
            report.append("Top Electric Vehicle: N/A").append(System.lineSeparator());
        } else {
            report.append(String.format(Locale.ROOT,
                    "Top Electric Vehicle: batteryCapacity=%d kWh, averageKmPerYear=%.2f km%n",
                    topElectric.getBatteryCapacity(), topElectric.averageKmPerYear()));
        }
        report.append("Total Fleet Inventory Value: ").append(totalValue).append(System.lineSeparator());
        report.append("Filtered Hybrid Inventory:").append(System.lineSeparator());
        boolean foundHybrid = false;
        for (Vehicle vehicle : hybrids) {
            if (vehicle.getFinalPrice() <= averagePrice) {
                report.append("  ").append(vehicle).append(System.lineSeparator());
                foundHybrid = true;
            }
        }
        if (!foundHybrid) {
            report.append("  None").append(System.lineSeparator());
        }
        report.append("Young Inventory Count: ").append(youngCount).append(System.lineSeparator());

        Vehicle discountedVehicle = createDiscountSimulationCopy(vehicles.first());
        discountedVehicle.applyDiscount(10);
        report.append("Discount Simulation: updated final price of ")
                .append(discountedVehicle.getBrand()).append(" = ")
                .append(discountedVehicle.getFinalPrice()).append(System.lineSeparator());
        return report.toString();
    }

    /**
     * Creates a temporary copy for the discount simulation so the inventory is not changed.
     *
     * @param vehicle vehicle to copy
     * @return a copy with the same vehicle attributes
     */
    private Vehicle createDiscountSimulationCopy(Vehicle vehicle) {
        if (vehicle instanceof ElectricVehicle) {
            ElectricVehicle electricVehicle = (ElectricVehicle) vehicle;
            return new ElectricVehicle(
                    electricVehicle.getBrand(),
                    electricVehicle.getModel(),
                    electricVehicle.getBasePrice(),
                    electricVehicle.getAge(),
                    electricVehicle.getKm(),
                    electricVehicle.getFuelType(),
                    electricVehicle.getBatteryCapacity(),
                    electricVehicle.hasHeatPump());
        }
        return new Vehicle(
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getBasePrice(),
                vehicle.getAge(),
                vehicle.getKm(),
                vehicle.getFuelType());
    }

    /**
     * Loads the inventory, prints the generated report, and writes it to a file.
     *
     * @param args command-line arguments (not used)
     * @throws IOException if the report cannot be written
     */
    public static void main(String[] args) throws IOException {
        LOGGER.info("Dealership.main called");
        Dealership dealership = new Dealership();
        dealership.loadVehicles(Paths.get("vehicles.txt"));
        String report = dealership.createReport();
        System.out.print(report);
        Files.write(Paths.get("outputDealership.txt"), report.getBytes("UTF-8"),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    }
}
