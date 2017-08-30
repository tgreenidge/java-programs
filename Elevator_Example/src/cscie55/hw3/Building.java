package cscie55.hw3;

import java.util.ArrayList;

/**
 * Created by Tisha Greenidge (Trinitee) on 3/31/17.
 */
public class Building {
    /**
     * Number of floors in the building
     */
    public static final int FLOORS = 7;

    /**
     * Ground Floor Number
     */
    protected static final int MIN_FLOOR = 1;

    /**
     * Elevator in a building
     */
    private Elevator elevator;

    /**
     * Keeps track of the information for each floor in a building
     */
    protected Floor [] buildingFloors;


    /**
     * Creates a new instance of a building
     */
    public Building() {

        this.elevator = new Elevator(this);
        this.buildingFloors = new Floor [FLOORS + 1];

        //add floors to the building
        for( int i = 1; i <= FLOORS; i++){
            buildingFloors[i] = floor(i);
            this.elevator.passengers.put(i, new ArrayList<Passenger>());
        }
    }

    /**
     * Gets the elevator for the building
     * @return this building's elevator
     */
    public Elevator elevator() {
        return this.elevator;
    }

    /**
     * Creates a new instance of a floor and
     * Gets the floor information
     * @return floor
     */
    public Floor floor(int floorNumber) {
        Floor floor = new Floor (this, floorNumber);
        return floor;
    }

    /**
     * Calls the Floor.enterGroundFloor() method for the groundFloor
     * @param passenger - the passenger entering the ground floor
     */
    public void enter (Passenger passenger) {
        buildingFloors[MIN_FLOOR].enterGroundFloor(passenger);
    }

}
