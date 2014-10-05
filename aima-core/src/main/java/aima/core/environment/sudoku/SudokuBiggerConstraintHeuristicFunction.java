package aima.core.environment.sudoku;

import aima.core.search.framework.HeuristicFunction;

/**
 * For the location just filled
 * 
 */
public class SudokuBiggerConstraintHeuristicFunction implements HeuristicFunction {

	public double h(Object state) {
		SudokuBoard board = (SudokuBoard) state;
		int retVal = 0;
		for (int i = 1; i < board.getBoardSize(); i++) {
			if(board.canAddNumber(i, board.getLocFilled()))
				retVal ++;
		}
		return retVal;
	}
}