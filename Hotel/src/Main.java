import java.text.*;
import java.util.*;
import java.io.*;

public class Main {
    public static Map<Integer, Map<String, Object>> rooms = new HashMap<>();

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to our hotel!");
            System.out.println("Please select what you want to do:");
            System.out.println("1-Make a reservation");
            System.out.println("2-List free rooms");
            System.out.println("3-Checkout room");
            System.out.println("4-Stats");
            System.out.println("5-Find a room");
            System.out.println("6-Update a room");
            System.out.println("0-Finish reservation");
            System.out.println("Enter your choice here: ");

            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                    makeReservation(scan);
                    break;
                case 2:
                    listFreeRooms();
                    break;
                case 3:
                    checkoutRoom(scan);
                    break;
                case 4:
                    displayStats(scan);
                    break;
                case 5:
                    findRoom(scan);
                    break;
                case 6:
                    updateRoom(scan);
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again!");
                    break;
            }
        }
        System.out.println("Goodbye!");
    }

    private static void makeReservation(Scanner scan) {
        System.out.println("Enter room number: ");
        int roomNum = scan.nextInt();
        scan.nextLine();
        if (!isValidRoomNumber(roomNum)) {
            System.out.println("Invalid room number. Try again!");
            System.out.println();
            return;
        }

        System.out.println("Start date(dd/mm/yyyy): ");
        String startDate = scan.nextLine();
        if (!isValidDate(startDate)) {
            System.out.println("Invalid date. Try again!");
            System.out.println();
            return;
        }

        System.out.println("End date(dd/mm/yyyy): ");
        String endDate = scan.nextLine();
        if (!isValidDate(endDate)) {
            System.out.println("Invalid date. Try again!");
            System.out.println();
            return;
        }

        if (startDate.compareTo(endDate) >= 0) {
            System.out.println("End date should be after the start date. Try again!");
            System.out.println();
            return;
        }

        Map<String, Object> existingRes = rooms.get(roomNum);
        if (existingRes != null) {
            String resStartDate = (String) existingRes.get("startDate");
            String resEndDate = (String) existingRes.get("endDate");

            if (resStartDate.compareTo(endDate) <= 0 || resEndDate.compareTo(startDate) >= 0) {
                System.out.println("This room is already reserved. Try another room!");
                System.out.println();
                return;
            }
        }

        System.out.println("Enter notes: ");
        String notes = scan.nextLine();

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("startDate", startDate);
        reservation.put("endDate", endDate);
        reservation.put("notes", notes);

        rooms.put(roomNum, reservation);

        System.out.println("Reservation was created!");
        System.out.println();
    }

    private static void listFreeRooms() {
        System.out.println("List of free rooms: ");

        Map<Integer, Boolean> freeRooms = new HashMap<>();

        for (int i = 1; i <= 4; i++) {
            String fileName = "RoomsWith" + i + "Beds.txt";

            try (Scanner fileScan = new Scanner(new File(fileName))) {
                while (fileScan.hasNextLine()) {
                    String roomNum = fileScan.nextLine();

                    if (!rooms.containsKey(Integer.parseInt(roomNum))) {
                        freeRooms.put(Integer.parseInt(roomNum), true);
                    }
                }
                System.out.println();
            } catch (FileNotFoundException ex) {
                System.out.println("File not found: " + fileName);
            }
        }

        List<Integer> sortedRooms = new ArrayList<>(freeRooms.keySet());
        Collections.sort(sortedRooms);

        for (Integer roomNum : sortedRooms) {
            System.out.println(roomNum);
        }
        System.out.println();
    }

    private static void checkoutRoom(Scanner scan) {
        System.out.println("Enter a room number to checkout: ");
        int roomNum = scan.nextInt();
        if (!isValidRoomNumber(roomNum)) {
            System.out.println("Invalid room number. Try again!");
            System.out.println();
            return;
        }

        boolean roomFound = false;

        for (Integer existingRoomNum : rooms.keySet()) {
            if (existingRoomNum == roomNum) {
                roomFound = true;
                break;
            }
        }

        if (roomFound) {
            rooms.remove(roomNum);
            System.out.println("Room" + roomNum + " has been checked out.");
        } else {
            System.out.println("Room " + roomNum + " is not reserved.");
        }
    }

    private static void displayStats(Scanner scan) {
        System.out.println("Start date(dd/mm/yyyy): ");
        String startDate = scan.nextLine();
        if (!isValidDate(startDate)) {
            System.out.println("Invalid date. Try again!");
            System.out.println();
            return;
        }

        System.out.println("End date(dd/mm/yyyy): ");
        String endDate = scan.nextLine();
        if (!isValidDate(endDate)) {
            System.out.println("Invalid date. Try again!");
            System.out.println();
            return;
        }

        if (startDate.compareTo(endDate) >= 0) {
            System.out.println("End date should be after the start date. Try again!");
            System.out.println();
            return;
        }

        for (int i = 1; i <= 4; i++) {
            String fileName = "RoomsWith" + i + "Beds.txt";
            try (Scanner fileScan = new Scanner(new File(fileName))) {
                while (fileScan.hasNextLine()) {
                    String line = fileScan.nextLine();
                    int roomNum = Integer.parseInt(line);
                    Map<String, Object> reservation = rooms.get(roomNum);

                    if (reservation != null) {
                        String resStartDate = (String) reservation.get("startDate");
                        String resEndDate = (String) reservation.get("endDate");

                        if (resStartDate.compareTo(endDate) < 0 || resEndDate.compareTo(startDate) > 0) {
                            int resDur = getResDur(resStartDate, resEndDate);
                            System.out.println("Room " + roomNum + ":" + resDur + " days");
                        }
                    }
                }

            } catch (FileNotFoundException ex) {
                System.out.println(ex.getMessage() + ":" + fileName);
            }
        }
        System.out.println();
    }

    private static void findRoom(Scanner scan) {
        System.out.println("Enter number of beds:");
        int beds = scan.nextInt();
        scan.nextLine();

        if (beds > 0 && beds < 5) {
            String fileName = "RoomsWith" + beds + "Beds.txt";
            System.out.println("Enter start date(dd/mm/yyyy):");
            String startDate = scan.nextLine();
            if (!isValidDate(startDate)) {
                System.out.println("Invalid date. Try again!");
                System.out.println();
                return;
            }

            System.out.println("Enter end date(dd/mm/yyyy):");
            String endDate = scan.nextLine();
            if (!isValidDate(endDate)) {
                System.out.println("Invalid date. Try again!");
                System.out.println();
                return;
            }

            if (startDate.compareTo(endDate) >= 0) {
                System.out.println("End date should be after the start date. Try again!");
                System.out.println();
                return;
            }

            System.out.println("Available rooms:");
            try (Scanner fileScan = new Scanner(new File(fileName))) {
                while (fileScan.hasNextLine()) {
                    String line = fileScan.nextLine();
                    int roomNum = Integer.parseInt(line);
                    Map<String, Object> reservation = rooms.get(roomNum);

                    if (reservation == null) {
                        System.out.println(roomNum);
                    } else {
                        String resStartDate = (String) reservation.get("startDate");
                        String resEndDate = (String) reservation.get("endDate");

                        if (resStartDate.compareTo(endDate) >= 0 || resEndDate.compareTo(startDate) <= 0) {
                            System.out.println(roomNum);
                        }
                    }
                }
                System.out.println();
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + fileName);
            }
        } else {
            System.out.println("The rooms are only with 1, 2, 3 or 4 beds. Try again!");
        }

    }

    private static void updateRoom(Scanner scan) {
        System.out.println("Enter room number to update: ");
        int roomNum = scan.nextInt();
        scan.nextLine();
        if (!isValidRoomNumber(roomNum)) {
            System.out.println("Invalid room number. Try again!");
            System.out.println();
            return;
        }

        if (rooms.containsKey(roomNum)) {
            System.out.println("Enter new notes: ");
            String newNotes = scan.nextLine();

            Map<String, Object> reservation = rooms.get(roomNum);
            reservation.put("notes", newNotes);

            System.out.println("Room " + roomNum + " has been updated.");
            System.out.println();
        } else {
            System.out.println("Room " + roomNum + " is not reserved.");
            System.out.println();
        }
    }

    private static boolean isValidRoomNumber(int roomNum) {
        return ((roomNum > 100 && roomNum < 116) || (roomNum > 200 && roomNum < 216) ||
                (roomNum > 300 && roomNum < 316) || (roomNum > 400 && roomNum < 516) || (roomNum > 500 && roomNum < 516));
    }

    private static boolean isValidDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
            return false;
        }

    }

    private static int getResDur(String startDate, String endDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);

            long diffInDays = (end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000);

            return (int) diffInDays;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return -1;
        }
    }
}
