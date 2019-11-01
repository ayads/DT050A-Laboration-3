# DT050A-Laboration-3
Implement a distributed election to process to elect a leader (Bully algorithm).

NOTE:
> In this last laboration you are expected to  This laboration is optional, but you must do it in order to be eligible to get an A or B on the course. You must finish this laboration before the course ends.

Use the basic program you coded in Laboration 1, which you now will need to extend to also do the following:
## Goal 
Implement a distributed election to process to elect a leader (Bully algorithm). As well as implement total ordering, managed by this leader.

## General Tasks
### Implement the bully algorithm to elect a leader
- [X] Send election message when nodes join, leave, or if the leader is disconnected.
- [X] Everyone answers the election message.
- [X] A winner is announced.
### Implement total ordering, managed by the elected leader
- [X] Before each chat message is sent. Get a sequence number from the leader.
- [X] When receiving a chat message, check if it has the correct sequence number.
- [X] Never display a message on the screen that is ahead of or out of order.

## Bully Algorithm
The goal is to develop an algorithm for granting the resource which satisfies the following conditions:
ME1: (Safety) At most one process may execute in the critical section (CS) at a time.
ME2: (liveness) if every process which is granted the resource eventually releases it, then every request is eventually granted.
ME3: different requests for the resource must be granted in the order in which they are made.
An election process is typically performed in two phases:
1. Select a leader with the highest identifier.
2. Inform all processes about the winner.
### Coordinator Election steps
> When process `pi` initiates an election:
* `state_pi = ELECTION-ON`
* `pi` sends an `electionRequestMessage` to all process with a higher identifier.
* If no answer message arrives before time-out then `pi` is the coordinator and sends a `coordinatorMessage` to all processes else `pi` waits for a coordinator message to arrive.
* if no coordinator message arrives before time-out then restart election procedure

> When process `pj` receives an `electionRequestMessage` from `pi`:
* `pj` replies with an `electionReplyMessage` to `pi`.
* If `state_pj = ELECTION-OFF` then start election procedure.

## Project Structure
```
.
└── src
    ├── se
    │   └── miun
    │       └── distsys
    │           ├── clients
    │           │   └── Client.java
    │           ├── helpers
    │           │   └── BullyMessageHandler.java
    │           ├── listeners
    │           │   ├── ChatMessageListener.java
    │           │   ├── CoordinatorMessageListener.java
    │           │   ├── ElectionReplyMessageListener.java
    │           │   ├── ElectionRequestMessageListener.java
    │           │   ├── JoinMessageListener.java
    │           │   ├── JoinResponseMessageListener.java
    │           │   ├── LeaveMessageListener.java
    │           │   ├── SequenceReplyMessageListener.java
    │           │   └── SequenceRequestMessageListener.java
    │           ├── messages
    │           │   ├── BullyMessage.java
    │           │   ├── ChatMessage.java
    │           │   ├── CoordinatorMessage.java
    │           │   ├── ElectionMessage.java
    │           │   ├── ElectionReplyMessage.java
    │           │   ├── ElectionRequestMessage.java
    │           │   ├── JoinMessage.java
    │           │   ├── JoinResponseMessage.java
    │           │   ├── LeaveMessage.java
    │           │   ├── Message.java
    │           │   ├── MessageSerializer.java
    │           │   ├── SequenceMessage.java
    │           │   ├── SequenceReplyMessage.java
    │           │   └── SequenceRequestMessage.java
    │           └── GroupCommunication.java
    ├── Program.java
    └── WindowProgram.java
```

![Message structure](/Images/MessageStructure.png)

## Code description

When a new client joins the chat they send a "join message" to all the other participants in the chat.The new client is added to the client list and an election starts to decide who has the highest ID and hence will be chosen as the coordinator of the chat. 

```java
    public void onIncomingJoinMessage(JoinMessage joinMessage) {
        try {
            gc.myClientList.put(joinMessage.clientID, false);
            txtpnStatus.setText(joinMessage.clientID + " join." + "\n" + txtpnStatus.getText());
            if(joinMessage.clientID != gc.myClient.ID) {
                gc.sendJoinResponseMessage(gc.myClient);
			}
			gc.sendElectionRequestMessage(joinMessage.clientID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```

When an election request is made the recieving clients will compare their ID to the election leaders ID. If the recieving clients ID is larger(or equal to) than the election leaders ID then their ID will send a message back to the election leader letting the election leader know that they are up for election.

If no client has a larged ID than the election leader the leader will be chosen as the new coordinator.

```java
	public void onIncomingElectionRequestMessage(ElectionRequestMessage electionRequestMessage) {
		try {				
			if (gc.myClient.ID >= electionRequestMessage.clientID){
				gc.sendElectionReplyMessage(gc.myClient.ID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
```

If one or multiple participants are found with a higher ID than the election leader and answers the election message in time the participants with a higher ID are added to the list of posible coordinators.

The largest ID in the list of posible coordinators is then chosen as the new coordinator of the group chat and has to send a message to all the chat members letting them know that they are chosen as the new coordinator.

```java
	public void onIncomingElectionReplyMessage(ElectionReplyMessage electionReplyMessage) {
		try {
			if(gc.bullyMessageHandler.isWithinTimeoutLimit(electionReplyMessage.startElectionTime)){
				gc.electionCandidateList.add(electionReplyMessage.clientID);
				Integer maxClientID = (Collections.max(gc.electionCandidateList));
				gc.sendCoordinatorMessage(maxClientID);
			} else {
				gc.sendCoordinatorMessage(electionReplyMessage.clientID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
```

All clients are set to FALSE in the 'myClientList' and the new coordinator is mapped to TRUE. A coordinator message is sent from the new coordinator to all participants in the chat.

```java
	public void onIncomingCoordinatorMessage(CoordinatorMessage coordinatorMessage) {
		try {
			for (HashMap.Entry<Integer, Boolean> entry : gc.myClientList.entrySet()) {
				gc.myClientList.put(entry.getKey(),false);
			}
			gc.myClientList.put(coordinatorMessage.clientID, gc.bullyMessageHandler.setCoordinator());
			System.out.println("Coordinator: " + gc.myClientList);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
```

When a chat message is sent the coordinator has to be noticed so that messages are sent in the correct order to ensure Total Ordering. This is done by sending a request to sendSequenceRequestMessage.

```java
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equalsIgnoreCase("send")) {
			gc.sendSequenceRequestMessage(gc.myClient.ID, txtpnMessage.getText());
		}
	}
```

If the current client is the coordinator then it controls the sending of the requested message.

```java
	public void onIncomingSequenceRequestMessage(SequenceRequestMessage sequenceRequestMessage) {
		try {
			if(gc.myClientList.get(gc.myClient.ID)){
				gc.sendSequenceReplyMessage(sequenceRequestMessage.clientID, sequenceRequestMessage.chat, sequenceRequestMessage.startTime);
				System.out.println(sequenceRequestMessage.startTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
```

Using the timestamp of the message and comparing it to the previous messages timestamp. If the previous timestamp is less than the new messages timestamp the message can be sent. The new time is then set as the previous for the next sent message.

```java
	public void onIncomingSequenceReplyMessage(SequenceReplyMessage sequenceReplyMessage) {
		try {
			if(previousTime<sequenceReplyMessage.time){
				txtpnChat.setText(sequenceReplyMessage.clientID + sequenceReplyMessage.chat + "\n" + txtpnChat.getText());
				previousTime = sequenceReplyMessage.time;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
```

When a client leaves the chat group a new coordinator must be elected. To achieve that the client must first be removed from the candidate list. After that the other clients will send an election request appended with their id through group communication.
```java
	public void onIncomingLeaveMessage(LeaveMessage leaveMessage) {
		try {
			if (gc.myClientList.containsKey(leaveMessage.clientID)){
				txtpnStatus.setText(leaveMessage.clientID + " left." + "\n" + txtpnStatus.getText());
				gc.electionCandidateList.remove(leaveMessage.clientID);
				if(gc.myClientList.get(leaveMessage.clientID)){
					gc.myClientList.remove(leaveMessage.clientID);
					if(Integer.compare(gc.myClient.ID, leaveMessage.clientID) != 0){
						gc.sendElectionRequestMessage(gc.myClient.ID);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
```