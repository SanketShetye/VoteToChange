package com.codeforfun.himanshu.votetochange.DataObjetcs;

/**
 * Created by JAYESH WALAVALKAR on 18/03/2017.
 */

public class ElectionData {
    private String electionName;
    private String startDate;
    private int electionId;
    private String electionDescription;
    private String electionKey;
    private String candidateKey;
    private int owner;
    private int voteCount;
    private String electionStatus;
    private String winner;

    public ElectionData() {
    }

    public void setCandidateKey(String candidateKey) {
        this.candidateKey = candidateKey;
    }

    public void setElectionKey(String electionKey) {
        this.electionKey = electionKey;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setElectionName(String electionName) {
        this.electionName = electionName;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public void setElectionStatus(String electionStatus) {
        this.electionStatus = electionStatus;
    }

    public void setElectionId(int electionId) {
        this.electionId = electionId;
    }

    public void setElectionDescription(String electionDescription) {
        this.electionDescription = electionDescription;
    }

    public String getElectionName() {

        return electionName;
    }

    public String getStartDate() {
        return startDate;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getElectionStatus() {
        return electionStatus;
    }

    public int getElectionId() {
        return electionId;
    }

    public String getElectionDescription() {
        return electionDescription;
    }

    public String getCandidateKey() {
        return candidateKey;
    }

    public String getElectionKey() {
        return electionKey;
    }

    public String getWinner() {
        return winner;
    }

    public int getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return electionId+" "+electionName
                +" "+electionKey +" "+candidateKey
                +" "+winner+" "+owner
                +" "+voteCount +" "+electionStatus;
    }
}