/*
 Name: Tisha Greenidge
 Course: CSCIE-10b
 Date: 3.21.17
 Assignment #: 3 
*/


import java.io.*;
import java.util.*;

public class NoDuplicates {


    public static void main (String [] args) throws FileNotFoundException {
        if (args.length != 2) {
          System.out.println ("Please type exactly 2 arguments!");
          System.exit (0);
        }

        Scanner numbers = new Scanner (new File (args[0]) ); 
        ArrayList<Integer> noDupes = new ArrayList<Integer>();
        
        //create a list that contains no duplicates
        System.out.println ("Input Values: ");
        while (numbers.hasNextInt() ) {
          int num = numbers.nextInt();

          //outut content of file
          System.out.println(num);

          if (! noDupes.contains (num) ) noDupes.add (num); 
        }
        numbers.close();
        
        System.out.println();

        //output values of noDupes arrayList to file (using PrintStream as decribed in textbook)
        PrintStream output = new PrintStream (new File (args[1])); 
        
        for (int i = 0; i < noDupes.size(); i++){
            output.println (noDupes.get (i) );
        }

        output.close();

        //print outfile info
        Scanner outputFile = new Scanner (new File("output.txt"));
        System.out.println ("Output values: ");
        
        while (outputFile.hasNextInt()) {
            int outputNumber = outputFile.nextInt();
            System.out.println (outputNumber);
        }
        
        outputFile.close();
        
    }

}