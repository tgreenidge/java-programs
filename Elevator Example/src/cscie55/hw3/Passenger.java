package cscie55.hw3;
/**
 * Created by Trinitee on 3/31/17.
 *
 */


public class Passenger {
    /**
     * Id of passenger.
     */
    protected int id;

    /**
     * Value set to if a current floor  or destination floor is undefined.
     */
    protected int UNDEFINED_FLOOR = -1;

    /**
     * Current floor number that passenger is on.
     */
    protected int currentFloor;

    /**
     * Destination floor number of passenger.
     */
    protected int destinationFloor;


    /**
     * Passenger constructor
     * @param id - id of passenger
     */
    public Passenger (int id) {
        this.id = id;
        this.currentFloor = Building.MIN_FLOOR;
        this.destinationFloor = UNDEFINED_FLOOR;
    }

    /**
     * Gets the current floor for passenger
     * @return currentFloor
     */
    public int currentFloor() {
        return this.currentFloor;
    }

    /**
     * Gets the destination floor of the passenger
     * @return destinationFloor
     */
    public int destinationFloor() {
        return this.destinationFloor;
    }


    /**
     * Sets the passenger's destination floor of the new destination floor
     * @param newDestinationFloor - the passenger's new Destination floor
     */
    public void waitForElevator (int newDestinationFloor) {
        this.destinationFloor = newDestinationFloor;
    }

    /**
     * Sets the current floor to undefined when passenger steps into elevator
     */
    public void boardElevator() {
        this.currentFloor = UNDEFINED_FLOOR;
    }

    /**
     * Passenger on the elevator arrives at destination
     * currentFloor set to destinationFloor
     * destinationFloor set to undefined
     */
    public void arrive() {
        this.currentFloor = this.destinationFloor;
        this.destinationFloor = UNDEFINED_FLOOR;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                ", UNDEFINED_FLOOR=" + UNDEFINED_FLOOR +
                ", currentFloor=" + currentFloor +
                ", destinationFloor=" + destinationFloor +
                '}';
    }
}

