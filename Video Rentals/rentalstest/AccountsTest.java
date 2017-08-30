package rentalstest;

import org.junit.Test;
import rentals.*;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * These tests test methods in the Account Class
 */
public class AccountsTest {
    // create dummy data

    String firstName = "Tisha";
    String lastName = "Greenidge";
    String email = "TGreenidge@gmail.com";
    int id = 1;

    String firstName1 = "Tina";
    String lastName1 = "Green";
    String email1 = "TGreen@gmail.com";
    int id1 = 1;

    String firstName2 = "John";
    String lastName2 = "Doe";
    String email2 = "JDoe@gmail.com";
    int id2 = 2;

    String firstName3 = "Tish";
    String lastName3 = "Greene";
    String email3 = "TGreene@gmail.com";
    int id3 = 3;



    //tests the creation of a new account generates an account ID
    @Test
    public void testCreationNewAccount(){
        Account account = new Account (firstName, lastName, email, id);
        assertEquals(1, account.getID());
    }

    //tests the creation of multiple accounts generates multiple account IDs that was persistent
    @Test
    public void testCreationMultipleAccounts(){

        Account account1 = new Account (firstName1, lastName1, email1, id1);
        assertEquals(1, account1.getID());

        Account account2 = new Account (firstName2, lastName2, email2, id2);
        assertEquals(2, account2.getID());

    }

    //tests the creation of multiple accounts generates multiple account IDs that was persistent
    @Test
    public void testNewVideoStoreWithMultipleAccounts() {

        Account account1 = new Account (firstName1, lastName1, email1, id1);
        Account account2 = new Account (firstName2, lastName2, email2, id2);
        Account account3 = new Account (firstName3, lastName3, email3, id3);


        try {
            VideoStore videoStore = new VideoStore();
            videoStore.createAccount(firstName1,  lastName1, email1);
            videoStore.createAccount(firstName2,  lastName2, email2);
            videoStore.createAccount(firstName3,  lastName3, email3);
            assertEquals(videoStore.getAccount(email1), account1);
            assertEquals(videoStore.getAccount(email2), account2);
            assertEquals(videoStore.getAccount(email3), account3);
            assertEquals(videoStore.getAccount(email1).getID(), id1);
            assertEquals(videoStore.getAccount(email2).getID(), id2);
            assertEquals(videoStore.getAccount(email3).getID(), id3);

        } catch (VideoException e) {
            System.out.println(e);
            fail();
        }
    }

    /*This test tests the simulation of the creation of a videoStore with multiple accounts
    * by using the set up in VideoStoreUtils.java
    * It ensures that we have created accounts with unique email addresses
    */
    @Test
    public void testCreateNewVideoStoreSimulation() {

        try {
            VideoStore utilsVideoStore = new VideoStore();
            VideoStoreUtils.createAccounts(utilsVideoStore);
            assertEquals(VideoStoreUtils.getFirstInitials().size() * VideoStoreUtils.getNumberOfLastNames(),
                    utilsVideoStore.getNumberOfAccounts());
        } catch (VideoException e) {
            System.out.println(e);
            fail();
        }

    }

    //This test tests the creation of multiple videoStores to ensure that the ACCOUNTS_ID_TRACKER works accurately
    @Test
    public void testCreateMultipleNewVideoStores() {
        try {
            VideoStore utilsVideoStore = new VideoStore();
            VideoStoreUtils.createAccounts(utilsVideoStore);

            VideoStore videoStore = new VideoStore();
            videoStore.createAccount(firstName1,  lastName1, email1);
            videoStore.createAccount(firstName2,  lastName2, email2);
            videoStore.createAccount(firstName3,  lastName3, email3);

            assertEquals(videoStore.getAccountsIdTracker(), videoStore.getNumberOfAccounts());
        } catch (VideoException e) {
            System.out.println(e);
            fail();
        }

    }

    //This test tests the equality of 2 accounts
    @Test
    public void testEqualityOfAccounts() {


        Account account1 = new Account (firstName1, lastName1, email1, id1);
        Account account2 = new Account (firstName2, lastName2, email2, id2);
        Account account3 = new Account (firstName3, lastName3, email3, id3);
        Account account4 = new Account (firstName3, lastName3, email3, id3);
        Account account5 = new Account (firstName2, lastName3, email3, id3);

        // 2 accounts are equal only if all the fields are the same for the objects
        assertEquals(true, account3.equals(account4));
        assertEquals(false, account1.equals(account4));
        assertEquals(false, account3.equals(account2));
        assertEquals(false, account5.equals(account2));

    }

    //this test the addition of a single rental to an account at a time

    @Test
    public void testAddingAndSettlingSingleRentalToAccount() {

        try {

            Account account1 = new Account(firstName1, lastName1, email1, id1);
            Account account2 = new Account (firstName2, lastName2, email2, id2);
            Account account3 = new Account (firstName3, lastName3, email3, id3);


            Video video1 = new Video ("The Giants", 1926);
            Video video2 = new Video("A Star", 2014);
            Video video3 = new Video("Stary Night", 2013);


            //add rental1 to account 1
            VideoRental rental1 = new VideoRental(video1, account1);

            //check to see if the video & account that was added is as expected
            assertEquals(true, video1.equals(rental1.getVideo()));
            assertEquals(true, account1.equals(rental1.getAccount()));

            //check properties of Account
            assertEquals(true, account1.hasOpenRental(video1.getTitle()));
            //does not have a title that was not added
            assertEquals(false, account1.hasOpenRental(video2.getTitle()));
            assertEquals(1, account1.getNumberOpenRentals());
            assertEquals(0, account1.getNumberClosedRentals());
            assertEquals(0, account1.getOverdueRentals().size());

            //should not be able to add a rental from another account to account
            VideoRental rental2 = new VideoRental(video2, account1);
            account2.addRental(rental2);
            fail();

            //add overdue rental to the account
            VideoRental overDueRental = new VideoRental(video3, account3, LocalDate.now().minusDays(4));

            //check to see if the video & account that was added is as expected
            assertEquals(true, video3.equals(overDueRental.getVideo()));
            assertEquals(true, account3.equals(overDueRental.getAccount()));

            //check properties of Account
            assertEquals(true, account3.hasOpenRental(overDueRental.getVideo().getTitle()));

            assertEquals(1, account3.getNumberOpenRentals());
            assertEquals(0, account3.getNumberClosedRentals());
            assertEquals(1, account3.getOverdueRentals().size());



            //---------Settle Rental ---------

            //test settleRental(Rental rental)
            account1.settleRental(rental1);
            assertEquals(false, account1.hasOpenRental(rental1.getVideo().getTitle()));
            assertEquals(0, account1.getNumberOpenRentals());
            assertEquals(1, account1.getNumberClosedRentals());
            assertEquals(0, account1.getOverdueRentals().size());
            assertEquals(true, account1.getClosedRentals().contains(rental1));


            //test settleRental(String Title)
            account3.settleRental(overDueRental.getVideo().getTitle());
            assertEquals(false, account3.hasOpenRental(overDueRental.getVideo().getTitle()));
            assertEquals(0, account3.getNumberOpenRentals());
            assertEquals(1, account3.getNumberClosedRentals());
            assertEquals(0, account3.getOverdueRentals().size());
            assertEquals(true, account3.getClosedRentals().contains(overDueRental));

            //test settleOverdueRental(String Title)
            account3.settleRental(overDueRental.getVideo().getTitle());
            assertEquals(false, account3.hasOpenRental(overDueRental.getVideo().getTitle()));
            assertEquals(0, account3.getNumberOpenRentals());
            assertEquals(1, account3.getNumberClosedRentals());
            assertEquals(0, account3.getOverdueRentals().size());
            assertEquals(true, account3.getClosedRentals().contains(overDueRental));


        } catch (VideoException e) {
            System.out.println(e);
            fail();
        } catch (IllegalArgumentException e){
            //Expected;
        }

    }

    //this test the addition of a multiple rental to an account at a time

    @Test
    public void testAddingAndSettlingMultipleRentalToAccount() {

        try {
            Account account1 = new Account(firstName1, lastName1, email1, id1);

            Video video1 = new Video("The Giants", 1926);
            Video video2 = new Video("A Star", 2014);
            Video video3 = new Video("Stary Night", 2013);
            Video video4 = new Video("A Stary Morning", 2014);
            Video video5 = new Video("Stary Light", 2013);

            VideoRental rental1 = new VideoRental(video1, account1);
            VideoRental rental2 = new VideoRental(video2, account1);
            VideoRental rental3 = new VideoRental(video3, account1);

            VideoRental overDueRental1 = new VideoRental(video4, account1, LocalDate.now().minusDays(4));
            VideoRental overDueRental2 = new VideoRental(video5, account1, LocalDate.now().minusDays(4));

            assertEquals(true, account1.hasOpenRental(video1.getTitle()));
            assertEquals(true, account1.hasOpenRental(video2.getTitle()));
            assertEquals(true, account1.hasOpenRental(video3.getTitle()));
            assertEquals(true, account1.hasOpenRental(video4.getTitle()));
            assertEquals(true, account1.hasOpenRental(video5.getTitle()));
            assertEquals(5, account1.getNumberOpenRentals());
            assertEquals(0, account1.getNumberClosedRentals());
            assertEquals(2, account1.getOverdueRentals().size());

            account1.settleRental(overDueRental1);
            assertEquals(4, account1.getNumberOpenRentals());
            assertEquals(1, account1.getNumberClosedRentals());
            assertEquals(1, account1.getOverdueRentals().size());

            account1.settleRentals();
            assertEquals(0, account1.getNumberOpenRentals());
            assertEquals(5, account1.getNumberClosedRentals());
            assertEquals(0, account1.getOverdueRentals().size());


        } catch (VideoException e) {
            System.out.println(e.getMessage());
            fail();
        }

    }

}
