package aima.core.environment.sudoku;

import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.ResultFunction;

public class SudokuFunctionFactory {
	private static ActionsFunction _actionsFunction = null;
	private static ResultFunction _resultFunction = null;

	public static ActionsFunction getActionsFunction() {
		if (null == _actionsFunction) {
			_actionsFunction = new EPActionsFunction();
		}
		return _actionsFunction;
	}

	public static ResultFunction getResultFunction() {
		if (null == _resultFunction) {
			_resultFunction = new EPResultFunction();
		}
		return _resultFunction;
	}

	private static class EPActionsFunction implements ActionsFunction {
		public Set<Action> actions(Object state) {
			SudokuBoard board = (SudokuBoard) state;

			Set<Action> actions = new LinkedHashSet<Action>();
/*
			if (board.canAddNumber()) {
				actions.add();
			}
*/
			return actions;
		}
	}

	private static class EPResultFunction implements ResultFunction {
		public Object result(Object s, Action a) {
			SudokuBoard board = (SudokuBoard) s;

			if (SudokuBoard.UP.equals(a)
					&& board.canMoveGap(SudokuBoard.UP)) {
				SudokuBoard newBoard = new SudokuBoard(board);
				newBoard.moveGapUp();
				return newBoard;
			} else if (SudokuBoard.DOWN.equals(a)
					&& board.canMoveGap(SudokuBoard.DOWN)) {
				SudokuBoard newBoard = new SudokuBoard(board);
				newBoard.moveGapDown();
				return newBoard;
			} else if (SudokuBoard.LEFT.equals(a)
					&& board.canMoveGap(SudokuBoard.LEFT)) {
				SudokuBoard newBoard = new SudokuBoard(board);
				newBoard.moveGapLeft();
				return newBoard;
			} else if (SudokuBoard.RIGHT.equals(a)
					&& board.canMoveGap(SudokuBoard.RIGHT)) {
				SudokuBoard newBoard = new SudokuBoard(board);
				newBoard.moveGapRight();
				return newBoard;
			}

			// The Action is not understood or is a NoOp
			// the result will be the current state.
			return s;
		}
	}
}
