package aima.core.environment.sudoku;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicAction;
import aima.core.environment.eightpuzzle.EightPuzzleBoard;
import aima.core.util.datastructure.XYLocation;

public class SudokuBoard {

		public static Action FILL = new DynamicAction("Fill");
		public static Action ERASE = new DynamicAction("Erase");
		private int[] state;
		private int size;
		
		public SudokuBoard(int[] state) {
			this.state = state;
			this.size = (int) Math.sqrt(state.length);
		}
	
		public int getNumberOfEmptyCase(){
			int counter = 0;
			for (int i=0; i < state.length; i++){
				if (state[i] == 0) counter++;
			}
			return counter;
		}
		
		public int getValueAt(XYLocation loc) {
			return getValueAt(loc.getXCoOrdinate(), loc.getYCoOrdinate());
		}
		
		public boolean isAllConstraintsSatisfied(){
			for (int row=0; row < size; row++){
				for (int col=0; col < size; col++){
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
					if (i >= (loc.getYCoOrdinate())*size && i < loc.getYCoOrdinate()*size+size) return false;
					// Vérification de la contrainte sur la colonne
					else if (i%size == loc.getXCoOrdinate()%size) return false;
					// Vérification de la contrainte dans la zone
					else if ((i%size)/(int)Math.sqrt(size) == loc.getXCoOrdinate()/(int)Math.sqrt(size)
							&& (i/size)/(int)Math.sqrt(size) == loc.getYCoOrdinate()/(int)Math.sqrt(size)) return false;
				}
			}
			return true;
		}
		
		@Override
		public String toString() {
			String retVal = "";
			for (int i = 0; i < state.length;i++){
				if ((i+1)%size == 0) retVal += state[i] + "\n";
				else retVal += state[i] + " ";
				if(((i+1)%size)%Math.sqrt(size) == 0 && (i+1)%size != 0) retVal += " ";
				if ((i+1)%(Math.sqrt(size)*size) == 0) retVal += "\n";
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
			return absPos / 3;
		}

		/**
		 * Note: The graphic representation maps y values on column numbers (y-axis
		 * in horizontal direction).
		 */
		private int getYCoord(int absPos) {
			return absPos % 3;
		}

		private int getAbsPosition(int x, int y) {
			return y*size + x;
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
