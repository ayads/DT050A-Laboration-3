package se.miun.distsys.helpers;

import java.util.Collections;
import java.util.HashMap;


public class BullyMessageHandler {

	public BullyMessageHandler(){
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