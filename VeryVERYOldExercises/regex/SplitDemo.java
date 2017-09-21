package pl.edu.agh.kis.regex;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class SplitDemo {

    private static final String REGEX = ":";
    private static final String INPUT =
        "one:two:three:four:five";
    
    private static final String REGEX2 = "\\d";
    private static final String INPUT2 =
        "one9two4three7four1five";
    
    public static void main(String[] args) {
        Pattern p = Pattern.compile(REGEX);
        String[] items = p.split(INPUT);
        for(String s : items) {
            System.out.println(s);
        }
        
        System.out.println("\ni druga:");
        
        p = Pattern.compile(REGEX2);
        items = p.split(INPUT2);
        for(String s : items) {
            System.out.println(s);
        }
    }

}