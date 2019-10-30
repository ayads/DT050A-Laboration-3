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
The goal is to develope an algorithm for granting the resource which satisfies the following conditions:
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
