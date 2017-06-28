package de.unidue.stud.sehawagn.energy;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;

public class RequestRunSchedule extends AchieveREInitiator {

	private static final long serialVersionUID = -3530258942813425070L;
	private static MessageTemplate messageTemplate;
	private ScheduleRequester requester;

	public static MessageTemplate getMessageTemplate() {
			if (messageTemplate == null) {
				messageTemplate = MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
	//			messageTemplate = MessageTemplate.and(messageTemplate, MessageTemplate.MatchConversationId(DomesticLoadCoordinatorAgent.CONVERSATION_ID_REQUEST_SCHEDULE));
			}
			return messageTemplate;
		}

	public static ACLMessage prepareRequestMessage(Agent a, Serializable content) {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			Helper.fillMessage(msg, content, a);
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
	
			// We want to receive a reply in 10 secs
			msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
	
	//		System.out.println("RequestRunSchedule prepareRequestMessage");
	
			return msg;
		}

	public RequestRunSchedule(Agent a, ACLMessage msg, ScheduleRequester requester) {
		super(a, msg);
		this.requester=requester;

/*
		Iterator receivers = msg.getAllIntendedReceiver();
		while (receivers.hasNext()) {
			AID recvAID = (AID) receivers.next();
//			System.out.println("message intended for "+recvAID);
		}
		
		Vector<ACLMessage> allInitMessages = (Vector<ACLMessage>) getDataStore().get(ALL_INITIATIONS_K);
		if(allInitMessages!=null){
//			System.out.println("sent messages");
			for (ACLMessage aclMessage : allInitMessages) {
				Iterator actualreceivers = aclMessage.getAllIntendedReceiver();
				while (actualreceivers.hasNext()) {
					AID recvAID = (AID) actualreceivers.next();
//					System.out.println("message sent to "+recvAID);
				}
			}
		}

		
//		System.out.println("RequestRunSchedule constructor");
*/
	}

	@Override
	protected void handleInform(ACLMessage inform) {
//		System.out.println("Handling INFORM from " + inform.getSender().getName() + " requested Run Schedule should have been received");
		try {
			Serializable contentObject = inform.getContentObject();
			if (contentObject instanceof Flexibility) {
				Flexibility technicalSystem = (Flexibility) contentObject;
				requester.processReceivedSchedule(technicalSystem);
//				System.out.println("technicalSystem.getSystemID(): " + technicalSystem.getSystemID());
			}
		} catch (UnreadableException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	protected void handleRefuse(ACLMessage refuse) {
		System.err.println("Agent "+refuse.getSender().getName()+" refused to perform the requested action");
	}
	
	@Override
	protected void handleFailure(ACLMessage failure) {
		if (failure.getSender().equals(myAgent.getAMS())) {
			// FAILURE notification from the JADE runtime: the receiver does not exist
			System.err.println("Responder does not exist");
		}
		else {
			System.err.println("Agent "+failure.getSender().getName()+" failed to perform the requested action");
		}
	}
	
	@Override
	protected void handleAllResultNotifications(@SuppressWarnings("rawtypes") Vector notifications) {
		if (notifications.isEmpty()) {
			// Some responder didn't reply within the specified timeout
			System.err.println("Timeout expired: missing response");
		}
	}
}