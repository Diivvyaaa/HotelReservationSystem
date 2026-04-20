package HotelReservationSystems;

import HotelReservationSystems.system.HotelSystem;


public class Main {
    public static void main(String[] args) {
        HotelSystem hotel = new HotelSystem("Grand  Hotel");
        hotel.start();
    }
}
