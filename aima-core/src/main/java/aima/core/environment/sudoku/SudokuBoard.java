package aima.core.environment.sudoku;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

import aima.core.util.datastructure.XYLocation;

public class SudokuBoard {

		private int[] state;
		private int boardSize;
		private int cellSize;
		
		//
		// PUBLIC METHODS
		//
		
		public SudokuBoard(int[] state) {
			this.state = new int[state.length];
			System.arraycopy(state, 0, this.state, 0, state.length);
			this.boardSize = (int) Math.sqrt(state.length);
			this.cellSize = (int) Math.sqrt(boardSize);
		}

		public SudokuBoard(SudokuBoard copyBoard) {
			this(copyBoard.getState());
		}

		public int[] getState() {
			return state;
		}
		
		public int getBoardSize(){
			return boardSize;
		}
		
		public int getCellsize(){
			return cellSize;
		}
		
		public int getValueAt(XYLocation loc) {
			return getValueAt(loc.getXCoOrdinate(), loc.getYCoOrdinate());
		}
		
		public void addNumber(int val, XYLocation loc){
			this.setValue(loc.getXCoOrdinate(), loc.getYCoOrdinate(), val);
		}
		
		public boolean canAddNumber(int val, XYLocation loc){
			if(!this.isLocationEmpty(loc) || val < 1 || val > 9) return false;
			
			int indexLoc = getAbsPosition(loc.getXCoOrdinate(), loc.getYCoOrdinate());	
			for (int i=0; i < state.length; i++){
				if (state[i] == val && i != indexLoc){
					//Test line
					if (getYCoord(i) == loc.getYCoOrdinate()) return false;
					//Test column
					else if (getXCoord(i) == loc.getXCoOrdinate()) return false;
					//Test cell
					else if ((i%boardSize)/(int)cellSize == loc.getXCoOrdinate()/(int)cellSize
							&& (i/boardSize)/(int)cellSize == loc.getYCoOrdinate()/(int)cellSize) return false;
				}
			}
			return true;
		}
		
		public boolean isLocationEmpty(XYLocation loc){
			if (this.getValueAt(loc) == 0) return true;
			else return false;
		}
		
		public boolean isFilled(){
			if (this.getNumberOfEmptyCase() == 0) return true;
			else return false;
		}
		
		public int getNumberOfEmptyCase(){
			int counter = 0;
			for (int i=0; i < state.length; i++){
				if (state[i] == 0) counter++;
			}
			return counter;
		}
		
		public boolean isAllConstraintsSatisfied(){
			for (int row=0; row < boardSize; row++){
				for (int col=0; col < boardSize; col++){
					if (!isConstraintSatisfiedFor(new XYLocation(col, row))) return false;
				}
			}
			return true;
		}
		
		public boolean isConstraintSatisfiedFor(XYLocation loc){
			int indexLoc = getAbsPosition(loc.getXCoOrdinate(), loc.getYCoOrdinate());
			if (state[indexLoc] == 0) return false;
			
			for (int i=0; i < state.length; i++){
				if (state[i] == state[indexLoc] && i != indexLoc){
					// Vérification de la contrainte sur la ligne
					if (getYCoord(i) == loc.getYCoOrdinate()) return false;
					// Vérification de la contrainte sur la colonne
					else if (getXCoord(i) == loc.getXCoOrdinate()) return false;
					// Vérification de la contrainte dans la zone
					else if ((i%boardSize)/(int)cellSize == loc.getXCoOrdinate()/(int)cellSize
							&& (i/boardSize)/(int)cellSize == loc.getYCoOrdinate()/(int)cellSize) return false;
				}
			}
			return true;
		}
		
		@Override
		public String toString() {
			String retVal = "";
			for (int i = 0; i < state.length;i++){
				if ((i+1)%boardSize == 0) retVal += state[i] + "\n";
				else retVal += state[i] + " ";
				if(((i+1)%boardSize)%cellSize == 0 && (i+1)%boardSize != 0) retVal += " ";
				if ((i+1)%(cellSize*boardSize) == 0) retVal += "\n";
			}
			return retVal;
		}

		//
		// PRIVATE METHODS
		//

		/**
		 * Note: We have to clearly define how the graphic representation maps x and y.
		 * In the EightPuzzleBoard project it maps :
		 * - x values on row numbers (x-axis in vertical direction).
		 * - y values on column numbers (y-axis in horizontal direction)
		 * We, right now under here, seem to do the contrary. We'll have to check if the rest of our code is
		 * following this representation too, in case we use code from aima library not doing so.
		 * PS :
		 */
		private int getXCoord(int absPos) {
			return absPos%boardSize;
		}

		private int getYCoord(int absPos) {
			return (absPos/boardSize)%boardSize;
		}

		private int getAbsPosition(int x, int y) {
			return y*boardSize + x;
		}

		private int getValueAt(int x, int y) {
			// refactor this use either case or a div/mod soln
			return state[getAbsPosition(x, y)];
		}

		private void setValue(int x, int y, int val) {
			int absPos = getAbsPosition(x, y);
			state[absPos] = val;

		}
		
}
