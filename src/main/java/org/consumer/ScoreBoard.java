package org.consumer;

import org.constants.Constants;

public class ScoreBoard  {

    private class Score {
        long run = 0;
        int wicket = 0;
        long totalScore = 0;
        int totalWicket = 0;
        int updateByRunTopicCount = 0;
        int updateByWicketTopicCount = 0;
        int readCount = 0;
    }
    Score[] scores = new Score[(Constants.total_overs * 6) + 1];
    String battingTeam = "";
    String bowlingTeam = "";
    int lastReadIndex = 0;

    private int getBowlFromOver(double over) {
        int bowlsInOneOver = 6;
        int totalBowls = (int)over * bowlsInOneOver;
        double decimalPart = over % 1 + (1e-10);
        int additionalBowls = (int) (decimalPart * 10);
        totalBowls += additionalBowls;
        return totalBowls;
    }

    private double getOversFromBowl(int bowls) {
        int over = bowls/6;
        int decimal = bowls%6;
        return over + ((double) decimal /10);
    }

    private void reset() {
        for (int i = 0; i < (Constants.total_overs * 6) + 1; i++) {
            scores[i] = new Score();
        }
        battingTeam = "";
        bowlingTeam = "";
        lastReadIndex = 0;
    }
    public ScoreBoard() {
        reset();
    }
    public void update(String battingTeam, String bowlingTeam, long run,
                                    double over, boolean isWicket, boolean updateByWicket) {
        if (!this.battingTeam.equals(battingTeam)) { // This implies new match or innings, reset everything
            reset();
        }
        int index = getBowlFromOver(over);
        if (index < lastReadIndex) {
            return;
        }
        //System.out.println("Total Bowls played: " + index);
        Score score = scores[index];

        if (updateByWicket) {
            score.updateByWicketTopicCount++;
            score.wicket = isWicket ? 1 : 0;
            score.totalWicket = score.wicket + scores[index- 1].totalWicket;
        } else {
            score.updateByRunTopicCount++;
            score.run = run;
            score.totalScore += score.run + scores[index - 1].totalScore;
        }

        this.battingTeam = battingTeam;
        this.bowlingTeam = bowlingTeam;
    }

    public void commit() {
        int preLastReadIndex = lastReadIndex;
        int index = lastReadIndex;
        while (index <= (Constants.total_overs * 6)) {
            Score score = scores[index];
            //System.out.println(index + " " + score.updateByWicketTopicCount + " " + score.updateByRunTopicCount + " " + score.readCount);
            if ((score.updateByWicketTopicCount == score.updateByRunTopicCount) && (score.readCount < score.updateByRunTopicCount)) {
                System.out.println(this.battingTeam + " is " + score.totalScore + "/"
                        + score.totalWicket + " after " + getOversFromBowl(index) + " overs");
                score.readCount = score.updateByRunTopicCount;
                lastReadIndex = index;
                index++;
            } else if (index == preLastReadIndex) {
                index++;
            } else {
                break;
            }
        }
    }
}
