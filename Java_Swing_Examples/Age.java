/**
* Tisha Greenidge
* CSCIE-10b
* HW 4
* 
*/

import javax.swing.*;
import java.awt.*;

public class Age {

    public static void main (String [] Args) {

        String age = null;
        
        age = JOptionPane.showInputDialog (null, "What's your age, cowboy?");

        while( Integer.parseInt (age) < 0){
            JOptionPane.showMessageDialog (null, "Please enter a number 0 or more");
            age = JOptionPane.showInputDialog (null, "What's your age, cowboy?");
        }

        if( Integer.parseInt (age) < 40){
            JOptionPane.showMessageDialog 
            (null, "My, are young! You have only just begun to live child!");
        } else {
            JOptionPane.showMessageDialog 
            (null, "Dang, you are old! You are over the hill and far away!");
        }
        
    }

}