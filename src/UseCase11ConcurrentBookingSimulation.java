import java.util.*;
import java.util.concurrent.*;

// Reservation class for concurrent booking
class Reservation11 {
    String reservationId;
    String guestName;
    String roomType;
    String assignedRoomId;

    public Reservation11(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.assignedRoomId = null;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId + ", Guest: " + guestName +
                ", Room Type: " + roomType +
                (assignedRoomId != null ? ", Assigned Room ID: " + assignedRoomId : "");
    }
}

// Main Class for Concurrent Booking Simulation
public class UseCase11ConcurrentBookingSimulation {

    // Shared booking queue
    private static Queue<Reservation11> bookingQueue = new LinkedList<>();

    // Shared inventory: room type -> available count
    private static Map<String, Integer> roomInventory = new HashMap<>();

    // Shared allocated room IDs: room type -> assigned room IDs
    private static Map<String, Set<String>> allocatedRooms = new HashMap<>();

    // Initialize inventory and allocatedRooms
    static {
        roomInventory.put("Deluxe", 2);
        roomInventory.put("Suite", 2);
        roomInventory.put("Standard", 3);

        allocatedRooms.put("Deluxe", new HashSet<>());
        allocatedRooms.put("Suite", new HashSet<>());
        allocatedRooms.put("Standard", new HashSet<>());
    }

    // Add booking request (thread-safe)
    public static synchronized void addBookingRequest(Reservation11 reservation) {
        bookingQueue.add(reservation);
        System.out.println("Booking request added: " + reservation);
    }

    // Process booking requests (thread-safe)
    public static void processBookings() {
        while (true) {
            Reservation11 reservation;

            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) break;
                reservation = bookingQueue.poll();
            }

            synchronized (roomInventory) {
                int available = roomInventory.getOrDefault(reservation.roomType, 0);
                if (available > 0) {
                    // Generate unique room ID
                    String roomId = generateRoomId(reservation.roomType);
                    reservation.assignedRoomId = roomId;

                    allocatedRooms.get(reservation.roomType).add(roomId);
                    roomInventory.put(reservation.roomType, available - 1);

                    System.out.println("Booking confirmed: " + reservation);
                } else {
                    System.out.println("No rooms available for " + reservation);
                }
            }
        }
    }

    // Generate unique room ID (thread-safe)
    private static String generateRoomId(String roomType) {
        Set<String> assigned = allocatedRooms.get(roomType);
        int id = 1;
        while (assigned.contains(roomType + id)) {
            id++;
        }
        return roomType + id;
    }

    public static void main(String[] args) throws InterruptedException {
        // Simulate multiple guests booking concurrently
        List<Reservation11> sampleReservations = Arrays.asList(
                new Reservation11("R401", "Alice", "Deluxe"),
                new Reservation11("R402", "Bob", "Suite"),
                new Reservation11("R403", "Charlie", "Standard"),
                new Reservation11("R404", "David", "Deluxe"),
                new Reservation11("R405", "Eve", "Suite"),
                new Reservation11("R406", "Frank", "Standard"),
                new Reservation11("R407", "Grace", "Standard")
        );

        // Create threads to simulate concurrent booking
        List<Thread> threads = new ArrayList<>();
        for (Reservation11 r : sampleReservations) {
            Thread t = new Thread(() -> addBookingRequest(r));
            threads.add(t);
            t.start();
        }

        // Wait for all threads to finish
        for (Thread t : threads) t.join();

        System.out.println("\nProcessing bookings concurrently...");
        processBookings();

        System.out.println("\nFinal Inventory:");
        roomInventory.forEach((k, v) -> System.out.println(k + " -> " + v + " available"));
    }
}