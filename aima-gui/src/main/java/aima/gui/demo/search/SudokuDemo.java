package aima.gui.demo.search;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.sun.corba.se.spi.protocol.InitialServerRequestDispatcher;

import aima.core.agent.Action;
import aima.core.environment.sudoku.ConstraintsUnsatisfiedHeuristic;
import aima.core.environment.sudoku.SudokuBiggerConstraintHeuristicFunction;
import aima.core.environment.sudoku.SudokuBoard;
import aima.core.environment.sudoku.SudokuFunctionFactory;
import aima.core.environment.sudoku.SudokuGoalTest;
import aima.core.environment.sudoku.SudokuHCAction;
import aima.core.environment.sudoku.SudokuHCGoalTest;
import aima.core.search.framework.GraphLimitedSearch;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Node;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.informed.AStarSearch;
import aima.core.search.informed.GreedyBestFirstSearch;
import aima.core.search.local.HillClimbingSearch;
import aima.core.search.uninformed.DepthFirstSiblingCutSearch;
import aima.core.search.uninformed.DepthFirstSearch;

public class SudokuDemo {
	private static ArrayList<SudokuBoard> sudokus = new ArrayList<SudokuBoard>();

	private static int algoNbr = 0; // {0 All, 1 DFS, 2 SiblingTest, 3 HC, 4 BFh1, 5 BFh2}
	private static int nodesLimit = 1000;
	private static boolean printInitState = false;
	private static boolean printFinalState = false;
	private static boolean printAllStates = false;
	private static boolean printActions = false;
	private static boolean outputStatistics = true;
	private static BufferedWriter outFile = null;
	private static int output = 0;

	public static void main(String[] args) {
		String filename = askForFilename();
		loadGrids(filename);
		askForConfig();
		if (outputStatistics) initializeStatsOutputFile();
		
		for(int i=0; i < sudokus.size(); i++)
		{
			SudokuBoard board = sudokus.get(i);
			System.out.println("\n\n  <-- Sudoku "+(i+1)+" -->");
				if(printInitState) System.out.print(board);
			
			switch(algoNbr){
				case 1:
					sudokuDFSDemo(board);
					break;
				case 2:
					sudokuDepthFirstSiblingCutDemo(board);
					break;
				case 3:
					sudokuHCDemo(board);
					break;
				case 4:
					sudokuBestFirstDemo(board,1);
					break;
				case 5:
					sudokuBestFirstDemo(board,2);
					break;
				default:
					sudokuDFSDemo(board);
					sudokuDepthFirstSiblingCutDemo(board);
					sudokuHCDemo(board);
					sudokuBestFirstDemo(board,1);
					sudokuBestFirstDemo(board,2);
			}
		}
		
		
		if (outputStatistics) closeStatsOutputFile();
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
			System.out.println("Error loading the grids from the configuration file.");
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
		System.out.println("Enter the name of the file containing the grids: (default: 100sudoku.txt) ");
		try{
			line = keyboard.readLine();
			if (line.equals("")) line = "100sudoku.txt";
		}catch(IOException e){
			System.out.println("Error reading the filename.\n"+e.getMessage());
			e.printStackTrace();
		}
		return line;
	}
	
	 /** 
     * Ask for configurations of the program
     *  
     */  
	private static void askForConfig(){
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		try{
			String line = "";
			System.out.println("What algorithm would you like to use? (Default: Execute all)");
			System.out.println("1- Depth First Search \n2- Depth First Search with siblings cut\n"
					+ "3- Hill Climbing\n4- Best first, heuristic 1\n5- Best first, heuristic 2");
			line = keyboard.readLine();
			if (!line.equals("")) {
				SudokuDemo.algoNbr = Integer.parseInt(line);
				line = "";
			}
			System.out.println("What limit of nodes would you use? (Default: 1000)");
			line = keyboard.readLine();
			if (!line.equals("")) {
				SudokuDemo.nodesLimit = Integer.parseInt(line);
				line = "";
			}
			System.out.println("What would you like to add to the display? (Default: only search Instrumentation and Outcome)");
			System.out.println("1- Initial state and Final state \n2- All states \n3- Initial state, Final state and Actions");
			line = keyboard.readLine();
			int print = 0;
			if (!line.equals("")) print = Integer.parseInt(line);
			switch (print){
				case 1:
					SudokuDemo.printInitState = true;
					SudokuDemo.printFinalState = true;
					break;
				case 2:
					SudokuDemo.printInitState = true;
					SudokuDemo.printFinalState = true;
					SudokuDemo.printAllStates = true;
					break;
				case 3:
					SudokuDemo.printInitState = true;
					SudokuDemo.printFinalState = true;
					SudokuDemo.printActions = true;
					break;
				default:
					break;
			}
		}catch(IOException e){
			System.out.println("Erreur lors de la lecture d'un des paramètres de configuration");
			e.printStackTrace();
		}
	}
	
	
	 /** 
     * Initialize the file output for writing statistics data
     * Statistics file will ot
					SudokuDemo.printInstrumentation = true;nly contains the number of nodes expanded for the algorithm choosen
     *  
     */  	
	private static void initializeStatsOutputFile(){
		try {
			SudokuDemo.outFile = new BufferedWriter(new FileWriter("algo"+SudokuDemo.algoNbr+"-"+SudokuDemo.nodesLimit+".txt"));
		} catch (IOException e) {
			System.out.println("Error writing statistics data in output file.\n"+ e.getMessage());
			e.printStackTrace();
		}
	}
	
	 /** 
     * Close the statistics file output
     *  
     */  	
	private static void closeStatsOutputFile(){
		if (outFile != null)
			try {
				outFile.close();
			} catch (IOException e) {
				System.out.println("Error trying to close the output file.\n"+ e.getMessage());
				e.printStackTrace();
			}
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
        for (int i = 0; i < intArray.length; i++)
            intArray[i] = Integer.parseInt(nbr.substring(i, i+1)); 
        return intArray;  
    }  
    
	public static ArrayList<SudokuBoard> getSudokus() {
		return sudokus;
	}

	public static void setSudokus(ArrayList<SudokuBoard> sudokus) {
		SudokuDemo.sudokus = sudokus;
	}
	
	private static void sudokuDFSDemo(SudokuBoard initialBoard) {
		System.out.println("\n> Depth First Search");
		try {
			Problem problem = new Problem(initialBoard,
					SudokuFunctionFactory.getActionsFunction(),
					SudokuFunctionFactory.getResultFunction(),
					new SudokuGoalTest(printAllStates));
			GraphLimitedSearch graphLimitedSearch = new GraphLimitedSearch(nodesLimit);
			Search search = new DepthFirstSearch(graphLimitedSearch);
			SearchAgent agent = new SearchAgent(problem, search);
			if(printActions) printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
			System.out.println("Search Outcome : " + graphLimitedSearch.getOutcome());
			if(printFinalState) System.out.print("Final State : \n" + graphLimitedSearch.getLastSearchState());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void sudokuDepthFirstSiblingCutDemo(SudokuBoard initialBoard) {
		System.out.println("\n> Depth First Search (Sibling Cut)");
		try {
			Problem problem = new Problem(initialBoard,
					SudokuFunctionFactory.getActionsFunction(),
					SudokuFunctionFactory.getResultFunction(),
					new SudokuGoalTest(printAllStates));
			DepthFirstSiblingCutSearch depthFirstSiblingCutSearch = new DepthFirstSiblingCutSearch(nodesLimit);
			Search search = depthFirstSiblingCutSearch;
			SearchAgent agent = new SearchAgent(problem, search);
			if(printActions) printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
			System.out.println("Search Outcome : " + depthFirstSiblingCutSearch.getOutcome());
			if(printFinalState) System.out.print("Final State : \n" + depthFirstSiblingCutSearch.getLastSearchState());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void sudokuHCDemo(SudokuBoard initialBoard) {
		System.out.println("\n> HillClimbing");
		SudokuBoard newBoard = new SudokuBoard(initialBoard);
		newBoard.fillWithRandomValues();
		try {
			Problem problem = new Problem(new SudokuBoard(newBoard),
					SudokuFunctionFactory.getIActionsFunction(),
					SudokuFunctionFactory.getIResultFunction(),
					new SudokuHCGoalTest(printAllStates));
			HillClimbingSearch search = new HillClimbingSearch(
					new ConstraintsUnsatisfiedHeuristic()){
				private int expanded = 0;
				public List<Node> expandNode(Node node, Problem problem) {
					expanded++;
					if (expanded <= nodesLimit)
						return super.expandNode(node, problem);
					else
						return new ArrayList<Node>();
					};
			};
			SearchAgent agent = new SearchAgent(problem, search);
			if(printActions) printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
			System.out.println("Search Outcome : " + search.getOutcome());
			if(printFinalState) System.out.print("Final State : \n" + search.getLastSearchState());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void sudokuBestFirstDemo(SudokuBoard initialBoard, int heuristicNbr) {
		System.out.println("\n> BestFirst - Heuristic number "+heuristicNbr);
		try {
			Problem problem = new Problem(initialBoard,
					SudokuFunctionFactory.getActionsFunction(),
					SudokuFunctionFactory.getResultFunction(),
					new SudokuGoalTest(printAllStates));
			GraphLimitedSearch graphLimitedSearch = new GraphLimitedSearch(nodesLimit);
			Search search = new AStarSearch(graphLimitedSearch,
					new SudokuBiggerConstraintHeuristicFunction(heuristicNbr));
			SearchAgent agent = new SearchAgent(problem, search);
			if(printActions) printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
			System.out.println("Search Outcome : " + graphLimitedSearch.getOutcome());
			if(printFinalState) System.out.print("Final State : \n" + graphLimitedSearch.getLastSearchState());
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
			if (SudokuDemo.outFile != null && key == "nodesExpanded")
				try {
					outFile.write(property);
					outFile.newLine();
				} catch (IOException e) {
					System.err.println("Can't write to statistics output file.\n"+e.getMessage());
					e.printStackTrace();
				}
		}
	}

	private static void printActions(List<Action> actions) {
		for (int i = 0; i < actions.size(); i++) {
			String action = actions.get(i).toString();
			System.out.println(action);
		}
	}
}
