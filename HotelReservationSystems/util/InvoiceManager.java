package HotelReservationSystems.util;

import HotelReservationSystems.model.Reservation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class InvoiceManager {

    private static final String INVOICE_DIR = "hotel_data/invoices/";
    private static final DateTimeFormatter DT_FMT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");


    static {
        java.io.File dir = new java.io.File(INVOICE_DIR);
        if (!dir.exists()) dir.mkdirs();
    }


    public static String saveInvoice(Reservation res, String hotelName) {

        // Build file name: INV_R001_JohnDoe.txt  (spaces replaced with underscores)
        String guestNameSafe = res.getGuest().getName().replace(" ", "_");
        String fileName = INVOICE_DIR + "INV_" + res.getReservationId()
                          + "_" + guestNameSafe + ".txt";

        // try-with-resources: PrintWriter wraps FileWriter for easy line-by-line writing
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {

            String line  = "=".repeat(46);
            String dash  = "-".repeat(46);

            pw.println(line);
            pw.println(center(hotelName.toUpperCase(), 46));
            pw.println(center("INVOICE / BILL RECEIPT", 46));
            pw.println(center("Generated: " + LocalDateTime.now().format(DT_FMT), 46));
            pw.println(line);
            pw.println();


            pw.println("  RESERVATION DETAILS");
            pw.println(dash);
            pw.printf("  %-22s : %s%n", "Reservation ID",   res.getReservationId());
            pw.printf("  %-22s : %s%n", "Guest Name",       res.getGuest().getName());
            pw.printf("  %-22s : %s%n", "Phone",            res.getGuest().getPhone());
            pw.printf("  %-22s : %s%n", "Email",            res.getGuest().getEmail());
            pw.println();


            pw.println("  ROOM DETAILS");
            pw.println(dash);
            pw.printf("  %-22s : %d%n",   "Room Number",    res.getRoom().getRoomNumber());
            pw.printf("  %-22s : %s%n",   "Room Type",      res.getRoom().getRoomType());
            pw.printf("  %-22s : Rs. %.2f%n", "Rate / Night", res.getRoom().getPricePerNight());
            pw.println();


            pw.println("  STAY DETAILS");
            pw.println(dash);
            pw.printf("  %-22s : %s%n", "Check-In Date",    res.getCheckInDate());
            pw.printf("  %-22s : %s%n", "Check-Out Date",   res.getCheckOutDate());
            pw.printf("  %-22s : %d night(s)%n", "Duration", res.getNumberOfNights());
            pw.println();


            pw.println(dash);
            pw.printf("  %-22s : %d night(s) x Rs. %.2f%n",
                    "Calculation",
                    res.getNumberOfNights(),
                    res.getRoom().getPricePerNight());
            pw.println(line);
            pw.printf("  %-22s : Rs. %.2f%n", "TOTAL AMOUNT DUE", res.getTotalBill());
            pw.printf("  %-22s : %s%n",        "Payment Status",
                      res.isBillPaid() ? "PAID ✔" : "PENDING ✘");
            pw.println(line);
            pw.println();
            pw.println(center("Thank you for staying with us!", 46));
            pw.println(center("We hope to see you again soon.", 46));
            pw.println();
            pw.println(line);

        } catch (IOException e) {
            ConsoleUI.error("Could not save invoice: " + e.getMessage());
            return null;
        }

        return fileName;
    }


    private static String center(String text, int width) {
        int pad  = Math.max(0, width - text.length());
        int left = pad / 2;
        return " ".repeat(left) + text;
    }
}
