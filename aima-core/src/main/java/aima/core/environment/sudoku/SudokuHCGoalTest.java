package aima.core.environment.sudoku;

import aima.core.search.framework.GoalTest;

public class SudokuHCGoalTest implements GoalTest{

		private boolean printOn;
		
		public SudokuHCGoalTest(boolean printOn){
			this.printOn = printOn;		
		}

		@Override
		public boolean isGoalState(Object state) {
			SudokuBoard board = (SudokuBoard) state;
			
			if(printOn)	System.out.println(board);
			
			return (board.getNumberOfHorizontalAndVerticalConflictsOnTheGrid() == 0);
		}
}
