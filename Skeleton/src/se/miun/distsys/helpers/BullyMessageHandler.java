package se.miun.distsys.helpers;

import java.util.Collections;
import java.util.Vector;


public class BullyMessageHandler {

	public BullyMessageHandler(){
    }

	public Boolean isWithinTimeoutLimit(long startElectionTime){
		long estimatedElectionTime = System.currentTimeMillis() - startElectionTime;
		if (estimatedElectionTime < 250){
			return true;
		}
		return false;
	}

	public Vector<Integer> getElectionWinner(Vector<Integer> electionCandidateList){
		Integer maxClientID = (Collections.max(electionCandidateList));
		electionCandidateList.add(maxClientID);
		return electionCandidateList;
	}

	

    public Vector<Integer> removeDuplicates(Vector<Integer> electionCandidateList){
    	for(int i=0; i < electionCandidateList.size(); i++){
    	    for(int j=0; j < electionCandidateList.size(); j++){
    	    	if(i != j){
    	        	if(electionCandidateList.elementAt(i).equals(electionCandidateList.elementAt(j))){
						electionCandidateList.removeElementAt(j);
    	            }
    	        }
    	    }
		}
		return electionCandidateList;
	}
	
	public Boolean setCoordinator() {
		return true;
	}
}