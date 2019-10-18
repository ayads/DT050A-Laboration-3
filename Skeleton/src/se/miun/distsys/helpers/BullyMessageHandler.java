package se.miun.distsys.helpers;

import java.util.Vector;

import se.miun.distsys.clients.Client;

public class BullyMessageHandler {

	public BullyMessageHandler(){
    }

	public Client getMax(Client a, Client b){
		return (a.ID > b.ID) ? a : b;
	}

	public Boolean isTimeout(long startElectionTime){
		long estimatedElectionTime = System.currentTimeMillis() - startElectionTime;
		if (estimatedElectionTime < 100){
			return true;
		}
		return false;
	}

	public Integer getMaxCandidate(Vector<Client> electionCandidateList){
    	Integer maxCandidate = Integer.MIN_VALUE;
    	for(int i = 0; i < electionCandidateList.size(); i++){
			if(electionCandidateList.get(i).ID > maxCandidate){
    	        maxCandidate = electionCandidateList.get(i).ID;
    	    }
    	}
    	return maxCandidate;
	}
	
	public void assignCoordinator() {

	}
}