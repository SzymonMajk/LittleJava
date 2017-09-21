package pl.edu.agh.kis.regex;

import java.io.Console;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class E1 {

    public static void main(String[] args){
    	/*
        Console console = System.console();
        if (console == null) {
            System.err.println("No console.");
            System.exit(1);
        }*/

            /*Pattern pattern = 
            Pattern.compile(console.readLine("%nEnter your regex: "));
*/
        	Pattern pattern = Pattern.compile("foo");
            /*Matcher matcher = 
            pattern.matcher(console.readLine("Enter input string to search: "));
*/
        	
        	Matcher matcher = pattern.matcher("foofoofoo");

            
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
            
            pattern = Pattern.compile("[0-4[6-8]]");
            matcher = pattern.matcher("5");
            
            found = false;
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
            
            System.out.println("Nested Classes:");
            
            pattern = Pattern.compile("0-9&&[345]");
            matcher = pattern.matcher("2");
            
            found = false;
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
            
            System.out.println("Quantifiers:");
            
            pattern = Pattern.compile("a+");
            matcher = pattern.matcher("bbbba2");
            
            found = false;
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
            
            
            
            System.out.println("Backreferences:");
            
            pattern = Pattern.compile("(\\d\\d)\\1");
            matcher = pattern.matcher("121213121313");
            
            found = false;
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
            
            
            System.out.println("Boundary Matchers:");
            
            pattern = Pattern.compile("\\bdog\\B");
            matcher = pattern.matcher("The doggie plays in the yard.");
            
            found = false;
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
    }
}