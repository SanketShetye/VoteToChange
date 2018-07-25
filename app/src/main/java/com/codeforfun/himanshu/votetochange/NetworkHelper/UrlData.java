package com.codeforfun.himanshu.votetochange.NetworkHelper;

/**
 * Created by Himanshu on 13-03-2017.
 */

public class UrlData {

    public static final String BASE_URL = "http://codeforfun.16mb.com/VoteToChange/";

    public static final String LOGIN_URL = BASE_URL + "loginAuth.php";
    public static final String REGISTER_URL = BASE_URL + "registerUser.php";
    public static final String REGISTER_ELECTION_URL =BASE_URL + "registerElection.php";
    public static final String CANDIDATE_LIST_URL = BASE_URL + "candidateList.php";
    public static final String VALIDATING_ELECTION_KEY_URL = BASE_URL + "validateElectionKey.php";
    public static final String REGISTER_AS_CANDIDATE = BASE_URL + "registerCandidate.php";
    public static final String VOTE_FOR_CANDIDATE = BASE_URL + "vote.php";
    public static final String ELECTION_STATUS = BASE_URL + "getElectionStatus.php";
    public static final String START_ELECTION = BASE_URL + "startElectionProcess.php";
    public static final String END_ELECTION = BASE_URL + "endElectionProcess.php";
}