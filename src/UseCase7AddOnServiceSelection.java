import java.util.*;

// Add-On Service class
class AddOnService {
    String serviceName;
    double price;

    public AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    @Override
    public String toString() {
        return serviceName + " ($" + price + ")";
    }
}

// Reservation class for Use Case 7
class Reservation7 {
    String reservationId;
    String guestName;
    String roomType;

    public Reservation7(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId + ", Guest: " + guestName + ", Room: " + roomType;
    }
}

// Add-On Service Manager
public class UseCase7AddOnServiceSelection {

    // Map reservation ID -> list of add-on services
    private static Map<String, List<AddOnService>> reservationServices = new HashMap<>();

    // Sample reservations
    private static List<Reservation7> reservations = new ArrayList<>();

    // Add sample reservations
    static {
        reservations.add(new Reservation7("R101", "Alice", "Deluxe"));
        reservations.add(new Reservation7("R102", "Bob", "Suite"));
        reservations.add(new Reservation7("R103", "Charlie", "Standard"));
    }

    // Attach add-on service to a reservation
    public static void addServiceToReservation(String reservationId, AddOnService service) {
        reservationServices.computeIfAbsent(reservationId, k -> new ArrayList<>()).add(service);
        System.out.println("Added service " + service + " to reservation " + reservationId);
    }

    // Calculate total cost of add-ons for a reservation
    public static double calculateAddOnCost(String reservationId) {
        List<AddOnService> services = reservationServices.getOrDefault(reservationId, Collections.emptyList());
        return services.stream().mapToDouble(s -> s.price).sum();
    }

    // Show all add-ons per reservation
    public static void showAllAddOns() {
        System.out.println("\nAdd-On Services Summary:");
        for (Reservation7 res : reservations) {
            List<AddOnService> services = reservationServices.getOrDefault(res.reservationId, Collections.emptyList());
            System.out.println(res + " -> Services: " + services + " | Total: $" + calculateAddOnCost(res.reservationId));
        }
    }

    public static void main(String[] args) {
        // Add some add-on services
        addServiceToReservation("R101", new AddOnService("Breakfast", 15.0));
        addServiceToReservation("R101", new AddOnService("Airport Pickup", 30.0));
        addServiceToReservation("R102", new AddOnService("Spa Access", 50.0));
        addServiceToReservation("R103", new AddOnService("Extra Bed", 25.0));

        // Show summary
        showAllAddOns();
    }
}