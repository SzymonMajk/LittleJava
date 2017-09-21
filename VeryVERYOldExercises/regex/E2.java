package pl.edu.agh.kis.regex;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class E2 {


    public static void main(String[] args){
	
        Pattern pattern = Pattern.compile("dog",Pattern.CASE_INSENSITIVE);   
        Matcher matcher = pattern.matcher("DoGDOg");

            
       boolean found = false;
       
       while (matcher.find()) {
    	   	System.out.println("I found the text " +
                 matcher.group() + " starting at " +
                "index " + matcher.start() + " and ending at index " +
                matcher.end());
            found = true;
            }
        if(!found){
            System.out.println("No match found");
        }
        
        System.out.println("Union:");

        
    }         
    
}