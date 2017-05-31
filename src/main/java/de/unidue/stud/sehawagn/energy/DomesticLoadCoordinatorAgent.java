package de.unidue.stud.sehawagn.energy;

import jade.core.Agent;

public class DomesticLoadCoordinatorAgent extends Agent {

	public static String AGENT_ID= "DoLoCoAg";
	public static String CONVERSATION_ID_REQUEST_SCHEDULE="request-run-schedule";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4265865470470961177L;

	@Override
	protected void setup() {
		super.setup();
		Helper.enableForCommunication(this);
		addBehaviour(new ProvideRunSchedule(this, RequestRunSchedule.getMessageTemplate()));
	}

}
