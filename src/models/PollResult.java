package models;

import java.io.Serializable;

/**
 * Created by danielgek on 17/10/15.
 */
public class PollResult implements Serializable {

    int answerAVotes;
    int answerBVotes;

    public PollResult(int answerAVotes, int answerBVotes) {
        this.answerAVotes = answerAVotes;
        this.answerBVotes = answerBVotes;
    }

    public int getAnswerAVotes() {
        return answerAVotes;
    }

    public void setAnswerAVotes(int answerAVotes) {
        this.answerAVotes = answerAVotes;
    }

    public int getAnswerBVotes() {
        return answerBVotes;
    }

    public void setAnswerBVotes(int answerBVotes) {
        this.answerBVotes = answerBVotes;
    }

    @Override
    public String toString() {
        return "PollResult{" +
                "answerAVotes=" + answerAVotes +
                ", answerBVotes=" + answerBVotes +
                '}';
    }
}
