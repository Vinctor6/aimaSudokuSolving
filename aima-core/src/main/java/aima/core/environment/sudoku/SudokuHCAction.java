package aima.core.environment.sudoku;

import aima.core.agent.impl.DynamicAction;
import aima.core.util.datastructure.XYLocation;

public class SudokuHCAction extends DynamicAction{
	public static final String PERMUTE_NUMBER = "permuteNumbers";

	public static final String ATTRIBUTE_NUMBER_LOC1 = "location1";
	public static final String ATTRIBUTE_NUMBER_LOC2 = "location2";

	public SudokuHCAction(String type, XYLocation loc1, XYLocation loc2) {
		super(type);
		setAttribute(ATTRIBUTE_NUMBER_LOC1, loc1);
		setAttribute(ATTRIBUTE_NUMBER_LOC2, loc2);
	}
	public XYLocation getFirstLocation() {
		return (XYLocation) getAttribute(ATTRIBUTE_NUMBER_LOC1);
	}
	
	public XYLocation getSecondLocation() {
		return (XYLocation) getAttribute(ATTRIBUTE_NUMBER_LOC2);
	}

	public int getFirstX() {
		return getFirstLocation().getXCoOrdinate();
	}

	public int getFirstY() {
		return getFirstLocation().getYCoOrdinate();
	}
	
	public int getSecondX() {
		return getSecondLocation().getXCoOrdinate();
	}

	public int getSecondY() {
		return getSecondLocation().getYCoOrdinate();
	}	
}
