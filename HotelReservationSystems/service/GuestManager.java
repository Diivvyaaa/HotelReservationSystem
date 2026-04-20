package HotelReservationSystems.service;

import HotelReservationSystems.model.Guest;
import HotelReservationSystems.util.ConsoleUI;
import HotelReservationSystems.util.FileManager;

import java.util.*;


public class GuestManager {

    private List<Guest>        guests;
    private Map<String, Guest> guestMap;
    private int                idCounter;
    private Scanner            scanner;


    public GuestManager(Scanner scanner) {
        this.scanner  = scanner;
        this.guests   = FileManager.loadData(FileManager.GUESTS_FILE);
        this.guestMap = new HashMap<>();


        for (Guest g : guests) {
            guestMap.put(g.getGuestId(), g);

            try {
                int num = Integer.parseInt(g.getGuestId().substring(1));
                if (num > idCounter) idCounter = num;
            } catch (Exception ignored) {}
        }
    }



    public Guest registerGuest() {
        ConsoleUI.printHeader("Register New Guest");
        try {
            ConsoleUI.prompt("Full Name");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) { ConsoleUI.error("Name cannot be empty."); return null; }

            ConsoleUI.prompt("Phone Number");
            String phone = scanner.nextLine().trim();

            ConsoleUI.prompt("Email Address");
            String email = scanner.nextLine().trim();


            idCounter++;
            String guestId = "G" + String.format("%03d", idCounter);

            Guest guest = new Guest(guestId, name, phone, email);
            guests.add(guest);
            guestMap.put(guestId, guest);
            save();

            ConsoleUI.success("Guest registered! ID: " + guestId);
            return guest;
        } catch (Exception e) {
            ConsoleUI.error("Error registering guest: " + e.getMessage());
            return null;
        }
    }

    public void viewAllGuests() {
        ConsoleUI.printHeader("All Registered Guests");
        if (guests.isEmpty()) { ConsoleUI.warn("No guests registered yet."); return; }
        System.out.println();
        for (Guest g : guests) {
            System.out.println("  " + g);
        }
        System.out.println();
        ConsoleUI.info("Total Guests: " + guests.size());
    }

    public void searchGuest() {
        ConsoleUI.printHeader("Search Guest");
        ConsoleUI.prompt("Enter Guest ID or Name");
        String query = scanner.nextLine().trim().toLowerCase();
        System.out.println();

        boolean found = false;
        for (Guest g : guests) {
            if (g.getGuestId().toLowerCase().equals(query) ||
                g.getName().toLowerCase().contains(query)) {
                System.out.println("  " + g);
                found = true;
            }
        }
        if (!found) ConsoleUI.warn("No guest found matching: " + query);
    }


    public void updateGuest() {
        ConsoleUI.printHeader("Update Guest Info");
        ConsoleUI.prompt("Enter Guest ID");
        String id = scanner.nextLine().trim().toUpperCase();
        Guest g = guestMap.get(id);
        if (g == null) { ConsoleUI.error("Guest not found."); return; }

        ConsoleUI.info("Leave blank to keep current value.");
        ConsoleUI.prompt("New Name [" + g.getName() + "]");
        String name = scanner.nextLine().trim();

        ConsoleUI.prompt("New Phone [" + g.getPhone() + "]");
        String phone = scanner.nextLine().trim();

        ConsoleUI.prompt("New Email [" + g.getEmail() + "]");
        String email = scanner.nextLine().trim();

        if (!name.isEmpty())  g.setName(name);
        if (!phone.isEmpty()) g.setPhone(phone);
        if (!email.isEmpty()) g.setEmail(email);

        save();
        ConsoleUI.success("Guest updated successfully.");
        System.out.println("  " + g);
    }

    public Guest getGuestById(String id) { return guestMap.get(id); }
    public List<Guest> getAllGuests()     { return guests; }


    public void save() {
        FileManager.saveData(guests, FileManager.GUESTS_FILE);
    }
}
