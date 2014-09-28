package aima.core.environment.sudoku;

import aima.core.search.framework.GoalTest;

public class SudokuGoalTest implements GoalTest{

	@Override
	public boolean isGoalState(Object state) {
		SudokuBoard board = (SudokuBoard) state;
		/*return board.getEmptyCase() == 0
				&& board.isAllConstraintsSatisfied() == 0;*/
		return false;
	}

}
