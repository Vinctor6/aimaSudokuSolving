package aima.core.environment.sudoku;

import aima.core.agent.impl.DynamicAction;
import aima.core.util.datastructure.XYLocation;

public class SudokuAction extends DynamicAction {
	public static final String ADD_NUMBER = "placeNumberAt";

	public static final String ATTRIBUTE_NUMBER_LOC = "location";
	public static final String ATTRIBUTE_NUMBER_VAL = "value";

	public SudokuAction(String type, int val, XYLocation loc) {
		super(type);
		setAttribute(ATTRIBUTE_NUMBER_VAL, val);
		setAttribute(ATTRIBUTE_NUMBER_LOC, loc);
	}

	public int getValue() {
		return (int) getAttribute(ATTRIBUTE_NUMBER_VAL);
	}
	
	public XYLocation getLocation() {
		return (XYLocation) getAttribute(ATTRIBUTE_NUMBER_LOC);
	}

	public int getX() {
		return getLocation().getXCoOrdinate();
	}

	public int getY() {
		return getLocation().getYCoOrdinate();
	}
}
