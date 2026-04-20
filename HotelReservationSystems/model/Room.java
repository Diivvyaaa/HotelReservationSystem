package HotelReservationSystems.model;

import java.io.Serializable;


public class Room implements Serializable {


    public enum RoomType {
        SINGLE, DOUBLE, DELUXE, SUITE
    }


    private int roomNumber;
    private RoomType roomType;
    private double pricePerNight;
    private boolean isAvailable;


    public Room(int roomNumber, RoomType roomType, double pricePerNight) {
        this.roomNumber   = roomNumber;
        this.roomType     = roomType;
        this.pricePerNight = pricePerNight;
        this.isAvailable  = true;
    }


    public int getRoomNumber()       { return roomNumber; }
    public RoomType getRoomType()    { return roomType; }
    public double getPricePerNight() { return pricePerNight; }
    public boolean isAvailable()     { return isAvailable; }


    public void setAvailable(boolean available) { this.isAvailable = available; }
    public void setPricePerNight(double price)  { this.pricePerNight = price; }


    @Override
    public String toString() {
        return String.format("Room %-4d | %-8s | Rs.%-8.2f/night | %s",
                roomNumber,
                roomType,
                pricePerNight,
                isAvailable ? " Available" : " Occupied");
    }
}
