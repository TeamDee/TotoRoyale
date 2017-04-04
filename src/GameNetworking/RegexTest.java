package GameNetworking;

import GameModel.Map.TriHexTile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Z_K on 4/1/2017.
 */
/*public class RegexTest {
    public static void main(String[] args){
        String s = "Hello my name is 23 ha 46";
        Matcher m = Pattern.compile("^Hello my name is ([\\d]*) ha ([\\d]*)$").matcher(s);
        if(m.matches())
        {
            System.out.println("Before Name: " + m.group(0));
            System.out.println("Name entered: " + m.group(1));
            System.out.println("2nd entered: " + m.group(2));
        }

        Pattern myPattern = Pattern.compile("I have ([\\d]*) apples, ([\\d]*) oranges, and ([\\d]*) bananas");
        String myWord = "I have 2 apples, 6 oranges, and 10 bananas";
        Matcher myMatcher = myPattern.matcher(myWord);
        if(myMatcher.matches()) {
            for (int i = 1; i < 4; i++) {
                System.out.println("Item " + i + ": " + myMatcher.group(i));
            }
        }

        String exampleTile = "JUNGLE+ROCK";
//        String[] splited = exampleTile.split("[+]");
//        for(String str : splited){
//            System.out.println("Item: " + str);
//        }
        TriHexTile tht = TigerLandDelegate.makeTriHexTileFromString(exampleTile);
        System.out.println(tht.toString());
    }
}*/
