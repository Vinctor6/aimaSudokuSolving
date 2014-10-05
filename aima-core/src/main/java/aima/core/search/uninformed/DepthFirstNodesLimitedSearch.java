package aima.core.search.uninformed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aima.core.agent.Action;
import aima.core.search.framework.CutOffIndicatorAction;
import aima.core.search.framework.Node;
import aima.core.search.framework.NodeExpander;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchUtils;

public class DepthFirstNodesLimitedSearch extends NodeExpander implements Search {
	private static String PATH_COST = "pathCost";
	private static List<Action> cutoffResult = null;
	private final int limit;
	private int cpt;
	private boolean cutoff_occurred;

	public DepthFirstNodesLimitedSearch(int limit) {
		this.limit = limit;
		this.cpt = 0;
		this.cutoff_occurred = false;
	}

	/**
	 * Returns <code>true</code> if the specified action list indicates a search
	 * reached it limit without finding a goal.
	 * 
	 * @param result
	 *            the action list resulting from a previous search
	 * 
	 * @return <code>true</code> if the specified action list indicates a search
	 *         reached it limit without finding a goal.
	 */
	public boolean isCutOff(List<Action> result) {
		return 1 == result.size()
				&& CutOffIndicatorAction.CUT_OFF.equals(result.get(0));
	}

	/**
	 * Returns <code>true</code> if the specified action list indicates a goal
	 * not found.
	 * 
	 * @param result
	 *            the action list resulting from a previous search
	 * 
	 * @return <code>true</code> if the specified action list indicates a goal
	 *         not found.
	 */
	public boolean isFailure(List<Action> result) {
		return 0 == result.size();
	}

	// function DEPTH-LIMITED-SEARCH(problem, limit) returns a solution, or
	// failure/cutoff
	/**
	 * Returns a list of actions to the goal if the goal was found, a list
	 * containing a single NoOp Action if already at the goal, an empty list if
	 * the goal could not be found, or a list containing a single
	 * CutOffIndicatorAction.CUT_OFF if the search reached its limit without
	 * finding a goal.
	 * 
	 * @param p
	 * @return if goal found, the list of actions to the Goal. If already at the
	 *         goal you will receive a List with a single NoOp Action in it. If
	 *         fail to find the Goal, an empty list will be returned to indicate
	 *         that the search failed. If the search was cutoff (i.e. reached
	 *         its limit without finding a goal) a List with one
	 *         CutOffIndicatorAction.CUT_OFF in it will be returned (Note: this
	 *         is a NoOp action).
	 */
	public List<Action> search(Problem p) throws Exception {
		clearInstrumentation();
		// return RECURSIVE-DLS(MAKE-NODE(INITIAL-STATE[problem]), problem)
		return recursiveDLS(new Node(p.getInitialState()), p);
	}

	@Override
	/**
	 * Sets the nodes expanded and path cost metrics to zero.
	 */
	public void clearInstrumentation() {
		super.clearInstrumentation();
		metrics.set(PATH_COST, 0);
	}

	/**
	 * Returns the path cost metric.
	 * 
	 * @return the path cost metric
	 */
	public double getPathCost() {
		return metrics.getDouble(PATH_COST);
	}

	/**
	 * Sets the path cost metric.
	 * 
	 * @param pathCost
	 *            the value of the path cost metric
	 */
	public void setPathCost(Double pathCost) {
		metrics.set(PATH_COST, pathCost);
	}

	//
	// PRIVATE METHODS
	//

	// function RECURSIVE-DLS(node, problem) returns a solution, or
	// failure/cutoff
	private List<Action> recursiveDLS(Node node, Problem problem) {
		//If we found the goal state
		if (SearchUtils.isGoalState(problem, node)) { 	
			setPathCost(node.getPathCost());
			return SearchUtils.actionsFromNodes(node.getPathFromRoot());
		}
		//If we are at the limit of nodes
		else if (this.cpt == this.limit) { 				
			this.cutoff_occurred = true;
			return cutoff();
		}
		//Else : evaluate the children of node
		else {											
			for (Node child : this.expandNode(node, problem)) {
				if(this.cutoff_occurred) return cutoff(); //stop if we touched the limit already
				(this.cpt)++; //if not, increase number of nodes generated
				List<Action> result = recursiveDLS(child, problem); //generate actions from node
				if (!isFailure(result)) return result; //return existing actions
			}
			return failure();
		}
	}

	private List<Action> cutoff() {
		// Only want to created once
		if (null == cutoffResult) {
			cutoffResult = new ArrayList<Action>();
			cutoffResult.add(CutOffIndicatorAction.CUT_OFF);
			// Ensure it cannot be modified externally.
			cutoffResult = Collections.unmodifiableList(cutoffResult);
		}
		return cutoffResult;
	}

	private List<Action> failure() {
		return Collections.emptyList();
	}
}