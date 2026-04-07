import java.util.*;

class Reservation {
    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String toString() {
        return "Guest: " + guestName + ", Room Type: " + roomType;
    }
}

public class UseCase5BookingRequestQueue {

    private static Queue<Reservation> bookingQueue = new LinkedList<>();

    public static void addBookingRequest(String guestName, String roomType) {
        Reservation reservation = new Reservation(guestName, roomType);
        bookingQueue.add(reservation);
        System.out.println("Booking request added: " + reservation);
    }

    public static void showAllRequests() {
        System.out.println("\nCurrent Booking Queue:");
        if (bookingQueue.isEmpty()) {
            System.out.println("No booking requests.");
            return;
        }

        for (Reservation r : bookingQueue) {
            System.out.println(r);
        }
    }

    public static void main(String[] args) {
        addBookingRequest("Alice", "Deluxe");
        addBookingRequest("Bob", "Suite");
        addBookingRequest("Charlie", "Standard");

        showAllRequests();

        System.out.println("\nProcessing next request...");
        Reservation processed = bookingQueue.poll();
        System.out.println("Processed: " + processed);

        showAllRequests();
    }
}