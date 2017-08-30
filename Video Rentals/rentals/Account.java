package rentals;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toSet;

/**
 * Created by charliesawyer on 9/15/16.
 */
public class Account {
    private final String firstName;
    private final String lastName;
    private String email;
    private Set<VideoRental> openRentals = new HashSet();
    private Set<VideoRental> closedRentals = new HashSet();
    private final int id;

    public Account(String firstName, String lastName, String email, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.id = id;
    }
    public int getID() { return id; }
    public String getEmail() { return email; }
    void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (getClass() != other.getClass()) {
            return false;
        }
        Account otherAccount = (Account) other;
        return id == otherAccount.getID() &&
                firstName.equalsIgnoreCase(otherAccount.firstName) &&
                lastName.equalsIgnoreCase(((Account) other).lastName) &&
                email.equalsIgnoreCase(otherAccount.email);
    }

    @Override
    public int hashCode() {
        return id * firstName.hashCode() *
                lastName.hashCode() * email.hashCode();
    }

    public Set<VideoRental> getOpenRentals() {
        return openRentals;
    }
    public Set<VideoRental> getClosedRentals() {
        return closedRentals;
    }
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(firstName)
                .append(" ")
                .append(lastName)
                .append(" ")
                .append(email);
        return stringBuilder.toString();
    }

    public void addRental(VideoRental rental) {
        //since video coming in is unavailable, check to see if already exists in openRentals
        //and that the rental is not coming from another person's account
        if (equals(rental.getAccount()) && !openRentals.contains(rental)) {
            openRentals.add(rental);
        } else if ( openRentals.contains(rental) ) {
            throw new IllegalArgumentException( rental.getVideo().getTitle() + "was already checked out by this " +
                    "account");
        } else {
            throw new IllegalArgumentException(" Cannot add a rental from another account");
        }

    }

    public boolean hasOpenRental(String title) {
        Long count = openRentals.stream()
                .filter(r -> r.getVideo().getTitle().equalsIgnoreCase(title))
                .collect(counting());
        return count == 1;
    }
    public Set<VideoRental> getOverdueRentals() {

        return openRentals.stream()
                .filter(VideoRental::isOverDue)
                .collect(toSet());
    }
    public void settleRental(String title)  {
        Optional<VideoRental> theRental =
                openRentals.stream()
                        .filter(r -> r.getVideo().getTitle().equalsIgnoreCase(title))
                        .findAny();
        if (theRental.isPresent()) {


            VideoRental videoRental = theRental.get();
            videoRental.rentalReturn();
            settleRental(videoRental);

        }

    }
    public void settleRental(VideoRental videoRental) {

        Optional<VideoRental> theRental =
                openRentals.stream()
                        .filter(r -> r.getVideo().getTitle().equalsIgnoreCase(videoRental.getVideo().getTitle()))
                        .findAny();

        if (theRental.isPresent()) {

            VideoRental vidRental = theRental.get();

            //filter items that are to be kept open
            Set<VideoRental> theOpenRentals =
                    openRentals.stream()
                            .filter(r -> !r.equals(videoRental))
                            .collect(toSet());

            openRentals.clear();

            //add expected open rentals back to openRentals
            for(VideoRental o : theOpenRentals) {
                openRentals.add(o);
            }

            closedRentals.add(vidRental);

        }

    }
    public int getNumberOpenRentals() {
        return openRentals.size();
    }
    public int getNumberClosedRentals() {
        return closedRentals.size();
    }
    public void clearHistory() {
        closedRentals.clear();
    }
    public void settleRentals() {
        for (VideoRental rental : openRentals) {
            rental.rentalReturn();
            closedRentals.add(rental);
        }
        openRentals.clear();
    }
}
