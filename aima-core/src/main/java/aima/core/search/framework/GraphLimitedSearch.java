package aima.core.search.framework;

import java.util.ArrayList;
import java.util.List;

public class GraphLimitedSearch extends GraphSearch {
	
	public enum SearchOutcome {
		FAILURE, SOLUTION_FOUND
	};
	private SearchOutcome outcome = SearchOutcome.FAILURE;
	
	private Object lastState = null;
	
	private int expanded = 0;
	private int nodesLimit;
	
	public GraphLimitedSearch(int nodesLimit){
		this.nodesLimit = nodesLimit;
	}
	
	public SearchOutcome getOutcome() {
		return this.outcome;
	}
	
	public Object getLastSearchState() {
		return this.lastState;
	}
	
	public int getNbrOfNodesExpanded() {
		return this.expanded;
	}
	
	@Override
	public List<Node> expandNode(Node node, Problem problem) {
		this.expanded++;
		if (this.expanded <= this.nodesLimit)
			return super.expandNode(node, problem);
		else
			return new ArrayList<Node>();
	}
	
	@Override
	public List<Node> getResultingNodesToAddToFrontier(Node nodeToExpand, Problem problem) {
		
		List<Node> nodes = super.getResultingNodesToAddToFrontier(nodeToExpand,problem);
		for(Node child : nodes)
		{
			this.lastState = child.getState();
			if (SearchUtils.isGoalState(problem, child)) {
				this.outcome = SearchOutcome.SOLUTION_FOUND;
			}
		}
		return nodes;
	}
}