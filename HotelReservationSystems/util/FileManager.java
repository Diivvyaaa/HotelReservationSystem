package HotelReservationSystems.util;

import java.io.*;
import java.util.*;


public class FileManager {

    private static final String DATA_DIR = "hotel_data/";

    public static final String ROOMS_FILE        = DATA_DIR + "rooms.dat";
    public static final String GUESTS_FILE       = DATA_DIR + "guests.dat";
    public static final String RESERVATIONS_FILE = DATA_DIR + "reservations.dat";
    public static final String COUNTERS_FILE     = DATA_DIR + "counters.dat";

    static {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }


    public static <T> void saveData(List<T> data, String filePath) {
        // try-with-resources: stream is auto-closed after the block
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.out.println("⚠ Error saving data: " + e.getMessage());
        }
    }


    public static <T> List<T> loadData(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return new ArrayList<>(); // First run: no data yet

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<T>) ois.readObject(); // Deserialize back to List<T>
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("⚠ Error loading data: " + e.getMessage());
            return new ArrayList<>();
        }
    }


    public static void saveCounters(HashMap<String, Integer> counters) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(COUNTERS_FILE))) {
            oos.writeObject(counters);
        } catch (IOException e) {
            System.out.println("⚠ Error saving counters: " + e.getMessage());
        }
    }



    public static HashMap<String, Integer> loadCounters() {
        File file = new File(COUNTERS_FILE);
        if (!file.exists()) return new HashMap<>();
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(COUNTERS_FILE))) {
            return (HashMap<String, Integer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }
}
