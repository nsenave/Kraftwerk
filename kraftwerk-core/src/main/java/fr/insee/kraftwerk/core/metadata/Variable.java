package fr.insee.kraftwerk.core.metadata;

import lombok.Getter;
import lombok.Setter;

/**
 * Object class to represent a variable.
 *
 */
public class Variable {

	/** Variable name. */
	@Getter
	protected String name;

	/** Group reference */
	@Getter
	protected Group group;

	/** Variable type from the enum class (STRING, INTEGER, DATE, ...) */
	@Getter
	protected VariableType type;

	/** Variable length expected. */
	@Getter
	protected String length;

	/** Maximum length received in input for the variable. */
	@Getter
	@Setter
	protected int maxLengthData;

	/** Name of the item used to collect the answer. */
	@Getter
	@Setter
	protected String questionItemName;

	/** Identifies if the variable is part a question grid */
	@Getter
	@Setter
	protected boolean isInQuestionGrid;

	public Variable(String name, Group group, VariableType type) {
		this.name = name;
		this.group = group;
		this.type = type;
	}

	public Variable(String name, Group group, VariableType type, String length) {
		this.name = name;
		this.group = group;
		this.type = type;
		this.length = length;
	}

	public String getGroupName() {
		return group.getName();
	}


}
