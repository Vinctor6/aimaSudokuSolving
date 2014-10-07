package aima.core.environment.sudoku;

import java.util.Arrays;
import java.util.Random;
import java.lang.Math;

import aima.core.util.datastructure.XYLocation;

public class SudokuBoard {

		private int[] state;
		private int boardSize;
		private int cellSize;
		private XYLocation locFilled;
		private boolean[] fixedValues;
		public boolean isStuck = false;
		
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
			this.setFixedValues(copyBoard.getFixedValues());
		}

		public int[] getState() {
			return state;
		}
		
		public int getBoardSize(){
			return boardSize;
		}
		
		public int getCellSize(){
			return cellSize;
		}
		
		public XYLocation getLocFilled(){
			return locFilled;
		}
		
		public boolean[] getFixedValues() {
			return fixedValues;
		}
		
		public void setFixedValues(boolean[] fixedValues) {
			this.fixedValues = fixedValues;
		}

		public void initializeFixedValues() {
			this.fixedValues = new boolean[this.state.length];
			for(int i=0; i < this.state.length; i++){
				this.fixedValues[i] = (this.state[i] != 0)?true:false;
			}
		}
		
		public boolean isFixedValue(XYLocation loc) {
			return fixedValues[this.getAbsPosition(loc.getXCoOrdinate(), loc.getYCoOrdinate())];
		}

		public int getValueAt(XYLocation loc) {
			return getValueAt(loc.getXCoOrdinate(), loc.getYCoOrdinate());
		}
		
		public void addNumber(int val, XYLocation loc){
			this.locFilled = loc;
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
		
		public int getNumberOfHorizontalAndVerticalConflictsOnTheGrid(){
			int count = 0;
			for (int i=0; i < boardSize; i++)
				for (int j=0; j < boardSize; j++)
					count += getNumberOfHorizontalAndVerticalConflictsAt(i, j);
			return count / 2;
		}
		
		public boolean isSquareEmpty(XYLocation loc){
			return (getValueAt(loc) == 0)?true:false;
		}
		
		public boolean isSquareEmpty(int x, int y){
			return (getValueAt(x, y) == 0)?true:false;
		}
		
		public void fillWithRandomValues(){
			int leftCornerZone=0;
			while(leftCornerZone < boardSize*boardSize){
				boolean[] availableList = new boolean[boardSize];
				for (int i=0; i < boardSize; i++) availableList[i] = true;
				
				for (int i=getXCoord(leftCornerZone); i < getXCoord(leftCornerZone)+cellSize; i++)
				for (int j=getYCoord(leftCornerZone); j < getYCoord(leftCornerZone)+cellSize;j++){
					if (!isSquareEmpty(i, j)) availableList[getValueAt(i, j)-1] = false;
				}
				
				for (int i=getXCoord(leftCornerZone); i < getXCoord(leftCornerZone)+cellSize; i++)
				for (int j=getYCoord(leftCornerZone); j < getYCoord(leftCornerZone)+cellSize;j++)
					if (isSquareEmpty(i, j)){
						int randValue = getSudokuRandomNumber(availableList);
						this.setValue(i, j, randValue);
						availableList[randValue-1] = false;
					}
				if (leftCornerZone%boardSize == 2*cellSize) leftCornerZone += cellSize+(cellSize-1)*boardSize;
				else leftCornerZone += cellSize;
			}
		}
		
		public void permuteValues(XYLocation loc1, XYLocation loc2) throws Exception{
			if(isFixedValue(loc1) || isFixedValue(loc2)) throw new Exception("Trying to permute fixed values");
			if (loc1 != loc2){
				int tmp = getValueAt(loc1);
				setValue(loc1.getXCoOrdinate(), loc1.getYCoOrdinate(), getValueAt(loc2));
				setValue(loc2.getXCoOrdinate(), loc2.getYCoOrdinate(), tmp);
			}
		}
		
		@Override
		public String toString() {
			String retVal = "";                       
            for (int i = 0; i < state.length;i++){
            	if(((i+1)%boardSize)%cellSize == 0 && (i+1)%boardSize != 0) retVal += state[i] + " | ";
            	else retVal += state[i] + " ";
            	if ((i+1)%boardSize == 0) retVal += "\n";
            	if ((i+1)%(cellSize*boardSize) == 0){
            		if((i+1) != state.length)
            			for (int j = 0; j < this.boardSize*2-1+(this.cellSize-1)*2;j++) retVal += "-";
            		retVal += "\n";
            	}
            }
			return retVal;
		}

		@Override
		public boolean equals(Object o) {

			if (this == o) {
				return true;
			}
			if ((o == null) || (this.getClass() != o.getClass())) {
				return false;
			}
			SudokuBoard aBoard = (SudokuBoard) o;

			for(int i = 0; i < this.getBoardSize(); i++ )
				for(int j = 0; j < this.getBoardSize(); j++){
					if (this.getValueAt(i,j) != aBoard.getValueAt(new XYLocation(i,j))) return false;
				}
			return true;
		}
		
		@Override
		public int hashCode() {
			return Arrays.hashCode(state);
			//return this.toString().hashCode();
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

		private int getSudokuRandomNumber(boolean[] availableList){
		    Random rand = new Random();
		    int randomNum;
		    do{
		    	randomNum = rand.nextInt(availableList.length);
		    } while(availableList[randomNum] != true);

		    return randomNum+1;
		}
		
		private void setValue(int x, int y, int val) {
			int absPos = getAbsPosition(x, y);
			state[absPos] = val;
		}
		
		public int getNumberOfHorizontalAndVerticalConflictsAt(int x, int y){
			return getNumberOfHorizontalConflictsAt(x, y)
					+ getNumberOfVerticalConflictsAt(x, y);
		}	
		
		private int getNumberOfHorizontalConflictsAt(int x, int y){
			int conflicts = 0, val = getValueAt(x, y);
			for (int i=y*boardSize; i < y*boardSize+boardSize; i++)
				if (state[i] == val && i != getAbsPosition(x, y)) conflicts++;
			return conflicts;
		}
		
		private int getNumberOfVerticalConflictsAt(int x, int y){
			int conflicts = 0, val = getValueAt(x, y);
			for (int i=x; i < boardSize*boardSize; i+=boardSize)
				if (state[i] == val && i != getAbsPosition(x, y)) conflicts++;
			return conflicts;
		}
		
}
