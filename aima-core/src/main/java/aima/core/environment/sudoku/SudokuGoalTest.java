package aima.core.environment.sudoku;

import aima.core.search.framework.GoalTest;

public class SudokuGoalTest implements GoalTest{
	
	private boolean printOn;
	
	public SudokuGoalTest(boolean printOn){
		this.printOn = printOn;		
	}

	@Override
	public boolean isGoalState(Object state) {
		SudokuBoard board = (SudokuBoard) state;
		
		if(printOn)	System.out.println(board);
		
		return board.isFilled();
	}
}
