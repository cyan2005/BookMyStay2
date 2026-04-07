import java.util.*;

// Reservation class for Use Case 6
class Reservation6 {
    String guestName;
    String roomType;
    String assignedRoomId; // assigned during allocation

    public Reservation6(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.assignedRoomId = null; // not assigned yet
    }

    @Override
    public String toString() {
        return "Guest: " + guestName + ", Room Type: " + roomType +
                (assignedRoomId != null ? ", Room ID: " + assignedRoomId : "");
    }
}

// Main Room Allocation Service
public class UseCase6RoomAllocationService {

    // FIFO queue for booking requests
    private static Queue<Reservation6> bookingQueue = new LinkedList<>();

    // Inventory: room type -> available count
    private static Map<String, Integer> roomInventory = new HashMap<>();

    // Allocated rooms: room type -> assigned room IDs
    private static Map<String, Set<String>> allocatedRooms = new HashMap<>();

    // Initialize inventory
    static {
        roomInventory.put("Deluxe", 2);
        roomInventory.put("Suite", 2);
        roomInventory.put("Standard", 3);

        allocatedRooms.put("Deluxe", new HashSet<>());
        allocatedRooms.put("Suite", new HashSet<>());
        allocatedRooms.put("Standard", new HashSet<>());
    }

    // Add booking request
    public static void addBookingRequest(String guestName, String roomType) {
        Reservation6 reservation = new Reservation6(guestName, roomType);
        bookingQueue.add(reservation);
        System.out.println("Booking request added: " + reservation);
    }

    // Generate unique room ID
    private static String generateRoomId(String roomType) {
        Set<String> assigned = allocatedRooms.get(roomType);
        int id = 1;
        while (assigned.contains(roomType + id)) {
            id++;
        }
        return roomType + id;
    }

    // Process booking queue
    public static void processBookings() {
        while (!bookingQueue.isEmpty()) {
            Reservation6 reservation = bookingQueue.poll();
            String type = reservation.roomType;
            int available = roomInventory.getOrDefault(type, 0);

            if (available > 0) {
                String roomId = generateRoomId(type);
                reservation.assignedRoomId = roomId;

                // Update inventory and allocation
                allocatedRooms.get(type).add(roomId);
                roomInventory.put(type, available - 1);

                System.out.println("Reservation confirmed: " + reservation);
            } else {
                System.out.println("No rooms available for " + reservation);
            }
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
        // Add sample booking requests
        addBookingRequest("Alice", "Deluxe");
        addBookingRequest("Bob", "Suite");
        addBookingRequest("Charlie", "Standard");
        addBookingRequest("David", "Deluxe");
        addBookingRequest("Eve", "Suite");
        addBookingRequest("Frank", "Standard");
        addBookingRequest("Grace", "Standard");

        showInventory();

        System.out.println("\nProcessing bookings...");
        processBookings();

        showInventory();
    }
}