import java.util.*;

// Reservation class for Use Case 8
class Reservation8 {
    String reservationId;
    String guestName;
    String roomType;
    String assignedRoomId;

    public Reservation8(String reservationId, String guestName, String roomType, String assignedRoomId) {
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

// Booking History & Reporting
public class UseCase8BookingHistoryReport {

    // List to maintain booking history in order
    private static List<Reservation8> bookingHistory = new ArrayList<>();

    // Add a confirmed booking to history
    public static void addToHistory(Reservation8 reservation) {
        bookingHistory.add(reservation);
        System.out.println("Booking added to history: " + reservation);
    }

    // Generate a summary report
    public static void generateReport() {
        System.out.println("\n=== Booking History Report ===");
        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation8 r : bookingHistory) {
            roomTypeCount.put(r.roomType, roomTypeCount.getOrDefault(r.roomType, 0) + 1);
        }

        System.out.println("Total bookings: " + bookingHistory.size());
        for (Map.Entry<String, Integer> entry : roomTypeCount.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() + " -> " + entry.getValue() + " bookings");
        }
    }

    public static void main(String[] args) {
        // Sample confirmed reservations
        addToHistory(new Reservation8("R101", "Alice", "Deluxe", "Deluxe1"));
        addToHistory(new Reservation8("R102", "Bob", "Suite", "Suite1"));
        addToHistory(new Reservation8("R103", "Charlie", "Standard", "Standard1"));
        addToHistory(new Reservation8("R104", "David", "Deluxe", "Deluxe2"));

        // Generate report
        generateReport();
    }
}