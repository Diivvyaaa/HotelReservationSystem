package HotelReservationSystems.service;

import HotelReservationSystems.model.Guest;
import HotelReservationSystems.model.Reservation;
import HotelReservationSystems.model.Reservation.Status;
import HotelReservationSystems.model.Room;
import HotelReservationSystems.util.ConsoleUI;
import HotelReservationSystems.util.FileManager;
import HotelReservationSystems.util.InvoiceManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;


public class ReservationManager {


    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");


    private List<Reservation>        reservations;
    private Map<String, Reservation> reservationMap;
    private int                      idCounter;


    private RoomManager  roomManager;
    private GuestManager guestManager;
    private Scanner      scanner;
    private String       hotelName;


    public ReservationManager(Scanner scanner,
                               RoomManager roomManager,
                               GuestManager guestManager,
                               String hotelName) {
        this.scanner      = scanner;
        this.roomManager  = roomManager;
        this.guestManager = guestManager;
        this.hotelName    = hotelName;

        this.reservations   = FileManager.loadData(FileManager.RESERVATIONS_FILE);
        this.reservationMap = new HashMap<>();

        for (Reservation r : reservations) {
            reservationMap.put(r.getReservationId(), r);
            try {
                int num = Integer.parseInt(r.getReservationId().substring(1));
                if (num > idCounter) idCounter = num;
            } catch (Exception ignored) {}
        }
    }



    public void bookRoom() {
        ConsoleUI.printHeader("Book a Room");


        Guest guest = selectOrRegisterGuest();
        if (guest == null) return;


        System.out.println();
        ConsoleUI.info("Available Rooms:");
        roomManager.getAllRooms().stream()
            .filter(Room::isAvailable)
            .forEach(r -> System.out.println("    " + r));
        System.out.println();


        ConsoleUI.prompt("Enter Room Number");
        int roomNum;
        try { roomNum = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { ConsoleUI.error("Invalid room number."); return; }

        Room room = roomManager.getRoomByNumber(roomNum);
        if (room == null)          { ConsoleUI.error("Room not found."); return; }
        if (!room.isAvailable())   { ConsoleUI.error("Room is not available."); return; }


        LocalDate checkIn  = readDate("Check-In Date  (yyyy-MM-dd)");
        if (checkIn == null) return;
        LocalDate checkOut = readDate("Check-Out Date (yyyy-MM-dd)");
        if (checkOut == null) return;


        if (!checkOut.isAfter(checkIn)) {
            ConsoleUI.error("Check-out date must be after check-in date.");
            return;
        }


        idCounter++;
        String resId = "R" + String.format("%03d", idCounter);

        Reservation reservation = new Reservation(resId, guest, room, checkIn, checkOut);


        room.setAvailable(false);
        roomManager.save();

        reservations.add(reservation);
        reservationMap.put(resId, reservation);
        save();


        System.out.println();
        ConsoleUI.success("Reservation confirmed!");
        ConsoleUI.divider();
        System.out.println("  Reservation ID : " + resId);
        System.out.println("  Guest          : " + guest.getName());
        System.out.println("  Room           : " + room.getRoomNumber() + " (" + room.getRoomType() + ")");
        System.out.println("  Check-In       : " + checkIn);
        System.out.println("  Check-Out      : " + checkOut);
        System.out.println("  Nights         : " + reservation.getNumberOfNights());
        ConsoleUI.printBillTotal(reservation.getTotalBill());
    }

    public void checkIn() {
        ConsoleUI.printHeader("Guest Check-In");
        Reservation res = findReservation();
        if (res == null) return;

        if (res.getStatus() != Status.BOOKED) {
            ConsoleUI.error("Cannot check in. Status is: " + res.getStatus());
            return;
        }

        res.setStatus(Status.CHECKED_IN);
        save();
        ConsoleUI.success("Guest " + res.getGuest().getName() + " checked into Room "
                          + res.getRoom().getRoomNumber());
    }


    public void checkOut() {
        ConsoleUI.printHeader("Guest Check-Out & Billing");
        Reservation res = findReservation();
        if (res == null) return;

        if (res.getStatus() != Status.CHECKED_IN) {
            ConsoleUI.error("Guest is not checked in. Status: " + res.getStatus());
            return;
        }


        printBill(res);


        ConsoleUI.prompt("Mark bill as paid? (y/n)");
        String ans = scanner.nextLine().trim().toLowerCase();
        if (ans.equals("y")) {
            res.setBillPaid(true);
            ConsoleUI.success("Payment recorded. Thank you!");
        }


        res.setStatus(Status.CHECKED_OUT);
        res.getRoom().setAvailable(true);
        roomManager.save();
        save();


        String invoicePath = InvoiceManager.saveInvoice(res, hotelName);
        if (invoicePath != null) {
            ConsoleUI.success("Invoice saved to: " + invoicePath);
        }

        ConsoleUI.success("Check-out complete for " + res.getGuest().getName());
    }


    public void cancelReservation() {
        ConsoleUI.printHeader("Cancel Reservation");
        Reservation res = findReservation();
        if (res == null) return;

        if (res.getStatus() == Status.CHECKED_OUT || res.getStatus() == Status.CANCELLED) {
            ConsoleUI.error("Cannot cancel. Status: " + res.getStatus());
            return;
        }

        ConsoleUI.warn("Are you sure you want to cancel reservation " + res.getReservationId() + "? (y/n)");
        if (!scanner.nextLine().trim().equalsIgnoreCase("y")) {
            ConsoleUI.info("Cancellation aborted.");
            return;
        }

        res.setStatus(Status.CANCELLED);
        res.getRoom().setAvailable(true); // Free the room
        roomManager.save();
        save();
        ConsoleUI.success("Reservation " + res.getReservationId() + " cancelled.");
    }


    public void viewAllReservations() {
        ConsoleUI.printHeader("All Reservations");
        if (reservations.isEmpty()) { ConsoleUI.warn("No reservations found."); return; }
        for (Reservation r : reservations) {
            ConsoleUI.divider();
            System.out.println(r);
        }
        ConsoleUI.divider();
        ConsoleUI.info("Total: " + reservations.size() + " reservation(s)");
    }


    public void viewActiveReservations() {
        ConsoleUI.printHeader("Active Reservations");
        List<Reservation> active = reservations.stream()
            .filter(r -> r.getStatus() == Status.BOOKED ||
                         r.getStatus() == Status.CHECKED_IN)
            .collect(Collectors.toList());

        if (active.isEmpty()) { ConsoleUI.warn("No active reservations."); return; }
        for (Reservation r : active) {
            ConsoleUI.divider();
            System.out.println(r);
        }
        ConsoleUI.divider();
    }


    public void viewByGuest() {
        ConsoleUI.printHeader("Reservations by Guest");
        ConsoleUI.prompt("Enter Guest ID or Name");
        String query = scanner.nextLine().trim().toLowerCase();

        List<Reservation> result = reservations.stream()
            .filter(r -> r.getGuest().getGuestId().toLowerCase().contains(query) ||
                         r.getGuest().getName().toLowerCase().contains(query))
            .collect(Collectors.toList());

        if (result.isEmpty()) { ConsoleUI.warn("No reservations found for: " + query); return; }
        for (Reservation r : result) {
            ConsoleUI.divider();
            System.out.println(r);
        }
        ConsoleUI.divider();
    }


    private void printBill(Reservation res) {
        ConsoleUI.divider();
        System.out.println(ConsoleUI.YELLOW + ConsoleUI.BOLD + "                 INVOICE / BILL" + ConsoleUI.RESET);
        ConsoleUI.divider();
        ConsoleUI.printBillRow("Reservation ID :", res.getReservationId());
        ConsoleUI.printBillRow("Guest Name     :", res.getGuest().getName());
        ConsoleUI.printBillRow("Phone          :", res.getGuest().getPhone());
        ConsoleUI.printBillRow("Room Number    :", String.valueOf(res.getRoom().getRoomNumber()));
        ConsoleUI.printBillRow("Room Type      :", res.getRoom().getRoomType().toString());
        ConsoleUI.printBillRow("Check-In Date  :", res.getCheckInDate().toString());
        ConsoleUI.printBillRow("Check-Out Date :", res.getCheckOutDate().toString());
        ConsoleUI.printBillRow("Number of Nights:", res.getNumberOfNights() + " night(s)");
        ConsoleUI.printBillRow("Rate / Night   :", "Rs. " + String.format("%.2f", res.getRoom().getPricePerNight()));
        ConsoleUI.divider();
        ConsoleUI.printBillTotal(res.getTotalBill());
    }


    private Reservation findReservation() {
        ConsoleUI.prompt("Enter Reservation ID (e.g. R001)");
        String id = scanner.nextLine().trim().toUpperCase();
        Reservation res = reservationMap.get(id);
        if (res == null) ConsoleUI.error("Reservation " + id + " not found.");
        return res;
    }


    private Guest selectOrRegisterGuest() {
        System.out.println("\n  Is this guest registered?");
        ConsoleUI.menuOption("1", "Yes – enter Guest ID");
        ConsoleUI.menuOption("2", "No  – register new guest");
        ConsoleUI.prompt("Choice (1-2)");
        String c = scanner.nextLine().trim();

        if (c.equals("1")) {
            ConsoleUI.prompt("Guest ID");
            String id = scanner.nextLine().trim().toUpperCase();
            Guest g = guestManager.getGuestById(id);
            if (g == null) { ConsoleUI.error("Guest not found."); return null; }
            ConsoleUI.info("Guest found: " + g.getName());
            return g;
        } else {
            return guestManager.registerGuest();
        }
    }


    private LocalDate readDate(String label) {
        ConsoleUI.prompt(label);
        try {
            return LocalDate.parse(scanner.nextLine().trim(), DATE_FMT);
        } catch (DateTimeParseException e) {
            ConsoleUI.error("Invalid date format. Use yyyy-MM-dd (e.g. 2025-12-25)");
            return null;
        }
    }


    public void save() {
        FileManager.saveData(reservations, FileManager.RESERVATIONS_FILE);
    }
}
