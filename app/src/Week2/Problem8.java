package src.Week2;
import java.util.*;

public class Problem8 {
    // Parking spot states
    enum SpotStatus { EMPTY, OCCUPIED, DELETED }

    // Parking spot entry
    static class ParkingSpot {
        String licensePlate;
        long entryTime;
        SpotStatus status;

        ParkingSpot() {
            this.status = SpotStatus.EMPTY;
        }
    }

    private ParkingSpot[] spots;
    private int totalSpots;
    private int occupiedCount;
    private int totalProbes;
    private int probeOperations;
    private Map<Integer, Integer> hourlyOccupancy; // hour -> occupancy

    public Problem8(int capacity) {
        this.totalSpots = capacity;
        this.spots = new ParkingSpot[capacity];
        for (int i = 0; i < capacity; i++) {
            spots[i] = new ParkingSpot();
        }
        this.hourlyOccupancy = new HashMap<>();
    }

    // Hash function: licensePlate → preferred spot
    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % totalSpots;
    }

    // Park vehicle using linear probing
    public String parkVehicle(String licensePlate) {
        int preferred = hash(licensePlate);
        int probes = 0;

        for (int i = 0; i < totalSpots; i++) {
            int idx = (preferred + i) % totalSpots;
            if (spots[idx].status == SpotStatus.EMPTY || spots[idx].status == SpotStatus.DELETED) {
                spots[idx].licensePlate = licensePlate;
                spots[idx].entryTime = System.currentTimeMillis();
                spots[idx].status = SpotStatus.OCCUPIED;
                occupiedCount++;
                totalProbes += probes;
                probeOperations++;
                return "Assigned spot #" + idx + " (" + probes + " probes)";
            }
            probes++;
        }
        return "Parking lot full!";
    }

    // Exit vehicle
    public String exitVehicle(String licensePlate) {
        int preferred = hash(licensePlate);

        for (int i = 0; i < totalSpots; i++) {
            int idx = (preferred + i) % totalSpots;
            if (spots[idx].status == SpotStatus.OCCUPIED && spots[idx].licensePlate.equals(licensePlate)) {
                long durationMs = System.currentTimeMillis() - spots[idx].entryTime;
                double hours = durationMs / (1000.0 * 60 * 60);
                double fee = hours * 5.5; // $5.5 per hour

                spots[idx].status = SpotStatus.DELETED;
                occupiedCount--;

                return "Spot #" + idx + " freed, Duration: " +
                        String.format("%.2f", hours) + "h, Fee: $" + String.format("%.2f", fee);
            }
        }
        return "Vehicle not found!";
    }

    // Generate statistics
    public String getStatistics() {
        double occupancyRate = (occupiedCount * 100.0) / totalSpots;
        double avgProbes = probeOperations == 0 ? 0 : (totalProbes * 1.0 / probeOperations);

        // Simulate peak hour tracking
        int peakHour = hourlyOccupancy.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(-1);

        return "Occupancy: " + String.format("%.1f", occupancyRate) + "%, " +
                "Avg Probes: " + String.format("%.2f", avgProbes) + ", " +
                "Peak Hour: " + (peakHour == -1 ? "N/A" : peakHour + ":00");
    }

    // Demo
    public static void main(String[] args) throws InterruptedException {
        Problem8 parkingLot = new Problem8(500);

        System.out.println(parkingLot.parkVehicle("ABC-1234")); // Assigned spot
        System.out.println(parkingLot.parkVehicle("ABC-1235")); // Collision → linear probe
        System.out.println(parkingLot.parkVehicle("XYZ-9999")); // More probes

        Thread.sleep(2000); // simulate time passing

        System.out.println(parkingLot.exitVehicle("ABC-1234")); // Freed with fee

        System.out.println(parkingLot.getStatistics()); // Occupancy, avg probes, peak hour
    }
}

