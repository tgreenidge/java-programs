package rentalstest;

import org.junit.Test;
import rentals.*;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/*
 * These tests test methods in the VideoRentals class
 */
public class VideoRentalsTest {



    //this test tests the creation of a new VideoRental using the first constructor
    @Test
    public void testNewVideoRentalUsingConstructor1() {

        try {
            Video video1 = new Video ("Titanic", 1997);
            Video video2 = new Video("Green Eggs", 1936);

            Account account1 = new Account("Ann", "Frank", "Afrank@gmail.com", 1);
            Account account2 = new Account("John", "Doe", "jdoe@gmail.com", 2);
            LocalDate exactlyOnTime = LocalDate.now().plusDays(14);

            //the tests below tests the properties of videoRental1
            VideoRental videoRental1 = new VideoRental(video1, account1);

            //availability of video
            assertEquals(false, videoRental1.getVideo().isAvailable());

            //video used for rental
            assertEquals(video1, videoRental1.getVideo());

            //tests dueDate is updated accurately
            assertEquals(exactlyOnTime, videoRental1.getDateDue());

            //account used is the same when created
            assertEquals(account1, videoRental1.getAccount());

            //tests to see if newly created rental is overdue
            assertEquals(false, videoRental1.isOverDue());

            //rental period
            assertEquals(14, videoRental1.getRentalPeriod());

            //rental is open
            assertEquals(true, videoRental1.isOpen());

            // should not be able to create rental on another's account
            VideoRental videoRental2 = new VideoRental(video2, account2);
            account1.addRental(videoRental2);
            fail();

            assertEquals(true, videoRental1.equals(videoRental1));
        } catch (VideoException e) {
           System.out.println(e);
           fail();
        } catch (IllegalArgumentException e){
            //expected
        }

    }

    //this test tests the creation of a new VideoRental using the 2nd constructor
    @Test
    public void testNewVideoRentalUsingConstructor2() {

        try {
            Video video1 = new Video ("Titanic", 1997);
            Video video3 = new Video ( "Green Eggs", 1936);

            Account account1 = new Account("Ann", "Frank", "Afrank@gmail.com", 1);
            Account account2 = new Account("John", "Doe", "jdoe@gmail.com", 2);

            LocalDate exactlyOnTime = LocalDate.now().plusDays(14);


            //the tests below tests the properties of videoRental1
            VideoRental videoRental1 = new VideoRental(video1, account1, exactlyOnTime);

            //availability of video
            assertEquals(false, videoRental1.getVideo().isAvailable());

            //video used for rental
            assertEquals(video1, videoRental1.getVideo());

            //tests dueDate is updated accurately
            assertEquals(exactlyOnTime, videoRental1.getDateDue());

            //account used is the same when created
            assertEquals(account1, videoRental1.getAccount());

            //tests to see if newly created rental is overdue
            assertEquals(false, videoRental1.isOverDue());

            //rental period
            assertEquals(14, videoRental1.getRentalPeriod());

            //rental is open
            assertEquals(true, videoRental1.isOpen());

            // should not be able to create rental on another's account
            VideoRental videoRental2 = new VideoRental(video3, account2, exactlyOnTime);
            account1.addRental(videoRental2);
            fail();

            assertEquals(true, videoRental1.equals(videoRental1));
           ;
        } catch (VideoException e) {
            System.out.println(e);
            fail();
        } catch (IllegalArgumentException e){
            //expected
        }

    }

    //This test tests the onTime() calculation is correct when the due dates are earlier and later than today
    @Test
    public void testIsOverDue(){

        try {
            Video video1 = new Video("Titanic", 1997);
            Video video2 = new Video("Green Eggs", 1936);

            Account account1 = new Account("Ann", "Frank", "Afrank@gmail.com", 1);
            Account account2 = new Account("John", "Doe", "jdoe@gmail.com", 2);

            LocalDate late = LocalDate.now().minusDays(3);
            LocalDate early= LocalDate.now().plusDays(3);

            VideoRental earlyRental = new VideoRental(video1, account1, early);
            VideoRental lateRental = new VideoRental(video2, account2, late);

            assertEquals(false, earlyRental.isOverDue());
            assertEquals(true, lateRental.isOverDue());

        } catch (VideoException e) {
            System.out.println(e);
            fail();
        }

    }


    //This tests that the properties of a Rental is updated accurately when it is returned
    @Test
    public void testVideoRentalUpdatedWhenReturned() {

        try {
            Video video2 = new Video ("The Giants", 1926);
            Account account2 = new Account("John", "Doe", "jdoe@gmail.com", 2);

            VideoRental videoRental = new VideoRental(video2, account2);
            videoRental.rentalReturn();

            assertEquals(true, videoRental.getVideo().isAvailable());
            assertEquals(false, videoRental.isOpen());

        } catch (VideoException e) {
            System.out.println(e);
            fail();
        }

    }


}
