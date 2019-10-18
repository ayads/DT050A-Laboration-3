package se.miun.distsys.helpers;

import java.util.Collections;
import java.util.HashMap;


public class BullyMessageHandler {

	private Integer myClientID = null;
	private HashMap<Integer, Boolean> myElectionCandidateList;

	public BullyMessageHandler(Integer clientID, HashMap<Integer, Boolean> myElectionCandidateList){
		this.myClientID = clientID;
		this.myElectionCandidateList = myElectionCandidateList;
		myElectionCandidateList.put(myClientID, false);
    }

	public Boolean isWithinTimeoutLimit(long startElectionTime){
		long estimatedElectionTime = System.currentTimeMillis() - startElectionTime;
		if (estimatedElectionTime < 100){
			return true;
		}
		return false;
	}

	public HashMap<Integer, Boolean> getElectionWinner(HashMap<Integer, Boolean> electionCandidateList){
		Integer maxClientID = (Collections.max(electionCandidateList.keySet()));
		electionCandidateList.put(maxClientID, setCoordinator());
		return electionCandidateList;
	}
	
	public Boolean setCoordinator() {
		return true;
	}
}