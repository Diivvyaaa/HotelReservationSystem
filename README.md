HOTEL RESERVATION SYSTEM

A fully console-based Hotel Reservation Management System built with Core Java, featuring real-time booking management, automated billing, and persistent data storage.

Built a full-stack console application in Core Java managing the complete hotel workflow — room booking, guest check-in/check-out, billing, and invoice generation.

Designed a 4-layer architecture (Model → Service → System → Util) separating data, business logic, controller, and helper concerns — mirroring real-world professional Java application structure.

Implemented dual File I/O strategies — binary Object Serialization (.dat files) for persistent data storage across sessions, and character-based PrintWriter/FileWriter for generating human-readable invoice receipts.

Applied OOP principles throughout — encapsulation for data protection, enums for type safety (RoomType, Reservation.Status), and object aggregation linking Guests and Rooms inside Reservation objects.

Used Java Collections Framework strategically — ArrayList for ordered data display and HashMap for O(1) constant-time lookup by room number and guest ID.

Delivered a polished console UI using ANSI escape codes for coloured, formatted terminal output — success/error/warning messages, section headers, and formatted bill receipts.
