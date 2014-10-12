package aima.core.environment.sudoku;

import aima.core.search.framework.HeuristicFunction;

	/**
	 * Estimates the distance to goal by the number of conflicts on
	 * the board.
	 * 
	 */

public class ConstraintsUnsatisfiedHeuristic implements HeuristicFunction {
	@Override
	public double h(Object state) {
		SudokuBoard board = (SudokuBoard) state;
		return board.getNumberOfHorizontalAndVerticalConflictsOnTheGrid();
	}
}
