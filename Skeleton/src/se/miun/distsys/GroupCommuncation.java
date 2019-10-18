package se.miun.distsys;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import se.miun.distsys.clients.Client;
import se.miun.distsys.helpers.BullyMessageHandler;
import se.miun.distsys.listeners.ChatMessageListener;
import se.miun.distsys.listeners.CoordinatorMessageListener;
import se.miun.distsys.listeners.ElectionReplyMessageListener;
import se.miun.distsys.listeners.JoinMessageListener;
import se.miun.distsys.listeners.LeaveMessageListener;
import se.miun.distsys.listeners.JoinResponseMessageListener;
import se.miun.distsys.listeners.ElectionRequestMessageListener;
import se.miun.distsys.listeners.SequenceRequestMessageListener;
import se.miun.distsys.listeners.SequenceReplyMessageListener;
import se.miun.distsys.messages.ChatMessage;
import se.miun.distsys.messages.CoordinatorMessage;
import se.miun.distsys.messages.ElectionRequestMessage;
import se.miun.distsys.messages.ElectionReplyMessage;
import se.miun.distsys.messages.JoinMessage;
import se.miun.distsys.messages.LeaveMessage;
import se.miun.distsys.messages.Message;
import se.miun.distsys.messages.MessageSerializer;
import se.miun.distsys.messages.SequenceReplyMessage;
import se.miun.distsys.messages.SequenceRequestMessage;
import se.miun.distsys.messages.JoinResponseMessage;

public class GroupCommuncation {
	  
	private int datagramSocketPort = 2019;	
	DatagramSocket datagramSocket = null;
	boolean runGroupCommuncation = true;
	MessageSerializer messageSerializer = new MessageSerializer();

	ChatMessageListener chatMessageListener = null;
	JoinMessageListener joinMessageListener = null;
	LeaveMessageListener leaveMessageListener = null;
	JoinResponseMessageListener joinResponseMessageListener = null;
	ElectionRequestMessageListener electionRequestMessageListener = null;
	ElectionReplyMessageListener electionReplyMessageListener = null;
	CoordinatorMessageListener coordinatorMessageListener = null;
	SequenceRequestMessageListener sequenceRequestMessageListener = null;
	SequenceReplyMessageListener sequenceReplyMessageListener = null;

	public Client myClient = createClient();
	public HashMap<Integer, Boolean> electionCandidateList = new HashMap<>();
	public Vector<Integer> myClientList = new Vector<>();
	public BullyMessageHandler bullyMessageHandler = new BullyMessageHandler(myClient.ID, electionCandidateList);
	
	public GroupCommuncation() {
		try {
			runGroupCommuncation = true;
			datagramSocket = new MulticastSocket(datagramSocketPort);
			ReceiveThread rt = new ReceiveThread();
			rt.start();
			sendJoinMessage(myClient);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void shutdown() {
		sendLeaveMessage(myClient);
		runGroupCommuncation = false;
	}

	class ReceiveThread extends Thread{
		@Override
		public void run() {
			byte[] buffer = new byte[65536];
			DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
			while(runGroupCommuncation) {
				try {
					datagramSocket.receive(datagramPacket);
					byte[] packetData = datagramPacket.getData();
					Message receivedMessage = messageSerializer.deserializeMessage(packetData);
					handleMessage(receivedMessage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		private void handleMessage (Message message) {
			if(message instanceof ChatMessage) {
				ChatMessage chatMessage = (ChatMessage) message;
				if(chatMessageListener != null ){
					chatMessageListener.onIncomingChatMessage(chatMessage);
				}
			} else if (message instanceof JoinMessage) {
				JoinMessage joinMessage = (JoinMessage) message;
				if (joinMessageListener != null) {
					joinMessageListener.onIncomingJoinMessage(joinMessage);
				}
			} else if (message instanceof JoinResponseMessage) {
				JoinResponseMessage joinResponseMessage = (JoinResponseMessage) message;
				if (joinResponseMessageListener != null) {
					joinResponseMessageListener.onIncomingJoinResponseMessage(joinResponseMessage);
				}
			} else if (message instanceof LeaveMessage) {
				LeaveMessage leaveMessage = (LeaveMessage) message;
				if (leaveMessageListener != null) {
					leaveMessageListener.onIncomingLeaveMessage(leaveMessage);
				}
			} else if (message instanceof ElectionRequestMessage) {
				ElectionRequestMessage electionRequestMessage = (ElectionRequestMessage) message;
				if (electionRequestMessageListener != null) {
					electionRequestMessageListener.onIncomingElectionRequestMessage(electionRequestMessage);
				}
			} else if (message instanceof ElectionReplyMessage) {
				ElectionReplyMessage electionReplyMessage = (ElectionReplyMessage) message;
				if (electionReplyMessageListener != null) {
					electionReplyMessageListener.onIncomingElectionReplyMessage(electionReplyMessage);
				}
			} else if (message instanceof CoordinatorMessage) {
				CoordinatorMessage coordinatorMessage = (CoordinatorMessage) message;
				if (electionReplyMessageListener != null) {
					coordinatorMessageListener.onIncomingCoordinatorMessage(coordinatorMessage);
				}
			} else if (message instanceof SequenceRequestMessage) {
				SequenceRequestMessage sequenceRequestMessage = (SequenceRequestMessage) message;
				if (sequenceRequestMessageListener != null) {
					sequenceRequestMessageListener.onIncomingSequenceRequestMessage(sequenceRequestMessage);
				}
			} else if (message instanceof SequenceReplyMessage) {
				SequenceReplyMessage sequenceReplyMessage = (SequenceReplyMessage) message;
				if (sequenceReplyMessageListener != null) {
					sequenceReplyMessageListener.onIncomingSequenceReplyMessage(sequenceReplyMessage);
				}
			} else {
				System.out.println("Unknown message type");
			}
		}
	}
	
	public Client createClient() {
		try {
			Thread.sleep(250);
			Client newClient = new Client(ThreadLocalRandom.current().nextInt(0, 100));
			return newClient;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void sendChatMessage(Client myClient, String myChat) {
		try {
			ChatMessage chatMessage = new ChatMessage(myClient.ID, myChat);
			byte[] sendData = messageSerializer.serializeMessage(chatMessage);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, 
					InetAddress.getByName("255.255.255.255"), datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendJoinMessage(Client myClient) {
		try {
			JoinMessage joinMessage = new JoinMessage(myClient.ID);
			byte[] sendData = messageSerializer.serializeMessage(joinMessage);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, 
					InetAddress.getByName("255.255.255.255"), datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendJoinResponseMessage(Client myClient) {
		try {
			JoinResponseMessage joinResponseMessage = new JoinResponseMessage(myClient.ID);
			byte[] sendData = messageSerializer.serializeMessage(joinResponseMessage);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, 
					InetAddress.getByName("255.255.255.255"), datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	public void sendLeaveMessage(Client myClient) {
		try {
			LeaveMessage leaveMessage = new LeaveMessage(myClient.ID);
			byte[] sendData = messageSerializer.serializeMessage(leaveMessage);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, 
					InetAddress.getByName("255.255.255.255"), datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendElectionRequestMessage(Integer myClientID) {
		try {
			ElectionRequestMessage electionRequestMessage = new ElectionRequestMessage(myClient.ID);
			byte[] sendData = messageSerializer.serializeMessage(electionRequestMessage);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, 
					InetAddress.getByName("255.255.255.255"), datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendElectionReplyMessage(Integer myClientID) {
		try {
			ElectionReplyMessage electionReplyMessage = new ElectionReplyMessage(myClientID);
			byte[] sendData = messageSerializer.serializeMessage(electionReplyMessage);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, 
					InetAddress.getByName("255.255.255.255"), datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendCoordinatorMessage(Integer myClientID) {
		try {
			CoordinatorMessage coordinatorMessage = new CoordinatorMessage(myClientID);
			byte[] sendData = messageSerializer.serializeMessage(coordinatorMessage);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, 
					InetAddress.getByName("255.255.255.255"), datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendSequenceRequestMessage(Integer myClientID) {
		try {
			SequenceRequestMessage sequenceRequestMessage = new SequenceRequestMessage(myClientID);
			byte[] sendData = messageSerializer.serializeMessage(sequenceRequestMessage);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, 
					InetAddress.getByName("255.255.255.255"), datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void sendSequenceReplyMessage(Integer myClientID) {
		try {
			SequenceReplyMessage sequenceReplyMessage = new SequenceReplyMessage(myClientID);
			byte[] sendData = messageSerializer.serializeMessage(sequenceReplyMessage);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, 
					InetAddress.getByName("255.255.255.255"), datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setChatMessageListener(ChatMessageListener listener) {
		this.chatMessageListener = listener;
	}

	public void setJoinMessageListener(JoinMessageListener listener) {
		this.joinMessageListener = listener;
	}

	public void setJoinResponseMessageListener(JoinResponseMessageListener listener) {
		this.joinResponseMessageListener = listener;
	}

	public void setLeaveMessageListener(LeaveMessageListener listener) {
		this.leaveMessageListener = listener;
	}

	public void setElectionRequestMessageListener(ElectionRequestMessageListener listener) {
		this.electionRequestMessageListener = listener;
	}

	public void setElectionResultMessageListener(ElectionReplyMessageListener listener) {
		this.electionReplyMessageListener = listener;
	}

	public void setCoordinatorMessageListener(CoordinatorMessageListener listener) {
		this.coordinatorMessageListener = listener;
	}

	public void setSequenceRequestMessageListener(SequenceRequestMessageListener listener) {
		this.sequenceRequestMessageListener = listener;
	}

	public void setSequenceReplyMessageListener(SequenceReplyMessageListener listener) {
		this.sequenceReplyMessageListener = listener;
	}
}