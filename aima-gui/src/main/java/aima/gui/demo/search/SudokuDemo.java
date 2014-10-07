package aima.gui.demo.search;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import aima.core.agent.Action;
import aima.core.environment.sudoku.ConstraintsUnsatisifiedHeuristic;
import aima.core.environment.sudoku.SudokuBiggerConstraintHeuristicFunction;
import aima.core.environment.sudoku.SudokuBoard;
import aima.core.environment.sudoku.SudokuFunctionFactory;
import aima.core.environment.sudoku.SudokuGoalTest;
import aima.core.environment.sudoku.SudokuHCGoalTest;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.informed.GreedyBestFirstSearch;
import aima.core.search.local.HillClimbingSearch;
import aima.core.search.uninformed.DepthFirstNodesLimitedSearch;

public class SudokuDemo {
	private static ArrayList<SudokuBoard> sudokus = new ArrayList<SudokuBoard>();
	
	private static boolean printStates = false;
	private static int nodesLimit = 10000;

	public static void main(String[] args) {
		String filename = askForFilename();
		loadGrids(filename);
		
		System.out.println(sudokus.get(0));
		//sudokuDFSDemo(sudokus.get(0));
		sudokuBestFirstDemo(sudokus.get(0));
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
				SudokuBoard newBoard = new SudokuBoard(newGrid);
				newBoard.initializeFixedValues();
				sudokus.add(newBoard);
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
		System.out.println("\nSudokuDemo DFS -->");
		try {
			Problem problem = new Problem(initialBoard,
					SudokuFunctionFactory.getActionsFunction(),
					SudokuFunctionFactory.getResultFunction(),
					new SudokuGoalTest(printStates));
			Search search = new DepthFirstNodesLimitedSearch(nodesLimit);
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void sudokuHCDemo(SudokuBoard initialBoard) {
		System.out.println("\nSudokuDemo HillClimbing  -->");
		try {
			Problem problem = new Problem(new SudokuBoard(initialBoard),
					SudokuFunctionFactory.getIActionsFunction(),
					SudokuFunctionFactory.getIResultFunction(),
					new SudokuHCGoalTest(printStates));
			HillClimbingSearch search = new HillClimbingSearch(
					new ConstraintsUnsatisifiedHeuristic());
			SearchAgent agent = new SearchAgent(problem, search);

			System.out.println();
			printActions(agent.getActions());
			System.out.println("Search Outcome=" + search.getOutcome());
			System.out.println("Final State=\n" + search.getLastSearchState());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void sudokuBestFirstDemo(SudokuBoard initialBoard) {
		System.out.println("\nSudokuDemo BestFirst -->");
		try {
			Problem problem = new Problem(initialBoard,
					SudokuFunctionFactory.getActionsFunction(),
					SudokuFunctionFactory.getResultFunction(),
					new SudokuGoalTest(printStates));
			//On veut modifier l'algo GraphSearch pour inclure le compteur (action cutoff) + rejeter certains noeuds (if isStuck)
			Search search = new GreedyBestFirstSearch(new GraphSearch(), new SudokuBiggerConstraintHeuristicFunction());
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
