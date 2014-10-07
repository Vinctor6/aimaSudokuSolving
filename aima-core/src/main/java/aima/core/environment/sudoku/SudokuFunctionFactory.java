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

	private static class SudokuActionsFunction implements ActionsFunction {
		public Set<Action> actions(Object state) {
			SudokuBoard board = (SudokuBoard) state;

			Set<Action> actions = new LinkedHashSet<Action>();
			
			int boardSize = board.getBoardSize();
			for (int i = 0; i < boardSize; i++)
			for (int j = 0; j < boardSize; j++){
				XYLocation loc = new XYLocation(i, j);
				if (board.isLocationEmpty(loc)){
					int nbrOfValAdded = 0;
					int onlyVal = 0;
					for (int val = 1; val <= boardSize ; val++)
						if (board.canAddNumber(val,loc)){
							nbrOfValAdded++;
							onlyVal = val;
							actions.add(new SudokuAction(SudokuAction.ADD_NUMBER, val, loc));
						}
					if(nbrOfValAdded == 1)
					{
						actions.clear();
						actions.add(new SudokuAction(SudokuAction.ADD_NUMBER, onlyVal, loc));
						return actions;
					}
					else if(nbrOfValAdded == 0)
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
}
