import java.util.*;

public class Main {
    private static Map<Integer, Map<String, Object>> rooms = new HashMap<>();

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("1-Make a reservation");
            System.out.println("2-List free rooms");
            System.out.println("3-Checkout room");
            System.out.println("4-Stats");
            System.out.println("5-Find a room");
            System.out.println("6-Update a room");
            System.out.println("Please select what you want to do:");
            
            int choice = scan.nextInt();

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
                default:
                    System.out.println("Invalid choice. Please try again!");
                    break;
            }
        }
        System.out.println("Goodbye");
    }
    private static void makeReservation(Scanner scan){
        System.out.println("Enter room number: ");
        int roomNum = scan.nextInt();
        scan.nextLine();

        System.out.println("Start date(dd/mm/yyyy): ");
        String startDate=scan.nextLine();

        System.out.println("End date(dd/mm/yyyy): ");
        String endDate=scan.nextLine();

        System.out.println("Enter notes: ");
        String notes=scan.nextLine();

        Map<String, Object> reservation =new HashMap<>();
        reservation.put("startDate", startDate);
        reservation.put("endDate", endDate);
        reservation.put("notes", notes);

        rooms.put(roomNum, reservation);

        System.out.println("Reservation was created!");
        System.out.println("");
    }
    private static void listFreeRooms(){

    }
    private static void checkoutRoom(Scanner scan){
        System.out.println("Enter a room number to checkout: ");
        int roomNum = scan.nextInt();

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
    private static void displayStats(Scanner scan){

    }
    private static void findRoom(Scanner scan){
System.out.println("Enter number of beds:");
        int beds = scan.nextInt();
        scan.nextLine();

        System.out.println("Enter start date(dd/mm/yyyy):");
        String startDate = scan.nextLine();

        System.out.println("Enter end date(dd/mm/yyyy):");
        String endDate = scan.nextLine();

        System.out.println("Available rooms: "); /
        for (Map.Entry<Integer, Map<String, Object>> roomEntry : rooms.entrySet()) {
            int roomNum = roomEntry.getKey();
            Map<String, Object> reservation = roomEntry.getValue();

            if (reservation == null && hasEnoughBeds(roomNum, beds)) {
                System.out.println(roomNum);
            }
        }
    }
    private static void updateRoom(Scanner scan){

    }
    private static boolean hasEnoughBeds(int roomNum, int beds) {
        return true;
    }
}
