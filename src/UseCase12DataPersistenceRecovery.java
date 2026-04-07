import java.io.*;
import java.util.*;

// Serializable Reservation class
class Reservation12 implements Serializable {
    private static final long serialVersionUID = 1L;

    String reservationId;
    String guestName;
    String roomType;
    String assignedRoomId;

    public Reservation12(String reservationId, String guestName, String roomType) {
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

public class UseCase12DataPersistenceRecovery {

    private static final String FILE_PATH = "booking_state.ser";

    private static Queue<Reservation12> bookingQueue = new LinkedList<>();
    private static Map<String, Integer> roomInventory = new HashMap<>();
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
    public static void addBookingRequest(Reservation12 reservation) {
        bookingQueue.add(reservation);
        System.out.println("Booking request added: " + reservation);
    }

    // Process bookings
    public static void processBookings() {
        while (!bookingQueue.isEmpty()) {
            Reservation12 res = bookingQueue.poll();
            int available = roomInventory.getOrDefault(res.roomType, 0);

            if (available > 0) {
                String roomId = generateRoomId(res.roomType);
                res.assignedRoomId = roomId;

                allocatedRooms.get(res.roomType).add(roomId);
                roomInventory.put(res.roomType, available - 1);

                System.out.println("Reservation confirmed: " + res);
            } else {
                System.out.println("No rooms available for " + res);
            }
        }
    }

    // Generate unique room ID
    private static String generateRoomId(String roomType) {
        Set<String> assigned = allocatedRooms.get(roomType);
        int id = 1;
        while (assigned.contains(roomType + id)) id++;
        return roomType + id;
    }

    // Save system state to file
    public static void saveState() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(new LinkedList<>(bookingQueue));
            oos.writeObject(new HashMap<>(roomInventory));
            oos.writeObject(new HashMap<>(allocatedRooms));
            System.out.println("\nSystem state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    // Load system state from file
    public static void loadState() {
        File f = new File(FILE_PATH);
        if (!f.exists()) {
            System.out.println("No previous system state found. Starting fresh.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            bookingQueue = (Queue<Reservation12>) ois.readObject();
            roomInventory = (Map<String, Integer>) ois.readObject();
            allocatedRooms = (Map<String, Set<String>>) ois.readObject();
            System.out.println("\nSystem state loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading system state: " + e.getMessage());
        }
    }

    public static void showInventory() {
        System.out.println("\nCurrent Inventory:");
        roomInventory.forEach((k, v) -> System.out.println(k + " -> " + v + " available"));
    }

    public static void main(String[] args) {
        loadState();

        // Add sample bookings
        addBookingRequest(new Reservation12("R501", "Alice", "Deluxe"));
        addBookingRequest(new Reservation12("R502", "Bob", "Suite"));
        addBookingRequest(new Reservation12("R503", "Charlie", "Standard"));

        showInventory();
        System.out.println("\nProcessing bookings...");
        processBookings();
        showInventory();

        // Save system state
        saveState();
    }
}