import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.StringTokenizer;



class Node
{
	
	public double id;
	public int index;
	public int lowlink;
	public int component;
	public ArrayList<Edge> edges;
	
	public Node(int index, int lowlink, ArrayList<Edge> edges)
	{
		this.index = index;
		this.lowlink = lowlink;
		this.edges = edges;
	}
	
	public Node(ArrayList<Edge> edges)
	{
		this.edges = edges;
		this.index = -1;
		this.lowlink = -1;
	}
	
}
class Edge
{
	public double id;
	public double cost;
	public int nodeA;
	public int nodeB;
	public int component;
	public short direction;
	
	
	public Edge(int nodeA, int nodeB, double cost)
	{
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.cost = cost;
		this.component = 0;
		this.direction = -2;
	}
	
	public Edge(int nodeA, int nodeB, double cost, int component, short direction)
	{
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.cost = cost;
		this.component = component;
		this.direction = direction;
	}
	
}

class Component
{
	public double cost;
	//only IO edges
	public ArrayList<Edge> edges;
	public ArrayList<Node> nodes;
	
}

class Graph
{
	
	public ArrayList<Node> graph;
	
	public  Graph(int numberOfHuts, int p1, int p2, int p3, int p4, int K)
	{
		
		graph = new ArrayList<Node>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		int i, j;
		double identifier, preliminaryPrice;
		for(i = 0; i < numberOfHuts; i++){
			for (j = i-K; j < i+K; j++) {
				//track does not exist
				
				if(j==i || j < 0 || j >= numberOfHuts || Math.abs(i-j) > K) continue;
				identifier = i * (2*K+1) + (j - i + K - 1);
				preliminaryPrice = ( p1 * identifier ) % p2;
				if(p3 <= preliminaryPrice && preliminaryPrice <= p4 )
				{				
					edges.add(new Edge(i, j, preliminaryPrice));
				}		
			}
			
				graph.add(i, new Node(new ArrayList<Edge>(edges)));
				edges.clear();
			
			
		} 
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		int size = 0;
		for(Node sublist : graph)
		{
			size+= sublist.edges.size();
		}
		return "Edges: "+size;
	}
	
	
	
	
}
class CondensedGraph
{
	
	public ArrayList<Component> components;
	private static int index;
	
	private CondensedGraph()
	{
		
	}
	
	public static CondensedGraph condenseGraph(Graph g)
	{
		index = 0;
		CondensedGraph instance = new CondensedGraph();
		instance.components = new ArrayList<Component>();
		Stack<Node> stack = new Stack<Node>();
		for(Node n : g.graph)
		{
			if(n.index == -1)
			{
				instance.tarjan(g, n, instance.components, stack);
			}
			
		}
		
		
		
		return instance;
	}
	
	private void tarjan(Graph g, Node n, ArrayList<Component> components, Stack<Node> stack)
	{
				n.index = index;
			    n.lowlink = index;
			    index++;
			    stack.push(n); //pridej na zasobnik
			    
			    
			    
			    for(Edge edge : n.edges)
			    {
			    	Node succ = g.graph.get(edge.nodeB);
			    	if(succ.index == -1)
			    	{
			    		tarjan(g, succ, components, stack);
			    		n.lowlink = Math.min(n.lowlink, succ.lowlink);
			    	}
			    	else if(stack.contains(n))
			    	{
			    		n.lowlink = Math.min(n.lowlink, succ.index);
			    	}
			    	
			    } 
			    if(n.lowlink == n.index)
		    	{
		    		Component component = new Component();
		    		component.nodes = new ArrayList<Node>();
		    		if(!stack.empty()){
		    		do
		    		{
		    			component.nodes.add(stack.pop());
		    			
		    		}
		    		while(!stack.empty() && stack.lastElement().index != n.index);
		    		components.add(component);
		    	}
		    	}
			    
			   
	}
	
	
}

public class main {

	
	
	
	
	
	
	
	
	//MAIN CLASS STUFF
	
	
	
	
	private static int numberOfHuts;
	private static int p1;
	private static int p2;
	private static int p3;
	private static int p4;
	private static int K;
	static long start, end;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	
		
		try 
		{
			readFromStdInFast();
		}
		catch (IOException e)
		{
			
			e.printStackTrace();
		}
		
		start = System.currentTimeMillis();
		
		Graph rawGraph = new Graph(numberOfHuts, p1, p2, p3, p4, K);
		
		end = System.currentTimeMillis();
		
		System.out.println("Graph "+rawGraph+" generated in "+(end - start)+" ms");
		
start = System.currentTimeMillis();
		
 		CondensedGraph condensedGraph = CondensedGraph.condenseGraph(rawGraph);
		
		end = System.currentTimeMillis();
		
		
		System.out.println("Zkondenzoval jsem nejakej bordel za "+(end - start)+" ms (nebo se to minimalne nezacyklilo): "+condensedGraph.components.size());
		

	}
	
	static void readFromStdInFast() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// read number of nodes:
		StringTokenizer st = new StringTokenizer(br.readLine());
		// System.out.println(st.countTokens());
		numberOfHuts = Integer.parseInt(st.nextToken());
		p1 = Integer.parseInt(st.nextToken());
		p2 = Integer.parseInt(st.nextToken());
		p3 = Integer.parseInt(st.nextToken());
		p4 = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());

	}
	
	

}
