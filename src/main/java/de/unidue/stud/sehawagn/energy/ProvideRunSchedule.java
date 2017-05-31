package de.unidue.stud.sehawagn.energy;

import java.io.IOException;

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
		System.out.println("Agent "+myAgent.getLocalName()+": REQUEST received from "+request.getSender().getName()+". Action is "+request.getContent());
			// We agree to perform the action. Note that in the FIPA-Request
			// protocol the AGREE message is optional. Return null if you
			// don't want to send it.
			System.out.println("Agent "+myAgent.getLocalName()+": Agree");
			ACLMessage agree = request.createReply();
			agree.setPerformative(ACLMessage.AGREE);
			return agree;
	}
	
	protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response)  {
			System.out.println("Agent "+myAgent.getLocalName()+": Action successfully performed");
			ACLMessage inform = request.createReply();
			inform.setPerformative(ACLMessage.INFORM);
			try {
				inform.setContentObject(request.getContentObject());
			} catch (IOException | UnreadableException e) {
				e.printStackTrace();
			}
			return inform;
	}
}