/*
 Name: Tisha Greenidge
 Course: CSCIE-10b
 Date: 3.24.17
 Assignment #: 3 
*/


import java.io.*;
import java.util.*;

public class ExamAnalysis {
    static ArrayList <Integer> studentCorrectNumberOfResponses = new ArrayList <Integer>();
    static ArrayList <Integer> studentCorrectNumberOfBlanks = new ArrayList <Integer>();
    static ArrayList <int []> consolidatedStudentResponses = new ArrayList <int []>();
    static ArrayList <Integer> correctAnswers = new ArrayList <Integer>();

    static int numberOfStudents = 0;
    static int numberOfQuestions;
    
    /**
     * Tests a string value for valid response (A, B, C, D, E, " ")
     * @param str - string input
     * @param isCorrectResponseStringTest - testing for the string of correct responses 
     *        (true or false)
     * @return response - true or false 
    */
    public static boolean isValidResponse (char str, boolean isCorrectResponseStringTest) {
        boolean response;
        
        switch (str){
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
                response = true;
                break;
                
            case ' ':
                if(! isCorrectResponseStringTest) response = true;
                else response = false;
                break;
                
            default:
                response = false;
        }
        
        return response;
    }
    
    /**
     * returns a mapping of the index to a number
     * @param letter - A, B, C, D, E, or " "
     * @return letterMap - index of corresponding letter or blank of valid 
     *         response: (A - E -> 0 - 4, " " -> 5)
    */
    public static int getLetterMapping (char letter){
        
        int letterMap;
        
        switch (letter){
            case 'A':
                letterMap = 0;
                break;
                
            case 'B':
                letterMap = 1;
                break;
                
            case 'C':
                letterMap = 2;
                break;
                
            case 'D':
                letterMap = 3;
                break;
                
            case 'E':
                letterMap = 4;
                break;
                
            default:
                //is a blank or some other character
                letterMap = 5;
        }
        
        return letterMap;
       
    }
    
    /**
     * Prints the student Analysis
     * 
    */
    public static void printStudentAnalysis () {
        
        System.out.format ("%5s \t%5s \t%5s \t%5s", "Student #", "Correct", "Incorrect", "Blank\n");
        System.out.format ("%5s \t%5s \t%5s \t%5s", "~~~~~~~~~", "~~~~~~~", "~~~~~~~~~", "~~~~~");
        
        for(int i = 0; i < studentCorrectNumberOfResponses.size(); i++) {
            int numBlanks = studentCorrectNumberOfBlanks.get(i);
            int numCorrect =  studentCorrectNumberOfResponses.get(i);
            int numIncorrect = numberOfQuestions - numCorrect - numBlanks;
            System.out.format ("\n%5d      \t%5d     \t%6d   \t%4d", i + 1 , numCorrect, 
                numIncorrect, numBlanks);
        }
        System.out.println("\n");
    }
    
    /**
     * Prints the question Analysis
     * 
    */
    public static void printQuestionAnalysis() {
        //multiple choice options
        String [] choices = {"A", "B", "C", "D", "E", "Blank"};
        
        System.out.println ("\nQUESTION ANALYSIS   (* marks the correct response)");
        System.out.println ("~~~~~~~~~~~~~~~~~\n");
        
        for (int i = 0; i < correctAnswers.size(); i++){
            System.out.println ("\nQuestion #" + (i + 1) +":\n");
            
            int correctIndex = correctAnswers.get (i);
            StringBuilder header = new StringBuilder();
            StringBuilder questionStudentData = new StringBuilder();
            StringBuilder questionPercentData = new StringBuilder();
            
            for (int j = 0; j < choices.length; j++){
                
                String choice;
                int totalStudentResponses;
                String percent;
                
                //build string for question header
                if (correctIndex == j) choice = choices[j] + "*";
                else choice = choices[j];
                
                if (j == choices.length - 1) header.append (String.format ("%8s", choice) );
                else header.append (String.format ("%4s", choice) );
                header.append ("\t");
                
                
                //build string for Consolidated Student Responses for question
                totalStudentResponses = consolidatedStudentResponses.get (i)[j];
                
                if (j == choices.length - 1) questionStudentData.append (
                    String.format ("%5s", totalStudentResponses) 
                    );
                else questionStudentData.append (String.format ("%4s", totalStudentResponses) );
                questionStudentData.append ("\t");
                
                
                //build string for percentage responses for question
                percent = getPercent (totalStudentResponses, numberOfStudents);
                
                if (j == choices.length - 1) questionPercentData.append (
                    String.format ("%7s", percent) 
                    );
                else questionPercentData.append (String.format ("%6s", percent) );
                questionPercentData.append ("\t");
                
            }
            header.append("\n");
            
            //print statistics for questions
            System.out.println (header.toString());
            System.out.println (questionStudentData.toString());
            System.out.println();
            System.out.println (questionPercentData.toString());
            System.out.println();

        }
    }
    
    /**
     * Updates consolidatedStudentResponses array with total student responses for each question
     * Updates the total numbers of questions answered correctly by each student
     * @param studentResponse - the string that respresents answers to questions
    */
    
    public static void updateStudentTotalCorrect (String studentResponse) {
        int numberBlanks = 0;
        int numberCorrect = 0;
        int letterMap;
    
        for (int i = 0; i < studentResponse.length(); i++){
            
            letterMap = getLetterMapping (studentResponse.charAt (i));
            if (correctAnswers.get (i).equals (letterMap)) numberCorrect ++;
            if (letterMap == 5) numberBlanks ++;
            
            
            // update total Student responses for each question
            consolidatedStudentResponses.get (i)[letterMap]++;
        }
        
        // update Total Correct for the student
        studentCorrectNumberOfResponses.add (numberCorrect);
        studentCorrectNumberOfBlanks.add (numberBlanks);
        
    }
    
    /**
     * Creates a new array of 0's of length 6
     * @return newArray - new array of 0's
    */
    public static int [] createArrayOfZeros () {
        int [] newArray = new int [6];
        
        for(int i = 0; i < newArray.length; i++) newArray[i] = 0;
        
        return newArray;
        
    }
    
    /**
     * Calculates the percentage, given 2 numbers
     * @param num - numerator of percent fraction
     * @param total - total
     * @return newArray - new array of 0's
    */
    public static String getPercent (int num, int total){
        
        return String.format( "%.1f", ( ((double) num / (double) total) * 100.0 )) + "%";
        
    }
    
    /**
     * Controls program flow for program
    */
    public static void main (String [] args) throws IOException {
    
        Scanner scanner1 = new Scanner (System.in);
        Scanner scanner2 = new Scanner (System.in);
        
        //correct responses for all questions as a string
        String correctResponse;
        
        boolean noIoExcep = false;
        boolean noIllegalArgExcep1 = false;
        
        //start of messages
        System.out.println ("I hope that you are ready to begin ...\n");
      
        while (!noIllegalArgExcep1) {
            
            try {
                System.out.println ("\nPlease type the correct answers to the exam questions, " +
                    "one right after the other: ");
                correctResponse = scanner2.nextLine().toUpperCase().trim();
                
                // test for valid input
                for (int i = 0; i < correctResponse.length(); i++) {
                    
                    if (! isValidResponse (correctResponse.charAt (i), true)  ) {
                        
                        //clear CorrectAnswers array list and start over
                        correctAnswers.clear();
                        throw new IllegalArgumentException (
                            "Invalid Input. Enter A, B, C, D, or E only for each answer.");
                        
                    } else {
                        
                        //map correct response for question on correctAnswers ArrayList
                        correctAnswers.add (getLetterMapping (correctResponse.charAt (i)) );
                        
                        consolidatedStudentResponses.add ( createArrayOfZeros () );
                        
                    }
                    
                }
                
                
                //no IllegalArgumentExcetion thrown if input is valid
                noIllegalArgExcep1 = true;
                
                //record number of questions
                numberOfQuestions = correctResponse.length();
                
                //read in lines from the file
                while (! noIoExcep) { 
            
                    try{
                        
                        System.out.println("\nWhat is the name of the file containing each " + 
                            "student's responses to the exam questions?");
                        String studentRepsonseFile = scanner1.next();
                        FileInputStream studentResponses = new FileInputStream (studentRepsonseFile);
                        
                        InputStreamReader reader = new InputStreamReader (studentResponses);
                        BufferedReader keyboard = new BufferedReader (reader);
                        String line = keyboard.readLine ();
                        
                        //no IO exception thrown if file is valid
                        noIoExcep = true;
                        
                        System.out.println();
                        
                        //process info for class
                        while (line != null) {
                            
                            numberOfStudents++;
                            System.out.println ("Student #" + numberOfStudents + 
                                "\'s responses:  " + line );
                        
                            //calculate responses correct for student
                            updateStudentTotalCorrect (line);
                            
                            line = keyboard.readLine();
                        }
                        
                        studentResponses.close();
                        
                        System.out.println ("We have reached the \"end of file\"!");
                        
                        //check for empty file
                        if (numberOfStudents == 0) {
                            System.out.println ("You have entered a file with no students.");
                            System.exit(0);
                        }
                        
                        System.out.println ("\nThank you for the data on " + 
                            numberOfStudents + " students. Here's the analysis:");
                        
                        System.out.println("\n\n");
                        printStudentAnalysis();
                        
                        System.out.println();
                        printQuestionAnalysis();
                        
                    } catch (IOException e) {
                        System.out.println ("\nFile Not Found. Please enter valid file name. \n");
                    }     
                }
                
            } catch (IllegalArgumentException e) {
                System.out.println (e + "\nPlease try again.");
            }
            
        }
        
    }
    
}



