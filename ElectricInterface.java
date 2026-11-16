/**
 * Defines operations specific to electric vehicles.
 */
public interface ElectricInterface {
    /**
     * Checks whether another vehicle has a similar electric range category.
     *
     * @param other vehicle to compare
     * @return {@code true} when the vehicles belong to the same category
     */
    boolean hasSameRangeCategory(Vehicle other);

    /**
     * Returns the estimated cost share attributable to the battery.
     *
     * @return estimated battery cost
     */
    int batteryCostShare();
}
