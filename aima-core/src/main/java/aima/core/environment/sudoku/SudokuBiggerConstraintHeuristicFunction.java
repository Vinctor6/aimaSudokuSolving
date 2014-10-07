package aima.core.environment.sudoku;

import aima.core.search.framework.HeuristicFunction;
import aima.core.util.datastructure.XYLocation;

/**
 * For the location just filled
 * 
 */
public class SudokuBiggerConstraintHeuristicFunction implements HeuristicFunction {

	private int functionNbr;
	public SudokuBiggerConstraintHeuristicFunction(int functionNbr)
	{
		this.functionNbr = functionNbr;
	}
	
	public double h(Object state) {
		SudokuBoard board = (SudokuBoard) state;
		int boardSize = board.getBoardSize();
		int retVal = 0;
		// functionNbr = 1 : check number of possible values for the filled location + number of empty left
		if (this.functionNbr == 1){
			for (int i = 1; i <= boardSize; i++) {
				if(board.canAddNumber(i, board.getLocFilled())) retVal ++;
			}
			retVal += board.getNumberOfEmptyCase();
		}
		// functionNbr = 2 : sum of potential actions left in every empty location
		else if (this.functionNbr == 2){
			for(int i = 0; i < boardSize; i++ ){
				for(int j = 0; j < boardSize; j++){
					XYLocation loc = new XYLocation(i, j);
					if (board.isLocationEmpty(loc)){
						boolean canAddOneNumber = false;
						for (int val = 1; val <= boardSize; val++) {
							canAddOneNumber = true;
							if(board.canAddNumber(val, loc)) retVal ++;
						}
						if(!canAddOneNumber) return boardSize*boardSize*boardSize;
					}
				}
			}
		}
		return retVal;
	}
}