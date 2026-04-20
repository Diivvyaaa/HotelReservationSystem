package HotelReservationSystems.model;

import java.io.Serializable;


public class Guest implements Serializable {

    private String guestId;    // Unique ID (e.g., G001, G002)
    private String name;
    private String phone;
    private String email;


    public Guest(String guestId, String name, String phone, String email) {
        this.guestId = guestId;
        this.name    = name;
        this.phone   = phone;
        this.email   = email;
    }


    public String getGuestId() { return guestId; }
    public String getName()    { return name; }
    public String getPhone()   { return phone; }
    public String getEmail()   { return email; }


    public void setName(String name)   { this.name  = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return String.format("Guest ID: %-6s | Name: %-20s | Phone: %-13s | Email: %s",
                guestId, name, phone, email);
    }
}
