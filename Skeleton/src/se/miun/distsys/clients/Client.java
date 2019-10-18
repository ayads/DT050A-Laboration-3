package se.miun.distsys.clients;

public class Client{

    public Integer ID = null;
    public Boolean isCoordinator = false;

	public Client(Integer clientID){
        this.ID = clientID;
    }
}