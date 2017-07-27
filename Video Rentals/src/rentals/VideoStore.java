package rentals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toSet;

/**
 * Created by charliesawyer on 9/15/16.
 */
public class VideoStore {
    private List<Video> library = new ArrayList<>();
    private Set<VideoRental> rentals = new HashSet<>();
    private List<Account> accounts = new ArrayList<>();
    private static String fileName;

    static {
        String fName = System.getProperty("FILENAME");
        if (fName == null) {
            fileName = "TITLES.TXT";
        }
    }
    public VideoStore() throws VideoException {
        if (library.size() == 0) {
            if(System.getProperty("STREAMS") != null) {
                loadStreamingVideos();
            } else {
                loadVideos();
            }
        }
    }
    int loadVideos() throws VideoException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new VideoException("no such File + " + fileName);
        }
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String pieces[] = line.split("[()]");
                Video video = new Video(pieces[0].trim(), Integer.parseInt(pieces[1]));
                library.add(video);
            }
        } catch (java.io.IOException eio) {
            throw new VideoException(eio.getMessage());
        }
        return library.size();
    }
    private static Video createVideo(String [] pieces) {
        try {
            Video video = new Video(pieces[0].trim(), Integer.parseInt(pieces[1]));
            return video;
        } catch (VideoException vid) {
            throw new RuntimeException(vid.getMessage());
        }
    }
    int loadStreamingVideos () throws VideoException {
        try {
            Stream<String> lines = Files.lines(Paths.get(fileName));
            library = lines.map(line -> line.split("[()]"))
                    .map(stringray -> createVideo(stringray))
                    .collect(Collectors.toList());
        } catch (java.io.IOException eio) {
            throw new VideoException(eio.getMessage());
        }
        return library.size();
    }
    public Video getVideo(String title) {
        for (Video video : library) {
            if (video.getTitle().equalsIgnoreCase(title)) {
                return video;
            }
        }
        return null;
    }
    public void rentVideo(String title, String email) throws Exception {
        Account account = getAccount(email);
        Video video = getVideo(title);
        if (account != null && video !=null) {
            VideoRental rental = new VideoRental(video, account);
            rentals.add(rental);
        } else {
            throw new Exception("Bogus title and/or account email");
        }
    }
    public boolean hasRentedVideo(String title, String email) throws Exception {
        // Get the account, stream its rentals, filter for title, count >? 0
        return getAccount(email).getOpenRentals().stream()
                .filter(s -> s.getVideo()
                        .getTitle()
                        .equalsIgnoreCase(title))
                .collect(counting()) > 0;
    }
    public Set<VideoRental> getRentals() {
        return rentals;
    }
    public Set<VideoRental> getOverdueRentals() {
        Set<VideoRental> result = new HashSet();
        for (VideoRental videoRental : rentals) {
            if (videoRental.isOverDue()) {
                result.add(videoRental);
            }
        }
        return result;
    }
    /* Same functionality as getOverdueRentals using Java8 streams
     */
    public Set<VideoRental> getOvrDueRentals() {
        return rentals.stream()
                .filter(VideoRental::isOverDue)
                .collect(toSet());
    }
    public void createAccount(String firstName, String lastName, String email) {
        accounts.add(new Account(firstName, lastName, email));
    }
    public Account getAccount(String email) {
        Account result = null;
        for (Account account : accounts) {
            if (account.getEmail().equals(email)) {
                result = account;
                break;
            }
        }
        return result;
    }
    /*
    Same as getAccount above using Java8 streams
     */
    public Account getAccountFromStream (String email) {
        Account result = null;
        Optional<Account> selectedAccounts =
                accounts.stream()
                .filter(a -> a.getEmail().equals(email))
                .findAny();
        return selectedAccounts.isPresent() ?
                selectedAccounts.get()
                : null;
    }
    public void addRental(VideoRental videoRental) {
        rentals.add(videoRental);
    }
    public void settleAccount(Account account) {
        account.getOpenRentals().stream().forEach(VideoRental::rentalReturn);
    }
    public void settleAccount(String email) {
        Account account = getAccount(email);
        settleAccount(account);
    }
    public Video getRandomAvailableVideo() {
        Random randy = new Random();
        Video result = null;
        while (!(result = library.get(randy.nextInt(library.size()))).isAvailable());
        return result;
    }
    public Account getRandomAccount() {
        Random randy = new Random();
        return accounts.get(randy.nextInt(accounts.size()));
    }
    public void clearRentals() {
        rentals.clear();
    }
}
