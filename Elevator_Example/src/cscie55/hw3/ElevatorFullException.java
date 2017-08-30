package cscie55.hw3;

/**
 * Created by Trinitee on 3/31/17.
 */

/**
 * Prints message of elevator's state when elevator is full
 */
public class ElevatorFullException extends Exception {
    ElevatorFullException() {
        System.out.println("Elevator is full");
    }
}
