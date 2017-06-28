package de.unidue.stud.sehawagn.energy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import energy.optionModel.Duration;
import energy.optionModel.FixedBoolean;
import energy.optionModel.State;
import energy.optionModel.TechnicalInterfaceConfiguration;
import energy.optionModel.TechnicalSystem;
import energy.optionModel.TimeUnit;
import energy.optionModel.VariableState;
import energy.persistence.SerialClone;

public class Flexibility extends TechnicalSystem {

	private static final long serialVersionUID = 5424673360091422609L;
	private TechnicalInterfaceConfiguration activeInterface;
	private List<State> states;

	public Flexibility(TechnicalSystem originalSystem) {
		super();
		TechnicalSystem clonedSystem = SerialClone.clone(originalSystem);
		this.interfaceConfigurations = clonedSystem.getInterfaceConfigurations();
		this.systemID = clonedSystem.getSystemID();
	}

	public static TechnicalInterfaceConfiguration findInterface(List<TechnicalInterfaceConfiguration> interfaceConfigurations, int interfaceID) {
		if (interfaceID < interfaceConfigurations.size()) {
			return interfaceConfigurations.get(interfaceID);
		}
		return null;
	}

	public Flexibility selectInterface(int interfaceID) {
		if (interfaceID < interfaceConfigurations.size()) {
			activeInterface = findInterface(interfaceConfigurations, interfaceID);
			if (activeInterface == null) {
				interfaceConfigurations = null;
			}
		}
		return this;
	}

	public Flexibility pruneForControllableStates(String variableControllable, String variableOn) {
		if (activeInterface != null) {
			states = activeInterface.getSystemStates();
			List<State> removeStates = new ArrayList<State>();

			// iterate over all states
			for (State state : states) {
				Boolean controllable = false;
				Boolean switchable = false;

				List<VariableState> allVariables = state.getStateEvaluationRanges();
				for (VariableState curVariable : allVariables) {
					if (!controllable && checkVariable(curVariable, variableControllable, false)) {
						controllable = true;
					}
					if (!switchable && checkVariable(curVariable, variableOn, true)) {
						switchable = true;
					}
				}
				if (!controllable || !switchable) {
					// this state is controllable by the system
					removeStates.add(state);
				}
			}
			states.removeAll(removeStates);
		}
		return this;
	}

	public Boolean checkVariable(VariableState curVariable, String variableName, Boolean invert) {
		if (curVariable.getVariableID().equals(variableName)) {
			if (curVariable instanceof FixedBoolean) {
				FixedBoolean variable = (FixedBoolean) curVariable;
				return variable.isValue() != invert;
			}
		}
		return false;
	}

	public Flexibility setStaticPhasePauses(Integer pauseSeconds) {
		// iterate over all (remaining) states
		for (State state : states) {
//			System.out.println(state.getStateID() + " duration before: " + state.getDuration().getValue() + state.getDuration().getUnit());

			// set new wait/pause time
			state.getDuration().setValue(pauseSeconds);
			state.getDuration().setUnit(TimeUnit.SECOND_S);

//			System.out.println(state.getStateID() + " duration after: " + state.getDuration().getValue() + state.getDuration().getUnit());

			/*
			List<StateTransition> transitions = state.getStateTransitions();
			// iterate over all transitions
			for (StateTransition stateTransition : transitions) {
				stateTransition.
				
				//don't make it overly complicated via
				//setMaxDuration()
				//setMinDuration()
			}
			*/
		}
		// .getStateTransitions().get(0).getMaxDuration();
		return this;

	}

	public static HashMap<String, Duration> deriveDurations(List<State> allStates) {
		HashMap<String, Duration> allDurations = new HashMap<String, Duration>();
		for (State state : allStates) {
			allDurations.put(state.getStateID(), state.getDuration());
		}
		return allDurations;
	}

	public void applyTo(TechnicalSystem originalSystem, int interfaceID) {
		List<State> originalStates = findInterface(originalSystem.getInterfaceConfigurations(), interfaceID).getSystemStates();
		List<State> processedStates = findInterface(interfaceConfigurations, 0).getSystemStates();
		HashMap<String, Duration> allDurations = deriveDurations(processedStates);
		for (State state : originalStates) {
			Duration foundDuration = allDurations.get(state.getStateID());
			if (foundDuration != null) {
				state.setDuration(foundDuration);
			}
		}
	}
}
