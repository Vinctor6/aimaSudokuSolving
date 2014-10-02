package aima.gui.demo.search;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import aima.core.agent.Action;
import aima.core.environment.sudoku.SudokuBoard;
import aima.core.environment.sudoku.SudokuFunctionFactory;
import aima.core.environment.sudoku.SudokuGoalTest;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.informed.AStarSearch;
import aima.core.search.informed.GreedyBestFirstSearch;
import aima.core.search.uninformed.DepthLimitedSearch;

public class SudokuDemo {
	private static ArrayList<SudokuBoard> sudokus = new ArrayList<SudokuBoard>();

	public static void main(String[] args) {
		String filename = askForFilename();
		loadGrids(filename);
		
/*		for (SudokuBoard sudoku : sudokus){
			System.out.println(sudoku + "\n\n\n");
		}
*/
		System.out.println(sudokus.get(0));
		sudokuDFSDemo(sudokus.get(0));
		/*sudokuHCDemo();
		sudokuGreedyBestFirstDemo();*/
	}
	
	 /** 
     * Load a sudoku grid from a file 
     *  
     * @param the filename of the file from which to read the grids configurations
 	 *
     */  
	public static void loadGrids(String filename){
		 try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			while (in.ready()){
				int[] newGrid = toIntArray(in.readLine());
				sudokus.add(new SudokuBoard(newGrid));
			}
			in.close();
		} catch (IOException e) {
			System.out.println("Erreur lors de la lecture du fichier de configuration.");
			e.printStackTrace();
		}
	}
	
	 /** 
     * Read the name of the file containing the grids to be loaded
     *  
     * @return the filename 
     */  
	private static String askForFilename(){
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		String line = "";
		System.out.println("Enter the name of the file containing the grids (default: 100sudoku.txt): ");
		try{
			line = keyboard.readLine();
			if (line.equals("")) line = "100sudoku.txt";
		}catch(IOException e){
			System.out.println("Erreur lors de la lecture du nom de fichier.");
			e.printStackTrace();
		}
		return line;
	}
	
	 /** 
     * Converts an integer (transtyped in String for more facility) to an array of integer
     *  
     * @param nbr the "String integer" to be converted
     *  
     * @return an array of integer 
     */  
    private static int[] toIntArray(String nbr) {
        int[] intArray = new int[nbr.length()];  
        for (int i = 0; i < intArray.length; i++) {  
            intArray[i] = Integer.parseInt(nbr.substring(i, i+1)); 
        }  
        return intArray;  
    }  
    
	public static ArrayList<SudokuBoard> getSudokus() {
		return sudokus;
	}

	public static void setSudokus(ArrayList<SudokuBoard> sudokus) {
		SudokuDemo.sudokus = sudokus;
	}
	
	private static void sudokuDFSDemo(SudokuBoard initialBoard) {
		System.out.println("\nSudokuDemo DFS (9) -->");
		try {
			Problem problem = new Problem(initialBoard,
					SudokuFunctionFactory.getActionsFunction(),
					SudokuFunctionFactory.getResultFunction(),
					new SudokuGoalTest());
			Search search = new DepthLimitedSearch(9);
			System.out.println("test1"); //works before
			SearchAgent agent = new SearchAgent(problem, search);
			System.out.println("test2"); //doesn't work after
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void sudokuHCDemo() {
	}

	private static void sudokuGreedyBestFirstDemo() {
	}


	private static void printInstrumentation(Properties properties) {
		Iterator<Object> keys = properties.keySet().iterator();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String property = properties.getProperty(key);
			System.out.println(key + " : " + property);
		}
	}

	private static void printActions(List<Action> actions) {
		for (int i = 0; i < actions.size(); i++) {
			String action = actions.get(i).toString();
			System.out.println(action);
		}
	}
}
