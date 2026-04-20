package HotelReservationSystems.system;

import HotelReservationSystems.service.GuestManager;
import HotelReservationSystems.service.ReservationManager;
import HotelReservationSystems.service.RoomManager;
import HotelReservationSystems.util.ConsoleUI;

import java.util.Scanner;


public class HotelSystem {

    private String             hotelName;
    private Scanner            scanner;
    private RoomManager        roomManager;
    private GuestManager       guestManager;
    private ReservationManager reservationManager;


    public HotelSystem(String hotelName) {
        this.hotelName = hotelName;
        this.scanner   = new Scanner(System.in);

        this.roomManager        = new RoomManager(scanner);
        this.guestManager       = new GuestManager(scanner);
        this.reservationManager = new ReservationManager(scanner, roomManager, guestManager, hotelName);
    }


    public void start() {
        while (true) {
            showMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1": reservationManager.bookRoom();
                break;
                case "2": reservationManager.checkIn();
                break;
                case "3": reservationManager.checkOut();
                break;
                case "4": reservationManager.cancelReservation();
                break;
                case "5": reservationManager.viewAllReservations();
                break;
                case "6": reservationManager.viewActiveReservations();
                break;
                case "7": reservationManager.viewByGuest();
                break;
                case "8":  roomManager.viewAllRooms();
                break;
                case "9":  roomManager.viewAvailableRooms();
                break;
                case "10": roomManager.addRoomInteractive();
                break;
                case "11": roomManager.updateRoomPrice();
                break;
                case "12": roomManager.searchByType();
                break;
                case "13": guestManager.viewAllGuests();
                break;
                case "14": guestManager.searchGuest();
                break;
                case "15": guestManager.updateGuest();
                break;
                case "16": guestManager.registerGuest();
                break;
                case "0":
                    ConsoleUI.printBanner(hotelName);
                    ConsoleUI.success("Thank you!  Goodbye.");
                    System.out.println();
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    ConsoleUI.error("Invalid choice. Please enter a number from the menu.");
            }

            ConsoleUI.pressEnter();
        }
    }


    private void showMainMenu() {
        ConsoleUI.printBanner(hotelName);

        System.out.println();
        System.out.println(ConsoleUI.CYAN + ConsoleUI.BOLD + "  ─── RESERVATIONS ───" + ConsoleUI.RESET);
        ConsoleUI.menuOption("1",  "Book a Room");
        ConsoleUI.menuOption("2",  "Check-In");
        ConsoleUI.menuOption("3",  "Check-Out & Generate Bill");
        ConsoleUI.menuOption("4",  "Cancel Reservation");

        System.out.println();
        System.out.println(ConsoleUI.CYAN + ConsoleUI.BOLD + "  ─── VIEW RESERVATIONS ───" + ConsoleUI.RESET);
        ConsoleUI.menuOption("5",  "View All Reservations");
        ConsoleUI.menuOption("6",  "View Active Reservations");
        ConsoleUI.menuOption("7",  "View Reservations by Guest");

        System.out.println();
        System.out.println(ConsoleUI.CYAN + ConsoleUI.BOLD + "  ─── ROOMS ───" + ConsoleUI.RESET);
        ConsoleUI.menuOption("8",  "View All Rooms");
        ConsoleUI.menuOption("9",  "View Available Rooms");
        ConsoleUI.menuOption("10", "Add New Room");
        ConsoleUI.menuOption("11", "Update Room Price");
        ConsoleUI.menuOption("12", "Search Rooms by Type");

        System.out.println();
        System.out.println(ConsoleUI.CYAN + ConsoleUI.BOLD + "  ─── GUESTS ───" + ConsoleUI.RESET);
        ConsoleUI.menuOption("13", "View All Guests");
        ConsoleUI.menuOption("14", "Search Guest");
        ConsoleUI.menuOption("15", "Update Guest Info");
        ConsoleUI.menuOption("16", "Register New Guest");

        System.out.println();
        ConsoleUI.menuOption("0",  "Exit");
        System.out.println();

        ConsoleUI.prompt("Your Choice");
    }
}
