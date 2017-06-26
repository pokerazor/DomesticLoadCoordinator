package de.unidue.stud.sehawagn.energy;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

import energy.optionModel.TechnicalSystem;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import jade.util.leap.Iterator;

public class RequestRunSchedule extends AchieveREInitiator {

	private static MessageTemplate messageTemplate;

	public RequestRunSchedule(Agent a, ACLMessage msg) {
		super(a, msg);
		Iterator receivers = msg.getAllIntendedReceiver();
		while (receivers.hasNext()) {
			AID recvAID = (AID) receivers.next();
			System.out.println("message intended for "+recvAID);
		}
		
		Vector<ACLMessage> allInitMessages = (Vector<ACLMessage>) getDataStore().get(ALL_INITIATIONS_K);
		if(allInitMessages!=null){
			System.out.println("sent messages");
			for (ACLMessage aclMessage : allInitMessages) {
				Iterator actualreceivers = aclMessage.getAllIntendedReceiver();
				while (actualreceivers.hasNext()) {
					AID recvAID = (AID) actualreceivers.next();
					System.out.println("message sent to "+recvAID);
				}
			}
		}

		
		System.out.println("RequestRunSchedule constructor");

	}

	public static ACLMessage prepareRequestMessage(Agent a, Serializable content) {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		Helper.fillMessage(msg, content, a);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		// We want to receive a reply in 10 secs
		msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));


		System.out.println("RequestRunSchedule prepareRequestMessage");

		return msg;
	}

	protected void handleInform(ACLMessage inform) {
		System.out.println("Handling INFORM from " + inform.getSender().getName() + " requested Run Schedule should have been received");
		try {
			Serializable contentObject = inform.getContentObject();
			if (contentObject instanceof TechnicalSystem) {
				TechnicalSystem technicalSystem = (TechnicalSystem) contentObject;
				System.out.println("technicalSystem.getSystemID(): " + technicalSystem.getSystemID());

				technicalSystem.getSystemID();
			}
		} catch (UnreadableException e) {
			e.printStackTrace();
		}

	}

	public static MessageTemplate getMessageTemplate() {
		if (messageTemplate == null) {
			messageTemplate = MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
//			messageTemplate = MessageTemplate.and(messageTemplate, MessageTemplate.MatchConversationId(DomesticLoadCoordinatorAgent.CONVERSATION_ID_REQUEST_SCHEDULE));
		}
		return messageTemplate;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3530258942813425070L;

}
