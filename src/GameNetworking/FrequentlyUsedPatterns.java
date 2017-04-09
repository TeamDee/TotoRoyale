package GameNetworking;

import java.util.regex.Pattern;

/**
 * Created by Z_K on 4/1/2017.
 */
public class FrequentlyUsedPatterns {
    static Pattern WelcomeMssgPattern = Pattern.compile("^WELCOME TO ANOTHER EDITION OF THUNDERDOME!$");

    static Pattern PlayerIDMssgPattern = Pattern.compile("^WAIT FOR THE TOURNAMENT TO BEGIN ([\\d]*)$");

    static Pattern ChallengeMssgPattern = Pattern.compile("^NEW CHALLENGE ([\\d]*) YOU WILL PLAY ([\\d]*) MATCH[ES]*$");
    static Pattern ChallengeOverMssgPattern = Pattern.compile("^END OF CHALLENGES$");

    static Pattern RoundBeginMssgPattern = Pattern.compile("^BEGIN ROUND ([\\d]*) OF ([\\d]*)$");
    static Pattern RoundEndMssgPattern = Pattern.compile("^END OF ROUND ([\\d]*) OF ([\\d]*)$");
    static Pattern RoundEndNextMssgPattern = Pattern.compile("^END OF ROUND ([\\d]*) OF ([\\d]*) WAIT FOR THE NEXT MATCH$");

    static Pattern NewMatchMssgPattern = Pattern.compile("^NEW MATCH BEGINNING NOW YOUR OPPONENT IS PLAYER ([\\d]*)$");

    static Pattern MoveServerPromptMssgPattern = Pattern.compile("^MAKE YOUR MOVE IN GAME ([\\d]*) WITHIN ([\\d.]*) SECOND[S]*: MOVE ([\\d]*) PLACE ([\\w+]*)$");
    static Pattern GameMovePlayerMssgPattern = Pattern.compile("^GAME ([\\d]*) MOVE ([\\d]*) PLAYER ([\\d]*) ([\\w\\d ]*)$");
    static Pattern GameForfeitedMssgPattern = Pattern.compile("^GAME ([\\d]*) MOVE ([\\d]*) PLAYER ([\\d]*) FORFEITED:([\\w ]*)$");
    static Pattern GameLostMssgPattern = Pattern.compile("^GAME ([\\d]*) MOVE ([\\d]*) PLAYER ([\\d]*) LOST:([\\w ]*)$");
    static Pattern GameOverMssgPattern = Pattern.compile("^GAME ([\\d]*) OVER PLAYER ([\\d]*) ([\\d]*) PLAYER ([\\d]*) ([\\d]*)$");

    static Pattern ExitMssgPattern = Pattern.compile("^THANK YOU FOR PLAYING! GOODBYE$");

    public static Pattern PlacementMssgPattern = Pattern.compile("^PLACED ([\\w]*) AT ([\\d]*) ([\\d]*) ([\\d]*) ([\\d ]*)$");
    public static Pattern BuildPattern = Pattern.compile("^([\\w ]*) AT ([\\d]*) ([\\d]*) ([\\d]*)([\\w ]*)$");
}

