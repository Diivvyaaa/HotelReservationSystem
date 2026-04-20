package HotelReservationSystems.service;

import HotelReservationSystems.model.Room;
import HotelReservationSystems.model.Room.RoomType;
import HotelReservationSystems.util.ConsoleUI;
import HotelReservationSystems.util.FileManager;

import java.util.*;


public class RoomManager {


    private List<Room> rooms;

    private Map<Integer, Room> roomMap;

    private Scanner scanner;


    public RoomManager(Scanner scanner) {
        this.scanner = scanner;

        this.rooms   = FileManager.loadData(FileManager.ROOMS_FILE);

        this.roomMap = new HashMap<>();
        for (Room r : rooms) roomMap.put(r.getRoomNumber(), r);

        if (rooms.isEmpty()) seedRooms();
    }


    private void seedRooms() {
        int[][] config = {
            {101, 1500}, {102, 1500}, {103, 1500},
            {201, 2500}, {202, 2500}, {203, 2500},
            {301, 4000}, {302, 4000}, {303, 4000},
            {401, 7000}, {402, 7000}, {403, 7000}
        };
        RoomType[] types = {
            RoomType.SINGLE, RoomType.SINGLE, RoomType.SINGLE,
            RoomType.DOUBLE, RoomType.DOUBLE, RoomType.DOUBLE,
            RoomType.DELUXE, RoomType.DELUXE, RoomType.DELUXE,
            RoomType.SUITE,  RoomType.SUITE,  RoomType.SUITE
        };
        for (int i = 0; i < config.length; i++) {
            addRoom(new Room(config[i][0], types[i], config[i][1]), false);
        }
        save();
    }


    public void addRoomInteractive() {
        ConsoleUI.printHeader("Add New Room");
        try {
            ConsoleUI.prompt("Room Number");
            int num = Integer.parseInt(scanner.nextLine().trim());
            if (roomMap.containsKey(num)) {
                ConsoleUI.error("Room " + num + " already exists!");
                return;
            }

            System.out.println("\n  Room Types:");
            ConsoleUI.menuOption("1", "SINGLE  – Rs.1500/night");
            ConsoleUI.menuOption("2", "DOUBLE  – Rs.2500/night");
            ConsoleUI.menuOption("3", "DELUXE  – Rs.4000/night");
            ConsoleUI.menuOption("4", "SUITE   – Rs.7000/night");
            ConsoleUI.prompt("Room Type (1-4)");
            int typeChoice = Integer.parseInt(scanner.nextLine().trim());
            RoomType[] types = RoomType.values();
            if (typeChoice < 1 || typeChoice > 4) { ConsoleUI.error("Invalid type."); return; }

            ConsoleUI.prompt("Price Per Night (Rs.)");
            double price = Double.parseDouble(scanner.nextLine().trim());

            Room room = new Room(num, types[typeChoice - 1], price);
            addRoom(room, true);
        } catch (NumberFormatException e) {
            ConsoleUI.error("Invalid input. Please enter numbers only.");
        }
    }

    private void addRoom(Room room, boolean verbose) {
        rooms.add(room);
        roomMap.put(room.getRoomNumber(), room);
        if (verbose) {
            save();
            ConsoleUI.success("Room " + room.getRoomNumber() + " added successfully!");
        }
    }


    public void viewAllRooms() {
        ConsoleUI.printHeader("All Rooms");
        if (rooms.isEmpty()) { ConsoleUI.warn("No rooms found."); return; }

        System.out.println();
        // Enhanced for-loop iterates over every Room in the ArrayList
        for (Room room : rooms) {
            System.out.println("  " + room);
        }
        System.out.println();
        ConsoleUI.info("Total Rooms: " + rooms.size());
    }


    public void viewAvailableRooms() {
        ConsoleUI.printHeader("Available Rooms");
        System.out.println();
        long count = rooms.stream()
            .filter(Room::isAvailable)
            .peek(room -> System.out.println("  " + room))
            .count();
        System.out.println();
        if (count == 0) ConsoleUI.warn("No rooms available at the moment.");
        else ConsoleUI.info("Available: " + count + " room(s)");
    }


    public void updateRoomPrice() {
        ConsoleUI.printHeader("Update Room Price");
        ConsoleUI.prompt("Enter Room Number");
        try {
            int num = Integer.parseInt(scanner.nextLine().trim());
            Room room = roomMap.get(num);
            if (room == null) { ConsoleUI.error("Room not found."); return; }

            ConsoleUI.info("Current price: Rs." + room.getPricePerNight());
            ConsoleUI.prompt("New Price Per Night (Rs.)");
            double newPrice = Double.parseDouble(scanner.nextLine().trim());
            room.setPricePerNight(newPrice);
            save();
            ConsoleUI.success("Price updated to Rs." + newPrice);
        } catch (NumberFormatException e) {
            ConsoleUI.error("Invalid input.");
        }
    }


    public void searchByType() {
        ConsoleUI.printHeader("Search Rooms by Type");
        System.out.println("\n  Types: 1-SINGLE  2-DOUBLE  3-DELUXE  4-SUITE");
        ConsoleUI.prompt("Enter Type (1-4)");
        try {
            int c = Integer.parseInt(scanner.nextLine().trim());
            RoomType[] types = RoomType.values();
            if (c < 1 || c > 4) { ConsoleUI.error("Invalid."); return; }
            RoomType selected = types[c - 1];
            System.out.println();
            rooms.stream()
                 .filter(r -> r.getRoomType() == selected)
                 .forEach(r -> System.out.println("  " + r));
        } catch (NumberFormatException e) {
            ConsoleUI.error("Invalid input.");
        }
    }


    public Room getRoomByNumber(int num) { return roomMap.get(num); }
    public List<Room> getAllRooms()      { return rooms; }


    public void save() {
        FileManager.saveData(rooms, FileManager.ROOMS_FILE);
    }
}
