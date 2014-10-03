package aima.core.environment.sudoku;

import aima.core.search.framework.GoalTest;

public class SudokuGoalTest implements GoalTest{

	@Override
	public boolean isGoalState(Object state) {
		SudokuBoard board = (SudokuBoard) state;
		
		System.out.println(board);
		
		if (board.isFilled()){
			return board.isAllConstraintsSatisfied();
		}
		else return false;
	}
}
