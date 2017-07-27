package rentals;

import java.time.LocalDate;

/**
 * Created by charliesawyer on 9/12/16.
 */
public class VideoRental {
    private static final int DEFAULT_RENTAL_PERIOD = 14;
    private static int RENTAL_PERIOD_DAYS = 14;
    private final Video video;
    private final Account account;
    private LocalDate dueDate;
    private LocalDate dateReturned = null;
    public VideoRental(Video video, Account account) throws VideoException {
        if (!video.isAvailable()) {
            throw new VideoException (video.toString());
        }
        video.checkOut();
        this.video = video;
        this.account = account;
        dueDate = LocalDate.now().plusDays(RENTAL_PERIOD_DAYS);
        account.addRental(this);
    }
    public VideoRental(Video video, Account account, LocalDate dueDate)
            throws VideoException{
        this(video, account);
        this.dueDate = dueDate;
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (getClass() != other.getClass()) {
            return false;
        }
        VideoRental otherRental = (VideoRental) other;
        return video.equals(otherRental.video) &&
                account == otherRental.getAccount() &&
                dueDate.equals(otherRental.dueDate);
    }
    @Override
    public int hashCode() {
        return video.hashCode()    *
                account.hashCode() *
                dueDate.hashCode();
    }
    @Override
    public String toString() {
        return video.getTitle() +
                "->" +
                account.getEmail() +
                " due on " +
                dueDate;
    }
    public LocalDate getDateDue() {
        return dueDate;
    }
    public Video getVideo() {
        return video;
    }
    public Account getAccount() {
        return account;
    }
    public boolean isOpen() { return dateReturned == null; }
    public void rentalReturn() {
        dateReturned = LocalDate.now();
        video.checkIn();
    }
    public static int getRentalPeriod() { return RENTAL_PERIOD_DAYS; }
    public boolean isOverDue() {
        return dueDate.isAfter(LocalDate.now());
    }
}
