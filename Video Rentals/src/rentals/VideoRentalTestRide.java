package rentals;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toSet;

/**
 * Created by charliesawyer on 10/20/16.
 */
public class VideoRentalTestRide {
    public static void main(String argv[]) throws Exception {
        VideoStore videoStore = new VideoStore();
        VideoStoreUtils.createAccounts(videoStore);
        // Create 10 Random VideoRental's
        VideoStoreUtils.rentSomeVideos(videoStore, 10);
        Set<VideoRental> rentals = videoStore.getRentals();
        System.out.println(">>> Ten Rentals: ");
        // Watch out! Java8 Streams code
        rentals.stream().forEach(System.out::println);
        // Stream the rentals and extract the Accounts
        Set<Account> accountsWithRentals = rentals.stream()
                .map(rental -> rental.getAccount())
                .collect(toSet());
        System.out.println("\n>>> Accounts that rented them: ");
        accountsWithRentals.stream().forEach(System.out::println);
        // Stream the rentals and extract all the rented Videos
        Set<Video> rentedVideos = rentals.stream()
                .map(r -> r.getVideo())
                .collect(toSet());
        System.out.println("\n>>> The Video's rented by those Accounts: ");
        rentedVideos.stream().forEach(System.out::println);
        // Iterate over Accounts with rentals and settle them
        System.out.println("Number of open rentals = " +
                rentals.stream().filter(VideoRental::isOpen).collect(counting()));
        accountsWithRentals.stream()
                .forEach(videoStore::settleAccount);
        System.out.println("\n>>> After settling the Accounts: ");
        rentedVideos.stream().forEach(System.out::println);
        System.out.println("Number open rentals = " +
        rentals.stream().filter(VideoRental::isOpen).collect(counting()));
    }
}
