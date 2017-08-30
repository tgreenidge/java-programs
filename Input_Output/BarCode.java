/*
 Name: Tisha Greenidge
 Course: CSCIE-10b
 Date: 3.9.17
 Assignment #: 2 
*/

import java.lang.*;

public class BarCode{
    private String myZipCode;
    private String myBarCode;
    private int [][] patterns = {
                                {1, 1, 0, 0, 0},
                                {0, 0, 0, 1, 1},
                                {0, 0, 1, 0, 1},
                                {0, 0, 1, 1, 0},
                                {0, 1, 0, 0, 1},
                                {0, 1, 0, 1, 0},
                                {0, 1, 1, 0, 0},
                                {1, 0, 0, 0, 1},
                                {1, 0, 0, 1, 0},
                                {1, 0, 1, 0, 0}
                                };
    private int [] multipliers = {7, 4, 2, 1, 0};
    
    public BarCode (String code) {
        
        // a conversion of the entered code to a zip Cde or a bar Code
        String theCode;
        
        // possibly test for valid - 5 0r 32 characters
        if (code.length() == 32){
            
            if (isValidBarCode(code)) this.myBarCode = code;
            else throw new IllegalArgumentException( "Illegal bar code values: " + code);
           
            theCode = decode(code);
           
            if ( theCode == "") throw new IllegalArgumentException( "Illegal zip code values: " + theCode);
            else this.myZipCode = theCode;
           
        } else {
           
            if (isValidZipCode(code)) this.myZipCode = code;
            else throw new IllegalArgumentException( "Illegal zip code values: " + code);
           
            theCode = encode(code);
           
            if ( theCode == "") throw new IllegalArgumentException( "Illegal zip code values: " + theCode);
            else this.myBarCode = theCode;
           
        }
    }
    
    public String getZipCode () {
        return myZipCode;
    }
    
    public String getBarCode () {
        return myBarCode;
    }
    
    // Returns five bars that represents the digit
    private String digitToCode (int digit) {

        String resultCode = "";
        
        //get pattern for digit from the patterns array
        int [] pattern = patterns[digit];
        
        //convert 0, 1 to : and |
        for (int i = 0; i < pattern.length; i++ ){
            if (pattern[i] == 0) resultCode += ':';
            else resultCode += '|';
        }
        
        return resultCode;
    }
    
    //takes a five bar code and returns corresponding 0 - 9 digit
    private int codeToDigit (String barCode) {
        
        int resultDigit = 0;
        int digit;
        
        for (int i = 0; i < barCode.length(); i++){
            
            if (barCode.charAt (i) == '|') digit = 1;
            else digit = 0;
            
            resultDigit += (digit * multipliers[i]);
        }
        
        //special handling for if zipcode digit value is 0
        if (resultDigit == 11) return 0;
        else return resultDigit;
    }
    
    private boolean isValidBarCode (String barCode) {
        
        // length must be 32
        if (barCode.length() != 32) return false;
        
        //First and last Character must be '|'
        if (barCode.charAt (0) != '|' || barCode.charAt (31) != '|' ) return false;
        
        // all other characters must be a '|' or a ':'
        for (int i = 1; i < 26; i++ ){
            if ( ! (barCode.charAt (i) == ':' || barCode.charAt (i) == '|')) return false;
        }
    
        //check for bad checkDigit
        String testZipCode = decode (barCode);
        if ( ! (digitToCode (getCheckDigit (testZipCode)).equals (barCode.substring (26, 31))))  return false;
        
        return true;
        
    }
    
    private boolean isValidZipCode (String zipCode) {
        
        // must be exactly 5 characters
        if ( zipCode.length() != 5 ) return false;
        
        //each digit must be 0 - 9
        for (int i = 0; i < zipCode.length(); i++ ){
            if (!Character.isDigit (zipCode.charAt (i) ) ) return false;
        }
        
        return true;
    }
    
    //Returns integer 0-9 that is necessary for the sum od the digits = next multiple of 10
    private int getCheckDigit (String zipCode) {
        
        int sum = 0;
        
        for (int i = 0; i < zipCode.length(); i++){
            sum += Character.getNumericValue (zipCode.charAt (i) ) ;
        }
        
        if (sum % 10 == 0){
            // multiple of 10
            return 0;
        } else {
            //round to next 10 
            int nearestTen = (int) Math.ceil(sum / 10.0) * 10 ;
            
            //get the difference between the sum and the nearest 10
            int diff =  nearestTen -  sum;
            
            return diff;
        }
    }
    
    // converts 5 digit zipcode to 32 character bar code
    private String encode (String zipCode) {
        //first frame bar
        String resultBarCode = "|";
        
        for ( int i  = 0; i  <  zipCode.length(); i++){
            String digitCode = digitToCode (Character.getNumericValue ((zipCode.charAt (i) )) ) ;
            resultBarCode += digitCode;
        }
        
        String checkDigitCode = digitToCode (getCheckDigit (zipCode));
        
        return resultBarCode + checkDigitCode  + "|";
    }
    
    // returns the 5 digit zip code as a string for the barCode input
    private String decode (String barCode) {
        
        String resultZipCode = "";
        String newDigitCode = "";
        
        int nextStart = 6;
        
        //traverse string from index 1 to 25 (ignores the checkDigitCode and frame bars)
        for (int i = 1; i < 26; i++){
            if (i == nextStart) {
                resultZipCode += codeToDigit(newDigitCode);
                newDigitCode = "" + barCode.charAt(i);
                nextStart += 5;
            } else {
                newDigitCode += barCode.charAt(i);
            }
        }
        
        return resultZipCode +  codeToDigit(newDigitCode);
    }
    
}