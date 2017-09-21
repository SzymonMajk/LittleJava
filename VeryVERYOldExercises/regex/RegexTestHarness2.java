package pl.edu.agh.kis.regex;

import java.io.Console;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

public class RegexTestHarness2 {

    public static void main(String[] args){
        Pattern pattern = null;
        Matcher matcher = null;

            try{
                pattern = 
                Pattern.compile("?i)foo");

                matcher = 
                pattern.matcher("fooooofofofo foo foo 35foo");
            }
            catch(PatternSyntaxException pse){
            	
            	System.out.println("There is a problem" +
                        " with the regular expression!");
            	System.out.println("The pattern in question is: " +
                               pse.getPattern());
            	System.out.println("The description is: " +
            			pse.getDescription());
            	System.out.println("The message is: " +
            			pse.getMessage());
            	System.out.println("The index is: " +
            			pse.getIndex());
            	System.exit(0);
            }


            boolean found = false;
            while (matcher.find()) {
            	System.out.println("I found the text" +
            			 matcher.group() +" starting at " +
                        "index " +  matcher.start() +
                        "and ending at index" + matcher.end()+"\n");
                found = true;
            }
            if(!found){
                System.out.println("No match found.%n");
            }
        
    }
}