import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class for Use Case 9
class Reservation9 {
    String reservationId;
    String guestName;
    String roomType;
    String assignedRoomId;

    public Reservation9(String reservationId, String guestName, String roomType, String assignedRoomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.assignedRoomId = assignedRoomId;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId + ", Guest: " + guestName + ", Room: " + roomType +
                ", Assigned Room ID: " + assignedRoomId;
    }
}

// Main Class for Error Handling & Validation
public class UseCase9ErrorHandlingValidation {

    private static Map<String, Integer> roomInventory = new HashMap<>();
    private static List<Reservation9> confirmedBookings = new ArrayList<>();

    // Initialize inventory
    static {
        roomInventory.put("Deluxe", 2);
        roomInventory.put("Suite", 2);
        roomInventory.put("Standard", 3);
    }

    // Validate booking request
    public static void validateBooking(String guestName, String roomType) throws InvalidBookingException {
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty!");
        }
        if (!roomInventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
        if (roomInventory.get(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for " + roomType);
        }
    }

    // Confirm booking
    public static void confirmBooking(String reservationId, String guestName, String roomType) {
        try {
            validateBooking(guestName, roomType);

            // Assign a room ID
            String roomId = roomType + (roomInventory.get(roomType));
            roomInventory.put(roomType, roomInventory.get(roomType) - 1);

            Reservation9 reservation = new Reservation9(reservationId, guestName, roomType, roomId);
            confirmedBookings.add(reservation);

            System.out.println("Booking confirmed: " + reservation);

        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
    }

    // Show current inventory
    public static void showInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : roomInventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue() + " available");
        }
    }

    public static void main(String[] args) {

        // Valid bookings
        confirmBooking("R201", "Alice", "Deluxe");
        confirmBooking("R202", "Bob", "Suite");

        // Invalid bookings
        confirmBooking("R203", "", "Standard");           // Empty guest name
        confirmBooking("R204", "Charlie", "Penthouse");  // Invalid room type
        confirmBooking("R205", "David", "Deluxe");       // Should succeed
        confirmBooking("R206", "Eve", "Deluxe");         // No Deluxe rooms left

        showInventory();
    }
}