package rentals;

import java.util.Random;

/**
 * Created by charliesawyer on 9/15/16.
 */
public class VideoStoreUtils {
    private static String firstNames[] = {
        "Tom", "Dick", "Miles", "Harry", "Sol", "George", "David", "John", "Etc", "Viz", "Ibid", "Sid"
    };
    private static String lastNames[] = {
      "Jones", "Smith", "Harrison", "Star", "Lennon", "McCartney", "Dylan", "Lincoln", "Alinsky",
            "Davis"
    };
    public static void createAccounts(VideoStore store) {
        for (String firstName : firstNames) {
            for (String lastName : lastNames) {
                store.createAccount(firstName, lastName, makeEmail(firstName, lastName));
            }
        }
    }
    private static String makeEmail(String firstName, String lastName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(firstName.substring(0,1))
                .append(lastName)
                .append("@gmail.com");
        return stringBuilder.toString();
    }
    static public void rentSomeVideos(VideoStore store, int numberToMake)
            throws Exception {
        for (int i = 0; i < numberToMake; i++) {
            Random randy = new Random();
            String email =
                    makeEmail(firstNames[randy.nextInt(firstNames.length)],
                    lastNames[randy.nextInt(lastNames.length)]);
            Account account = store.getAccount(email);
            Video video = store.getRandomAvailableVideo();
            VideoRental rental = new VideoRental(video, account);
            store.addRental(rental);
        }
    }
    static public Account rentVideosToRandomAccount(VideoStore store, int numberToMake)
            throws Exception {
        Random randy = new Random();
        String email = makeEmail(firstNames[randy.nextInt(firstNames.length)],
                lastNames[randy.nextInt(lastNames.length)]);
        Account account = store.getAccount(email);
        for (int i = 0; i < numberToMake; i++) {
            Video video = store.getRandomAvailableVideo();
            VideoRental rental = new VideoRental(video, account);
            store.addRental(rental);
        }
        return account;
    }
    public static void main(String argv[]) throws Exception {
        VideoStore store = new VideoStore();
        createAccounts(store);
        rentSomeVideos(store, 10);
        Account anAccount = store.getAccount("VAlinsky@gmail.com");
        store.rentVideo("JAWS", "VAlinsky@gmail.com");
        store.rentVideo("CITIZEN KANE", "VAlinsky@gmail.com");
        boolean hasRentedIt = store.hasRentedVideo("JAWS", "VAlinsky@gmail.com");
        for (VideoRental videoRental : store.getOverdueRentals()) {
            System.out.println(videoRental);
        }
        anAccount.getOpenRentals().stream().forEach(System.out::println);
        store.settleAccount("VAlinsky@gmail.com");
        anAccount.getOpenRentals().stream().forEach(System.out::println);
//        store.getOpenRentals().stream()
//                .map(s -> s.getAccount())
//                .map(s -> s.getEmail()).forEach(System.out::println);
    }
}
