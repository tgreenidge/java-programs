/*
 Name: Tisha Greenidge
 Course: CSCIE-10b
 Date: 3.22.17
 Assignment #: 3 
*/

import java.io.*;
import java.util.*;


public class CaesarCipher {
    
    /**
    *This method enciphers and deciphers a string input
    * @param input is the string to be enciphered or deciphered
    * @param shift is the amount of places in alphabet input should be "shifted" by
    * @param isToEncipher the choice to encipher(true) or decipher (false)
    */
    
    public static String caesarCipher (String input, int shift, Boolean isToEncipher){
       
        StringBuilder output = new StringBuilder();
        int letterCodeOffset;
        char letter; // letter encoded or decoded
        
        for (int i = 0; i < input.length(); i++){
            
            //check for upperCase letter
            if (Character.codePointAt(input, i) >= 65 && Character.codePointAt(input, i) <= 90)  {
               
               //position of letter in alphabet - 1
               int numInAlphabet = Character.codePointAt (input, i) - 65;
              
               if ( isToEncipher) {
                    //calculates new position resulting letter in alphabet from shift
                    letterCodeOffset = (numInAlphabet + shift) % 26;
               
                    //convert to uppercase letter
                    letter = (char) (letterCodeOffset + 65);
                    
                    
               } else { //to decode
               
                    //calculates new position resulting letter in alphabet from shift
                    letterCodeOffset    = (numInAlphabet - shift);
               
                    //convert to uppercase letter
                    if (letterCodeOffset >= 0) letter = (char) (letterCodeOffset + 65);
                    else  letter = (char) ( 91 + letterCodeOffset);
            
               }
              
               output.append(letter);
               
            }else{
                
                //ignore other non-UpperCase characters
                output.append(input.charAt(i));
            }
            
        }
        
        return output.toString();
    }
    
    /**
    * This main program tests the caesarCipher method
    * It reads the "hello.txt", then creates 4 enciphered Output files of shifts 1-4
    * It then deciphers each line of the enciphered files and compares them with the original
    * text files
    */
    public static void main (String [] args) throws IOException {
        
        String cipheredString;
        int [] shifts = {1, 2, 3, 4};
        String [] outputFiles = new String [4];
        
        System.out.println ("Welcome to CaesarCipher");
        System.out.println ("This main program tests the caesarEncipher Program.\n");
        
        for (int i = 0; i < shifts.length; i++){
            FileInputStream inputFile = new FileInputStream ("hello.txt");
            InputStreamReader reader = new InputStreamReader (inputFile);
            BufferedReader keyboard = new BufferedReader (reader);
            
            //create a new output file for each shift
            String outputFileName = "outputFile" + shifts[i] + ".txt";
            FileOutputStream outputFile = new FileOutputStream (outputFileName);
            outputFiles[i] = outputFileName;
            
            PrintWriter pw = new PrintWriter (outputFile);
            String line = keyboard.readLine ();
            
            //write to output file
            while (line != null) {
                cipheredString = caesarCipher (line, shifts[i], true);
                pw.println (cipheredString);
                line = keyboard.readLine ();
            }
            
            System.out.println("Done creating an ecoded file with a shift of " + shifts[i] + 
                ". Success!!");
            pw.close();
            inputFile.close();
            
        }
        
        
        System.out.println("\n\nNow we will test the cipher");
        
        //test cipher with output files
        for (int i = 0; i < shifts.length; i++){
            
            FileInputStream originalFile = new FileInputStream ("hello.txt");
            InputStreamReader reader1 = new InputStreamReader (originalFile);
            BufferedReader keyboard1 = new BufferedReader (reader1);
            
            
            String encodedFileName = outputFiles[i];
            FileInputStream encodedFile = new FileInputStream (encodedFileName);
            InputStreamReader reader2 = new InputStreamReader (encodedFile);
            BufferedReader keyboard2 = new BufferedReader (reader2);
            
            //read corresponding lines from input and outputfiles
            String line1 = keyboard1.readLine ();
            String line2 = keyboard2.readLine ();
            
            //decipher encoded file and test with original input file
            while (line1 != null && line2 != null) {
                if ((caesarCipher (line2, shifts[i], false)).equals(line1)){
                    line1 = keyboard1.readLine ();
                    line2 = keyboard2.readLine ();
                } else {
                    System.out.println ("Cipher Incorrect");
                    System.exit(0);
                }
                
            }
            
            if (line1 != null || line2 != null){
                System.out.println ("Lines in 2 files are not properly matched");
                System.exit(0);
            }
            
            System.out.println("Done deciphering " + outputFiles[i] + 
                " compared to original hello.txt file. Success!!");
            encodedFile.close();
            originalFile.close();
            
        }
        
        
    }
}
