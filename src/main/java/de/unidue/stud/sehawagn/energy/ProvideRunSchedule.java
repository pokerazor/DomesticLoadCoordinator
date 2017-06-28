package de.unidue.stud.sehawagn.energy;

import java.io.IOException;
import java.io.Serializable;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;

public class ProvideRunSchedule extends AchieveREResponder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4305299400892272613L;

	public ProvideRunSchedule(Agent a, MessageTemplate mt) {
		super(a, mt);
	}

	protected ACLMessage prepareResponse(ACLMessage request) {
		System.out.println("Agent " + myAgent.getLocalName() + ": REQUEST to provide a run schedule received from " + request.getSender().getName());
		// We agree to perform the action. Note that in the FIPA-Request
		// protocol the AGREE message is optional. Return null if you
		// don't want to send it.
//			System.out.println("Agent "+myAgent.getLocalName()+": Agree");
//			ACLMessage agree = request.createReply();
//			agree.setPerformative(ACLMessage.AGREE);
//			return agree;
		return null;
	}

	protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
		System.out.println("Agent " + myAgent.getLocalName() + ": computing and providing run schedule.");
		ACLMessage inform = request.createReply();
		try {
			Serializable contentObject = request.getContentObject();
			if (contentObject instanceof Flexibility) {
				Flexibility flexibility = (Flexibility) contentObject;
				System.out.println("technicalSystem.getSystemID(): " + flexibility.getSystemID());
				inform.setPerformative(ACLMessage.INFORM);
				try {
					flexibility.setStaticPhasePauses(60);
					inform.setContentObject(flexibility);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (UnreadableException e1) {
			e1.printStackTrace();
		}
		return inform;
	}
}