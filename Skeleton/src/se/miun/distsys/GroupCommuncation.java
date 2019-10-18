package se.miun.distsys;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import se.miun.distsys.clients.Client;
import se.miun.distsys.helpers.BullyMessageHandler;
import se.miun.distsys.listeners.ChatMessageListener;
import se.miun.distsys.listeners.JoinMessageListener;
import se.miun.distsys.listeners.LeaveMessageListener;
import se.miun.distsys.listeners.JoinResponseMessageListener;
import se.miun.distsys.listeners.ElectionRequestMessageListener;
import se.miun.distsys.listeners.ElectionResultMessageListener;
import se.miun.distsys.listeners.SequenceRequestMessageListener;
import se.miun.distsys.listeners.SequenceReplyMessageListener;
import se.miun.distsys.messages.ChatMessage;
import se.miun.distsys.messages.ElectionRequestMessage;
import se.miun.distsys.messages.ElectionResultMessage;
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
	ElectionResultMessageListener electionResultMessageListener = null;
	SequenceRequestMessageListener sequenceRequestMessageListener = null;
	SequenceReplyMessageListener sequenceReplyMessageListener = null;

	public Client myClient = createClient();
	public Vector<Integer> electionCandidateList = new Vector<>();
	public Vector<Integer> myClientList = new Vector<>();
	public BullyMessageHandler bullyMessageHandler = new BullyMessageHandler();
	
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
			} else if (message instanceof ElectionResultMessage) {
				ElectionResultMessage electionResultMessage = (ElectionResultMessage) message;
				if (electionResultMessageListener != null) {
					electionResultMessageListener.onIncomingElectionResultMessage(electionResultMessage);
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

	public void sendElectionRequestMessage(Client myClient) {
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

	public void sendElectionResultMessage(Integer myClientID) {
		try {
			ElectionResultMessage electionResultMessage = new ElectionResultMessage(myClientID);
			byte[] sendData = messageSerializer.serializeMessage(electionResultMessage);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, 
					InetAddress.getByName("255.255.255.255"), datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void sendSequenceRequestMessage(Client myClient) {
		try {
			SequenceRequestMessage sequenceRequestMessage = new SequenceRequestMessage(myClient.ID);
			byte[] sendData = messageSerializer.serializeMessage(sequenceRequestMessage);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, 
					InetAddress.getByName("255.255.255.255"), datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void sendSequenceReplyMessage(Client myClient) {
		try {
			SequenceReplyMessage sequenceReplyMessage = new SequenceReplyMessage(myClient.ID);
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

	public void setElectionResultMessageListener(ElectionResultMessageListener listener) {
		this.electionResultMessageListener = listener;
	}

	public void setSequenceRequestMessageListener(SequenceRequestMessageListener listener) {
		this.sequenceRequestMessageListener = listener;
	}

	public void setSequenceReplyMessageListener(SequenceReplyMessageListener listener) {
		this.sequenceReplyMessageListener = listener;
	}
}