package de.unidue.stud.sehawagn.energy;

import java.io.Serializable;

import energy.optionModel.TechnicalSystem;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;

public class RequestRunSchedule extends AchieveREInitiator {

	private static MessageTemplate messageTemplate;

	public RequestRunSchedule(Agent a, ACLMessage msg) {
		super(a, msg);
	}

	public static ACLMessage prepareRequestMessage(Agent a, Serializable content) {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		Helper.fillMessage(msg, content, a);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		System.out.println("RequestRunSchedule from Domestic Load Coordinator Agent ");

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
