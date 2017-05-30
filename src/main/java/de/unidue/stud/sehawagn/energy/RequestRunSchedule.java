package de.unidue.stud.sehawagn.energy;

import java.io.Serializable;

import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;

public class RequestRunSchedule extends AchieveREInitiator {

	private MessageTemplate messageTemplate;

	public RequestRunSchedule(Agent a, Serializable content) {
		this(a, prepareRequestMessage(a,content));
		// TODO Auto-generated constructor stub
	}
	
	public RequestRunSchedule(Agent a, ACLMessage msg) {
		super(a, msg);
	}
	
	public static ACLMessage prepareRequestMessage(Agent a, Serializable content){
		ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
		Helper.fillMessage(msg, content, a);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		return msg;
	}
	
	private MessageTemplate getMessageTemplate(){
		if(messageTemplate == null){
			messageTemplate = MessageTemplate.MatchConversationId(DomesticLoadCoordinatorAgent.CONVERSATION_ID_REQUEST_SCHEDULE);
			messageTemplate = MessageTemplate.and(messageTemplate, MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST));
		}
		return this.messageTemplate;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3530258942813425070L;

}
