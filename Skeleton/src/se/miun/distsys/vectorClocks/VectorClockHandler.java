package se.miun.distsys.vectorClocks;

import java.util.HashMap;

import se.miun.distsys.clients.Client;
import se.miun.distsys.messages.Message;

public class VectorClockHandler{
	private Integer myClientID = null;
	private HashMap<Integer, Integer> myVectorClock;

	public VectorClockHandler(Client client, HashMap<Integer, Integer> myVectorClock){
		this.myClientID = client.getID();
		this.myVectorClock = myVectorClock;
		myVectorClock.put(myClientID, 0);
	}

	public void printVectorClock(HashMap<Integer, Integer> myVectorClock) {
		myVectorClock.forEach((key, value) -> System.out.println("clientID: " + key + "\t" + "Timestamp: " + value));
		System.out.println("\n");
	}

	public void incrementVectorClock(Message message){
		int timestamp = myVectorClock.get(myClientID) + 1;
		myVectorClock.put(myClientID, timestamp);
		message.messageVectorClock = (HashMap<Integer, Integer>) myVectorClock.clone();
	}

	public Boolean isCausalOrder (Message receivedMessage, HashMap<Integer, Integer> myVectorClock){
		if (receivedMessage.messageVectorClock.containsKey(myClientID) && receivedMessage.messageVectorClock.get(myClientID) > myVectorClock.get(myClientID) ) {
			return false;
		}
		return true;
	}
}