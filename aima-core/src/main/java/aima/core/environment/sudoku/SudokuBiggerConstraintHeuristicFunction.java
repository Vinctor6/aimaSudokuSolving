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
			if(retVal == 1) return 0.0;
			if(retVal == 0) return boardSize*boardSize*boardSize;
			retVal += board.getNumberOfEmptyCase();
		}
		// functionNbr = 2 : sum of potential actions left in every empty location
		else if (this.functionNbr == 2){
			for(int i = 0; i < boardSize; i++ ){
				for(int j = 0; j < boardSize; j++){
					XYLocation loc = new XYLocation(i, j);
					if (board.isLocationEmpty(loc)){
						int nbrOfValAdded = 0;
						for (int val = 1; val <= boardSize; val++) {
							nbrOfValAdded++;
							if(board.canAddNumber(val, loc)) retVal ++;
						}
						if(nbrOfValAdded == 1) return 0.0;
						if(nbrOfValAdded == 0) return boardSize*boardSize*boardSize;
					}
				}
			}
		}
		return retVal;
	}
}