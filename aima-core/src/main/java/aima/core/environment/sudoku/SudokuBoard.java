package aima.core.environment.sudoku;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicAction;
import aima.core.environment.eightpuzzle.EightPuzzleBoard;
import aima.core.util.datastructure.XYLocation;

public class SudokuBoard {

		public static Action FILL = new DynamicAction("Fill"); //not precise enough, how should we do?
		public static Action ERASE = new DynamicAction("Erase"); //do we need that?
		private int[] state;
		private int tabSize;
		private int cellSize;
		
		//
		// PUBLIC METHODS
		//
		
		public SudokuBoard(int[] state) {
			this.state = new int[state.length];
			System.arraycopy(state, 0, this.state, 0, state.length);
			this.tabSize = (int) Math.sqrt(state.length);
			this.cellSize = (int) Math.sqrt(tabSize);
		}

		public SudokuBoard(SudokuBoard copyBoard) {
			this(copyBoard.getState());
		}

		public int[] getState() {
			return state;
		}
		
		public int getValueAt(XYLocation loc) {
			return getValueAt(loc.getXCoOrdinate(), loc.getYCoOrdinate());
		}
		
		public void AddNumber(int val, XYLocation loc){
			this.setValue(loc.getXCoOrdinate(), loc.getYCoOrdinate(), val);
		}
		
		public boolean canAddNumber(int val, XYLocation loc){
			if(val < 1 || val > 9) return false;
			
			int indexLoc = getAbsPosition(loc.getXCoOrdinate(), loc.getYCoOrdinate());	
			for (int i=0; i < state.length; i++){
				if (state[i] == val && i != indexLoc){
					//Test line
					if (getYCoord(i) == loc.getYCoOrdinate()) return false;
					//Test column
					else if (getXCoord(i) == loc.getXCoOrdinate()) return false;
					//Test cell
					else if ((i%tabSize)/(int)cellSize == loc.getXCoOrdinate()/(int)cellSize
							&& (i/tabSize)/(int)cellSize == loc.getYCoOrdinate()/(int)cellSize) return false;
				}
			}
			return true;
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
			for (int row=0; row < tabSize; row++){
				for (int col=0; col < tabSize; col++){
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
					else if ((i%tabSize)/(int)cellSize == loc.getXCoOrdinate()/(int)cellSize
							&& (i/tabSize)/(int)cellSize == loc.getYCoOrdinate()/(int)cellSize) return false;
				}
			}
			return true;
		}
		
		@Override
		public String toString() {
			String retVal = "";
			for (int i = 0; i < state.length;i++){
				if ((i+1)%tabSize == 0) retVal += state[i] + "\n";
				else retVal += state[i] + " ";
				if(((i+1)%tabSize)%cellSize == 0 && (i+1)%tabSize != 0) retVal += " ";
				if ((i+1)%(cellSize*tabSize) == 0) retVal += "\n";
			}
			return retVal;
		}

		//
		// PRIVATE METHODS
		//

		/**
		 * Note: The graphic representation maps x values on row numbers (x-axis in
		 * vertical direction).
		 */
		private int getXCoord(int absPos) {
			return absPos%tabSize;
		}

		/**
		 * Note: The graphic representation maps y values on column numbers (y-axis
		 * in horizontal direction).
		 */
		private int getYCoord(int absPos) {
			return (absPos/tabSize)%tabSize;
		}

		private int getAbsPosition(int x, int y) {
			return y*tabSize + x;
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
