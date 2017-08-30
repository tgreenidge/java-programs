package rentalstest;

import org.junit.Test;
import rentals.Video;
import rentals.VideoException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * These tests test the methods in the video class
 */
public class VideosTest {

    //This test the availability of a single video after it has been checked in/out is updated accurately
    @Test
    public void testAvailabilityOfVideoAfterCheckedInOrOut() {
        try {
            Video titanic = new Video ("Titanic", 1997);

            //when a video is created, it should be available
            assertEquals(true, titanic.isAvailable());

            titanic.checkOut();
            //once the video is checked out, it should be unavailable
            assertEquals(false, titanic.isAvailable());
            assertEquals(true, titanic.isNotAvailable());

            titanic.checkIn();
            //once the video is checked out, it should be unavailable
            assertEquals(true, titanic.isAvailable());
            assertEquals(false, titanic.isNotAvailable());

        } catch (VideoException e){
            System.out.println(e.getMessage());
            fail();
        }
    }

    //This test the availability of a single video after it has been placed instock/out of stock updates accurately
    @Test
    public void testAvailabilityOfVideoAfterRemovedOrAddedToStock() {
        try {
            Video star = new Video("Star", 2013);

            star.removeFromStock();
            //once the video is removed from stock, it should be unavailable
            assertEquals(false, star.isAvailable());
            assertEquals(true, star.isNotAvailable());

            //Out of stock videos should not be allowed to be checked in or checked out
            star.checkIn();
            fail();

            star.checkOut();
            fail();

            //In stock videos should be available
            star.replaceToStock();
            assertEquals(true, star.isAvailable());

        } catch (VideoException e) {
            System.out.println(e.getMessage());
            fail();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            //expected
        }

    }

    //This tests that the equality of 2 videos
    @Test
    public void testEqualityOfVideos() {
        try {
            Video star1 = new Video("Star", 2013);
            Video star2 = new Video("Star", 2014);
            Video staryNight1 = new Video("Stary Night", 2013);
            Video staryNight2 = new Video("Stary Night", 2013);

            //2 videos should be equal only if the titles AND years are the same
            assertEquals(true, staryNight1.equals(staryNight2));
            assertEquals(false, star1.equals(star2));
            assertEquals(false, star1.equals(staryNight1));

        } catch (VideoException e) {
            System.out.println(e.getMessage());
            fail();
        }

    }

    //This tests the CompareTo method
    @Test
    public void testCompareTo() {
        try {
            Video star1 = new Video("Star", 2013);
            Video star2 = new Video("Star", 2002);
            Video aStar = new Video("A Star", 2014);
            Video staryNight = new Video("Stary Night", 2013);

            assertEquals(true, star1.compareTo(star2) == 0 );
            assertEquals(true , staryNight.compareTo(star2) > 0 );
            assertEquals(true, star1.compareTo(staryNight) < 0 );
            assertEquals(true, aStar.compareTo(staryNight) < 0 );

        } catch (VideoException e) {
            System.out.println(e.getMessage());
            fail();
        }


    }


}
