package de.unidue.stud.sehawagn.energy;

import java.io.IOException;
import java.io.Serializable;

import hygrid.ontology.HyGridOntology;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class Helper {

	private static final Ontology STANDARD_ONTOLOGY = HyGridOntology.getInstance();
//	private static final Codec STANDARD_CODEC=new XMLCodec();
//	private static final Codec STANDARD_CODEC=new LEAPCodec();
	private static final Codec STANDARD_CODEC = new SLCodec();

	public static void fillMessage(ACLMessage msg, Serializable contentObject, Agent a) {
		msg.setLanguage(STANDARD_CODEC.getName());
		msg.setOntology(STANDARD_ONTOLOGY.getName());
		try {
			msg.setContentObject(contentObject);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void enableForCommunication(Agent toBeEnabled){
		toBeEnabled.getContentManager().registerLanguage(STANDARD_CODEC);
		toBeEnabled.getContentManager().registerOntology(STANDARD_ONTOLOGY);
	}
}
