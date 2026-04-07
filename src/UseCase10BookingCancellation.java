import java.util.*;

// Reservation class for Use Case 10
class Reservation10 {
    String reservationId;
    String guestName;
    String roomType;
    String assignedRoomId;

    public Reservation10(String reservationId, String guestName, String roomType, String assignedRoomId) {
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

// Main Class for Booking Cancellation & Inventory Rollback
public class UseCase10BookingCancellation {

    private static Map<String, Integer> roomInventory = new HashMap<>();
    private static Map<String, Stack<String>> rollbackStack = new HashMap<>();
    private static List<Reservation10> confirmedBookings = new ArrayList<>();

    // Initialize inventory
    static {
        roomInventory.put("Deluxe", 2);
        roomInventory.put("Suite", 2);
        roomInventory.put("Standard", 3);

        rollbackStack.put("Deluxe", new Stack<>());
        rollbackStack.put("Suite", new Stack<>());
        rollbackStack.put("Standard", new Stack<>());
    }

    // Confirm a booking
    public static void confirmBooking(String reservationId, String guestName, String roomType) {
        int available = roomInventory.getOrDefault(roomType, 0);
        if (available <= 0) {
            System.out.println("Booking failed: No rooms available for " + roomType);
            return;
        }

        String roomId = roomType + (available); // e.g., Deluxe2
        roomInventory.put(roomType, available - 1);
        rollbackStack.get(roomType).push(roomId);

        Reservation10 reservation = new Reservation10(reservationId, guestName, roomType, roomId);
        confirmedBookings.add(reservation);
        System.out.println("Booking confirmed: " + reservation);
    }

    // Cancel a booking
    public static void cancelBooking(String reservationId) {
        Reservation10 toCancel = null;
        for (Reservation10 r : confirmedBookings) {
            if (r.reservationId.equals(reservationId)) {
                toCancel = r;
                break;
            }
        }

        if (toCancel == null) {
            System.out.println("Cancellation failed: Reservation " + reservationId + " not found");
            return;
        }

        // Restore inventory and rollback
        Stack<String> stack = rollbackStack.get(toCancel.roomType);
        if (!stack.isEmpty() && stack.peek().equals(toCancel.assignedRoomId)) {
            stack.pop();
            roomInventory.put(toCancel.roomType, roomInventory.get(toCancel.roomType) + 1);
            confirmedBookings.remove(toCancel);
            System.out.println("Booking cancelled successfully: " + toCancel);
        } else {
            System.out.println("Cancellation error: Room ID mismatch or already rolled back");
        }
    }

    // Show current inventory
    public static void showInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : roomInventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue() + " available");
        }
    }

    // Show all confirmed bookings
    public static void showConfirmedBookings() {
        System.out.println("\nConfirmed Bookings:");
        for (Reservation10 r : confirmedBookings) {
            System.out.println(r);
        }
    }

    public static void main(String[] args) {
        // Confirm bookings
        confirmBooking("R301", "Alice", "Deluxe");
        confirmBooking("R302", "Bob", "Suite");
        confirmBooking("R303", "Charlie", "Standard");

        showInventory();
        showConfirmedBookings();

        // Cancel a booking
        cancelBooking("R302"); // cancel Bob's Suite
        cancelBooking("R999"); // invalid ID

        showInventory();
        showConfirmedBookings();
    }
}