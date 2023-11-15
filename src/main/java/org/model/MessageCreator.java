package org.model;

import org.constants.Constants;
import org.json.simple.JSONObject;

public class MessageCreator {
    public static String CreateRunMessage(String battingTeam, String bowlingTeam,
                                          double ball, int run) {

        JSONObject obj = new JSONObject();
        obj.put(Constants.BATTING_KEY, battingTeam);
        obj.put(Constants.BOWLING_KEY, bowlingTeam);
        obj.put(Constants.BALL_KEY, ball);
        obj.put(Constants.RUN_KEY, run);
        return obj.toJSONString();
    }

    public static String CreateWicketMessage(String battingTeam, String bowlingTeam,
                                          double ball, boolean out) {
        JSONObject obj = new JSONObject();
        obj.put(Constants.BATTING_KEY, battingTeam);
        obj.put(Constants.BOWLING_KEY, bowlingTeam);
        obj.put(Constants.BALL_KEY, ball);
        obj.put(Constants.WICKET_KEY, out);
        return obj.toJSONString();
    }
}
