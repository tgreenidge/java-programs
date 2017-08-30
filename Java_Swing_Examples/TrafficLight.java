/**
* Tisha Greenidge
* CSCIE-10b
* HW 4
* 
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class TrafficLight extends JFrame {

    public TrafficLight() {
        setTitle ("StopLight");
        setSize (200, 624);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        DrawLights lights = new DrawLights();
        add (lights);

    }
    
    public static void main (String [] args) {
        TrafficLight trafficLights = new TrafficLight();
        trafficLights.setVisible(true);  
    }

}

class DrawLights extends JPanel {
    public void paintComponent (Graphics g) {
        
        Color [] colors = {Color.RED, Color.YELLOW, Color.GREEN};

        int x = 0;

        for (Color color: colors){
            g.setColor (color);
            if (color == Color.RED) g.fillOval (0, x, 200, 200);
            else g.fillOval (0, x += 200, 200, 200);
            g.setColor (Color.BLACK);
            g.drawLine (0, x + 200, 400, x + 200);
        }
    }
}

