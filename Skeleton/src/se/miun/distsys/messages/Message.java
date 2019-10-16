package se.miun.distsys.messages;

import java.io.Serializable;
import java.util.HashMap;

public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	public Boolean isLeader = false;
	public HashMap<Integer, Integer> messageVectorClock = null;
}