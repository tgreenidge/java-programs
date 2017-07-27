package cscie55.hw3;

import java.util.ArrayList;

/**
 * Created by Trinitee on 3/31/17.
 */
public class Floor {
    /**
     * The building that the floor belongs to.
     */
    protected Building building;

    /**
     * floor number
     */
    protected int floorNumber;

    /**
     * List of passengers who are residents on the floor
     */
    protected ArrayList<Passenger> floorResidents;

    /**
     * List of passengers who are on the floor, waiting for the elevator to go up
     */
    protected ArrayList<Passenger> passengersGoingUp;



    /**
     * List of passengers who are on the floor, waiting for the elevator to go down
     */
    protected ArrayList<Passenger> passengersGoingDown;


    /**
     * Creates a new floor within a building
     */
    public Floor(Building building, int floorNumber) {
        this.building = building;
        this.floorNumber = floorNumber;
        this.floorResidents = new ArrayList<Passenger>();
        this.passengersGoingUp = new ArrayList<Passenger>();
        this.passengersGoingDown = new ArrayList<Passenger>();
    }


    /**
     * Checks to ensure that invalid floor is not requested,
     * or that floor requested is not the same as the floor currently on
     * Determines which passenger is waiting for the elevator on this floor
     * Compares destination floor to the floor number to determine if passenger is going up or down
     * @param passenger - passenger waiting for the elevator
     * @param destinationFloor - destination floor
     */
    public void waitForElevator (Passenger passenger, int destinationFloor) {

        if (destinationFloor > Building.FLOORS ||
                (destinationFloor < Building.MIN_FLOOR && destinationFloor != -1 )) {
               throw new IllegalArgumentException("Try again! You have entered an invalid floor.");
        }

        //initiate destination floor for passenger
        passenger.waitForElevator(destinationFloor);

        //remove passenger from floor residents
        building.buildingFloors[floorNumber].floorResidents.remove (passenger);

        // add passenger to list of those waiting to go up or below the floor
        if (floorNumber < destinationFloor){
            building.buildingFloors[floorNumber].passengersGoingUp.add (passenger);
        } else {
            building.buildingFloors[floorNumber].passengersGoingDown.add (passenger);
        }

    }

    /**
     * Detemines if passenger is resident of floor
     * @param passenger - passenger to test
     * @return true if passenger is resident of floor
     */
    public boolean isResident (Passenger passenger) {
        return building.buildingFloors[floorNumber].floorResidents.contains(passenger);
    }

    /**
     * Adds a passenger to the floor's residents
     * @param passenger - passenger entering floor
     */
    public void enterGroundFloor (Passenger passenger) {
        this.floorResidents.add (passenger);
    }

}
