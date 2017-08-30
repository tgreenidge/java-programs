package cscie55.hw3;

import java.util.*;

/**
 * Created by Trinitee on 3/31/17.
 */
public class Elevator {

    /**
     * Maximum capacity in elevator.
     */
    public static final int CAPACITY = 10;

    /**
     * Elevator's current floor
     */
    private int currentFloor;

    /**
     * Direction values of elevator
     */
    enum  DIRECTION {UP, DOWN};

    /**
     * Direction of elevator.
     */
    private DIRECTION direction;

    /**
     * Building that the elevator belongs to.
     */
    private Building building;

    /**
     * Keeps track of the number of passengers in the elevator at a given time.
     */
    private int numPassengersOnBoard;

    /**
     * Keeps track of the passengers destined for a designated floor.
     */
    protected HashMap<Integer, ArrayList<Passenger>> passengers;

    /**
     * Elevator constructor
     * @param building - the building that the elevator belongs to
     */
    public Elevator(Building building){

        this.currentFloor = Building.MIN_FLOOR;
        this.direction = DIRECTION.UP;
        this.building = building;
        this.passengers = new HashMap<Integer, ArrayList<Passenger>>();

    }


    /**
     * Increments current floor if elevator is moving up,
     * and changes direction if top floor is reached.
     * Decrements current floor if elevator is moving down,
     * and changes direction if bottom floor is reached.
     * Stops and deboards passengers only if there are passengers destined
     * for the current floor elevator has moved to.
     * Stops and boards passengers only if there are passengers waiting
     * for the current floor elevator has moved to.
     * Makes call to the toString method to prints the state of the elevator after each move.
     */
    public void move() {

        //ensures that floor status is printed when it reaches floor minimum
        if (currentFloor == Building.MIN_FLOOR) System.out.println(toString());

        if (direction == DIRECTION.UP){
            currentFloor++;
            if (currentFloor == Building.FLOORS) direction = DIRECTION.DOWN;
        }else{
            currentFloor--;
            if (currentFloor == Building.MIN_FLOOR) direction = DIRECTION.UP;
        }


        // deboard passengers if passengers in elevator needs to deboard
        if (passengers.get(currentFloor).size() > 0) deboardPassengers(currentFloor);

        // try to board passengers waiting on the floor one at a time
        while (numPassengersWaitingOnThisFloor() > 0){
            try {
                boardPassenger();
            } catch (ElevatorFullException e) {
                System.out.println("Not Taking Passengers\n");

                //break out of while loop
                break;
            }
        }

        System.out.println(toString());

    }

    /**
     * Only Boards passenger if elevator is not full
     * Increases the number of passengers destined for requested floor by 1.
     * Increases the number of passengers in the elevator by 1.
     * Ensures that passengers are boarded in order of elevator call
     */
    public void boardPassenger() throws ElevatorFullException {

        // determine next passenger in line to board elevator
        //made null to get rid of the uninitialized error
        Passenger nextPassenger = null;

        // Flag for if passenger is boarded
        // made false to get rid of the uninitialized error
        boolean boardPassenger = false;

        //only board passenger if elevator is not full
        if ( numPassengersOnBoard  ==  CAPACITY){
            throw new ElevatorFullException();
        } else {
            //get the next passenger from the list of passengers waiting for the elevator
            if ( goingUp()){

                if (building.buildingFloors[currentFloor].passengersGoingUp.size() > 0) {

                     nextPassenger =
                             building.buildingFloors[currentFloor]
                                     .passengersGoingUp.get(0);

                     //remove passenger from list of passengers on this floor waiting to go up
                     building.buildingFloors[currentFloor].passengersGoingUp.remove(0);

                     //flag passenger ok to board
                     boardPassenger = true;
                 }

            } else {
                if (building.buildingFloors[currentFloor].passengersGoingDown.size() > 0){

                    nextPassenger =
                            building.buildingFloors[currentFloor]
                                    .passengersGoingDown.get(0);

                    //remove passenger from list of passengers on this floor waiting to go down
                    building.buildingFloors[currentFloor].passengersGoingDown.remove(0);

                    //flag passenger ok to board
                    boardPassenger = true;
                }
            }



            if(boardPassenger) {
                //board passenger on elevator
                nextPassenger.boardElevator();

                //add boarded passenger to list of passengers for destination floor
                passengers.get(nextPassenger.destinationFloor).add(nextPassenger);

                //increase the number of passengers on board the elevator by 1
                numPassengersOnBoard++;
            }
        }
    }

    /**
     * Gets all passengers intended to be let off on floor
     * Let's off passenger one by one
     * Ensures passenger "arrives" on floor once passenger is let off
     * Ensures passenger becomes a resident of the floor once the passenger arrives on floor
     * Reduces the number of passengers in elevator by the number of passengers destined for floor.
     * Clears the number of passengers destined for floor once passengers are let off the elevator.
     * @param floor - floor that elevator should stop on to let of passengers.
     */
    public void deboardPassengers(int floor){

        //get the number of passengers to deboard
        int numPassengersToDeboard = passengers.get(floor).size();

        //get the actual passengers on elevator for the floor
        ArrayList<Passenger> passengersToDeboard = passengers.get(floor);

        //deboard passenger one by one
        for (Passenger p: passengersToDeboard){

            // once passenger steps off of the elevator, passenger arrives on floor
            p.arrive();

            // passenger becomes resident of floor let off on
            building.buildingFloors[currentFloor].enterGroundFloor(p);

        }

        //remove passengers from the elevator
        passengers.get(floor).clear();

        // reduce the numberOfPassengers in elevator by the number of passengers let off
        numPassengersOnBoard -= numPassengersToDeboard;
    }


    /**
     * @return a boolean that represents whether the elevator is going up.
     */
    public boolean goingUp(){
        return direction == DIRECTION.UP;
    }

    /**
     * @return a boolean that represents whether the elevator is going down.
     */
    public boolean goingDown() {
        return direction == DIRECTION.DOWN;
    }


    /**
     * Gets the elevator's current floor.
     * @return this elevator's current floor.
     */
    public int currentFloor() {
        return currentFloor;
    }


    /**
     * Creates a string with information about the the occupants in the elevator while on current floor.
     * @return string that contain's information about the elevator's floor,
     * and number of passengers in elevator.
     */
    public String toString(){
        return "Floor " + currentFloor + "\nElevator Direction: " + direction +  ". \nPassengers " +
                this.passengers().size() + ". \n\nResidents. " +
                building.buildingFloors[currentFloor].floorResidents.toString()
                + ". \n\nPassengers Going Down"
                + building.buildingFloors[currentFloor].passengersGoingDown.toString()
                + ". \n\nPassengers Gowing Up"
                + building.buildingFloors[currentFloor].passengersGoingUp.toString()
                + "\n------\n";
    }


    /**
     * Gets the Set of passengers in the elevator.
     * @return allPassengers - a set that corresponds with the passengers in elevator.
     */
    public Set<Passenger> passengers() {
        Set <Passenger> allPassengers = new HashSet <Passenger>();

        Iterator iterator = passengers.keySet().iterator();

        while (iterator.hasNext()) {
            ArrayList<Passenger> floorPassengers = passengers.get(iterator.next());
            for (Passenger p: floorPassengers){
                allPassengers.add(p);
            }
        }

        return allPassengers;
    }

    /**
     * Gets the number of passengers waiting on current floor based on the direction of elevator
     * @return numPassengersWaitingOnThisFloor -
     */
    private int numPassengersWaitingOnThisFloor() {

        int numPassengersWaitingOnThisFloor;

        if ( goingUp() ) {
            // get number of passengers waiting to go up
            numPassengersWaitingOnThisFloor =
                    building.buildingFloors[currentFloor].passengersGoingUp.size();
        } else {
            // get number of passengers waiting to go Down
            numPassengersWaitingOnThisFloor =
                    building.buildingFloors[currentFloor].passengersGoingDown.size();
        }

        return numPassengersWaitingOnThisFloor;
    }


}
