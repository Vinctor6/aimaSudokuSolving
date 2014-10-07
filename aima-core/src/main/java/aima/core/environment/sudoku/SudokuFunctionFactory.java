package aima.core.environment.sudoku;

import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.ResultFunction;
import aima.core.util.datastructure.XYLocation;

public class SudokuFunctionFactory {
	private static ActionsFunction _actionsFunction = null;
	private static ResultFunction _resultFunction = null;
	private static ActionsFunction _iActionsFunction = null;
	private static ResultFunction _iResultFunction = null;
	
	public static ActionsFunction getActionsFunction() {
		if (null == _actionsFunction) {
			_actionsFunction = new SudokuActionsFunction();
		}
		return _actionsFunction;
	}

	public static ResultFunction getResultFunction() {
		if (null == _resultFunction) {
			_resultFunction = new SudokuResultFunction();
		}
		return _resultFunction;
	}
	
	/**
	 * Returns an ACTIONS function for the incremental formulation of the
	 * sudoku problem.
	 */
	public static ActionsFunction getIActionsFunction() {
		if (null == _iActionsFunction) {
			_iActionsFunction = new SudokuIActionsFunction();
		}
		return _iActionsFunction;
	}
	
	public static ResultFunction getIResultFunction() {
		if (null == _iResultFunction) {
			_iResultFunction = new SudokuIResultFunction();
		}
		return _iResultFunction;
	}

	private static class SudokuActionsFunction implements ActionsFunction {
		public Set<Action> actions(Object state) {
			SudokuBoard board = (SudokuBoard) state;

			Set<Action> actions = new LinkedHashSet<Action>();
			
			int boardSize = board.getBoardSize();
			for (int i = 0; i < boardSize; i++)
			for (int j = 0; j < boardSize; j++){
				XYLocation loc = new XYLocation(i, j);
				if (board.isLocationEmpty(loc)){
					boolean canAddOneNumber = false;
					for (int val = 1; val <= boardSize ; val++)
						if (board.canAddNumber(val,loc)){
							canAddOneNumber = true;
							actions.add(new SudokuAction(SudokuAction.ADD_NUMBER, val, loc));
						}
					if(!canAddOneNumber)
					{
						board.isStuck = true;
						/* If we can't fill one location of the board,
						 * this means the given board will never be able to be completed.
						 * We can stop here and return a set of empty actions.						
						*/
						actions.clear();
						return actions;
					}
				}
			}
			return actions;
		}
	}
	
	/**
	 * Assumes that all the board is filled with random values
	 * and provides numbers permutations actions for all positions
	 */
	private static class SudokuIActionsFunction implements ActionsFunction {
		public Set<Action> actions(Object state) {
			SudokuBoard board = (SudokuBoard) state;

			Set<Action> actions = new LinkedHashSet<Action>();

			int leftCornerZone = 0, boardSize = board.getBoardSize(), cellSize = board.getCellSize();
			
			while(leftCornerZone < boardSize*boardSize){
				for (int i=leftCornerZone%boardSize; i < leftCornerZone%boardSize+cellSize; i++) // Parcourt les x de la zone
					for (int j=(leftCornerZone/boardSize)%boardSize; j < (leftCornerZone/boardSize)%boardSize+cellSize;j++){ // Parcourt les y de la zone
						XYLocation loc1 = new XYLocation(i,j);

						if (!board.isFixedValue(loc1)){
							// Reparcourt de la zone pour chercher les permutations
							for (int k1=leftCornerZone%boardSize; k1 < leftCornerZone%boardSize+cellSize; k1++)
								for (int k2=(leftCornerZone/boardSize)%boardSize; k2 < (leftCornerZone/boardSize)%boardSize+cellSize;k2++){
									XYLocation loc2 = new XYLocation(k1,k2);
									if (!loc2.equals(loc1) && !board.isFixedValue(loc2)){
										actions.add(new SudokuHCAction(SudokuHCAction.PERMUTE_NUMBER, loc1, board.getValueAt(loc1), loc2, board.getValueAt(loc2)));
									}
								}
						}
					}
				if (leftCornerZone%boardSize == 2*cellSize) leftCornerZone += cellSize+(cellSize-1)*boardSize;
				else leftCornerZone += cellSize;
			}
			return actions;
		}
	}
	
	private static class SudokuResultFunction implements ResultFunction {
		public Object result(Object s, Action a) {
			if (a instanceof SudokuAction) {
				SudokuAction qa = (SudokuAction) a;
				SudokuBoard board = (SudokuBoard) s;
				SudokuBoard newBoard = new SudokuBoard(board);
				if (qa.getName() == SudokuAction.ADD_NUMBER)
					newBoard.addNumber(qa.getValue(), qa.getLocation());
				s = newBoard;
			}
			// The Action is not understood or is a NoOp
			// the result will be the current state.
			return s;
		}
	}
	
	private static class SudokuIResultFunction implements ResultFunction {
		public Object result(Object s, Action a) {
			if (a instanceof SudokuHCAction) {
				SudokuHCAction qa = (SudokuHCAction) a;
				SudokuBoard board = (SudokuBoard) s;
				SudokuBoard newBoard = new SudokuBoard(board);
				if (qa.getName() == SudokuHCAction.PERMUTE_NUMBER)
					try {
						newBoard.permuteValues(qa.getFirstLocation(), qa.getSecondLocation());
					} catch (Exception e) {
						System.err.println(e.getMessage());
						e.printStackTrace();
					}
				s = newBoard;
			}
			// The Action is not understood or is a NoOp
			// the result will be the current state.
			return s;
		}
	}
}
