package HotelReservationSystems.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;



public class Reservation implements Serializable {


    public enum Status { BOOKED, CHECKED_IN, CHECKED_OUT, CANCELLED }


    private String        reservationId;
    private Guest         guest;
    private Room          room;
    private LocalDate     checkInDate;
    private LocalDate     checkOutDate;
    private Status        status;
    private double        totalBill;
    private boolean       billPaid;


    public Reservation(String reservationId, Guest guest, Room room,
                       LocalDate checkInDate, LocalDate checkOutDate) {
        this.reservationId = reservationId;
        this.guest         = guest;
        this.room          = room;
        this.checkInDate   = checkInDate;
        this.checkOutDate  = checkOutDate;
        this.status        = Status.BOOKED;
        this.billPaid      = false;
        this.totalBill     = calculateBill();
    }

    public double calculateBill() {
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (nights <= 0) nights = 1;
        this.totalBill = room.getPricePerNight() * nights;
        return this.totalBill;
    }

    public long getNumberOfNights() {
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return nights <= 0 ? 1 : nights;
    }

    public String    getReservationId() { return reservationId; }
    public Guest     getGuest()         { return guest; }
    public Room      getRoom()          { return room; }
    public LocalDate getCheckInDate()   { return checkInDate; }
    public LocalDate getCheckOutDate()  { return checkOutDate; }
    public Status    getStatus()        { return status; }
    public double    getTotalBill()     { return totalBill; }
    public boolean   isBillPaid()       { return billPaid; }


    public void setStatus(Status status)    { this.status   = status; }
    public void setBillPaid(boolean paid)   { this.billPaid = paid; }
    public void setCheckOutDate(LocalDate d){ this.checkOutDate = d; calculateBill(); }


    @Override
    public String toString() {
        return String.format(
            "Reservation ID : %s\n" +
            "Guest          : %s (%s)\n" +
            "Room           : %d (%s)\n" +
            "Check-In       : %s\n" +
            "Check-Out      : %s\n" +
            "Nights         : %d\n" +
            "Total Bill     : Rs. %.2f\n" +
            "Status         : %s\n" +
            "Bill Paid      : %s",
            reservationId,
            guest.getName(), guest.getPhone(),
            room.getRoomNumber(), room.getRoomType(),
            checkInDate, checkOutDate,
            getNumberOfNights(),
            totalBill,
            status,
            billPaid ? "Yes" : "No"
        );
    }
}
