package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;


class Node {

	public double id;
	public int index;
	public int lowlink;
	public ArrayList<Edge> edges;
	public int component;
	public boolean instack;

	public Node(int index, int lowlink, ArrayList<Edge> edges) {
		this.index = index;
		this.lowlink = lowlink;
		this.edges = edges;
		this.instack = false;
	}

	public Node(ArrayList<Edge> edges) {
		this.edges = edges;
		this.index = -1;
		this.lowlink = -1;
		this.instack = false;
	}

}

class Edge {
	public double cost;
	public int nodeA;
	public int nodeB;
	public int component;

	public Edge(int nodeA, int nodeB, double cost) {
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.cost = cost;
		this.component = -1;
	}

}

class Component {
	public double cost;
	// only IO edges
	public ArrayList<Edge> edges;
	public ArrayList<Node> nodes;
	public int id;
	public boolean leaf, root;
	public boolean visited;
	public double bestMin;
	public double bestMax;
	public Component() {
		this.nodes = new ArrayList<Node>();
		this.edges = new ArrayList<Edge>();
		this.root = true;
		this.leaf = false;
		this.visited = false;
		this.bestMax = 0;
		this.bestMin = Double.MAX_VALUE;
	}
}

class Graph {

	public ArrayList<Node> graph;

	public Graph(int numberOfHuts, int p1, int p2, int p3, int p4, int K) {
//		System.out.println("N:" + numberOfHuts + " P: " + p1 + ", " + p2 + ", "
//				+ p3 + ", " + p4 + " K: " + K);
		int number = 0;
		graph = new ArrayList<Node>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		int i, j;
		double identifier, preliminaryPrice;
		for (i = 0; i < numberOfHuts; i++) {
			for (j = i - K; j <= i + K; j++) {

				if (j == i || j < 0 || j >= numberOfHuts)
					continue; // track does not exist
				identifier = i * (2 * K + 1) + (j - i + K - 1);
				preliminaryPrice = (p1 * identifier) % p2;
				if (p3 <= preliminaryPrice && preliminaryPrice <= p4) {
					edges.add(new Edge(i, j, preliminaryPrice));
					number++;
				}
			}

			graph.add(i, new Node(new ArrayList<Edge>(edges)));
			edges.clear();

		}
//		System.out.println("Pocet hran " + number);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		int size = 0;
		for (int i = 0; i < graph.size(); i++) {
			size += graph.get(i).edges.size();
		}
		return "Edges: " + size;
	}

}

class CondensedGraph {

	public ArrayList<Component> components;
	private int index;
	static int counter = 0;
	private Stack<Node> stack;
	private ArrayList<Node> graph;
	private int componentCounter;
	private double bobsPrice;
	private double alicesPrice;
	private long timeout;
	
	public CondensedGraph(Graph g) {
		stack = new Stack<Node>();
		components = new ArrayList<Component>();
		componentCounter = 0;
		condenseGraph(g);
		
	}

	public void condenseGraph(Graph g) {

		index = 0;
		this.graph = g.graph;


		for (Node n : graph) {
			if (n.index == -1) {
				tarjan(n);
			}

		}
		createComponents();
//		int rootCount = 0;
//		int leafCount = 0;
//		for (Iterator<Component> iterator = components.iterator(); iterator.hasNext();) {
//			Component component = (Component) iterator.next();
//			if(component.leaf) leafCount++;
//			if(component.root) rootCount++;
//		}
//		System.out.println("Pocet rootu: "+rootCount+" Pocet listu: "+leafCount);
	}

	private void tarjan(Node n) {

		n.index = n.lowlink = index++;

		stack.push(n); // pridej na zasobnik
		n.instack = true;

		for (Edge edge : n.edges) {
			// succ = graph.get(edge.nodeB);
			if (graph.get(edge.nodeB).index == -1) {
				tarjan(graph.get(edge.nodeB));
				n.lowlink = Math.min(n.lowlink, graph.get(edge.nodeB).lowlink);
			} else if (graph.get(edge.nodeB).instack) {
				n.lowlink = Math.min(n.lowlink, graph.get(edge.nodeB).index);
			}

		}
		if (n.lowlink == n.index) {
			Component component = new Component();
			components.add(component);
			component.id = components.size()-1;
			do {
				component.nodes.add(stack.pop());
				component.nodes.get(component.nodes.size() - 1).instack = false;
				component.nodes.get(component.nodes.size() - 1).component = components.size()-1;
			} while (component.nodes.get(component.nodes.size() - 1).index != n.index);

			
		}

	}
	
	private void createComponents()
	{
		double cost;
		
		for(Node node : graph)
		{
			for(Edge e : node.edges)
			{
				if(graph.get(e.nodeB).component != graph.get(e.nodeA).component) components.get(graph.get(e.nodeB).component).root = false;
			}
		}
		
		for(Component c : components)
		{
			cost = 0;
			
			for(Node n : c.nodes)
			{
				for(Edge e : n.edges)
				{
					
					if(graph.get(e.nodeB).component != c.id )
					{
//						components.get(graph.get(e.nodeB).component).root = false;
						c.edges.add(e);
					}
					else
					{
						if(e.cost > cost) cost = e.cost;
					}
				}
				
			}
			
			if(c.edges.size() == 0) c.leaf = true;
//			if(c.root) System.out.println(c.leaf);
			c.cost = cost;
		}
		
	}

	public void bobsRoute() {
		timeout = System.currentTimeMillis();
		bobsPrice = 0;
		alicesPrice = Double.MAX_VALUE;
		double price = 0;
		for(Component c : components)
		{
			boolean alice = true, bob = true;
			if(c.root)
			{
				
				
				
				c.visited = true;
				if(c.leaf)
				{
					alicesPrice = 0;
					alice = false;
					if(c.cost > bobsPrice) bobsPrice = c.cost;
				}
				for(Edge e : c.edges)
				{
					double minprice = e.cost;
					double maxprice = e.cost+c.cost;
					
					DFS(components.get(graph.get(e.nodeB).component),e, price, minprice, maxprice, alice, bob);
				}
				
				
			}
			
			
			
			
			
		}
		System.out.println((int)(alicesPrice)+" "+(int)(bobsPrice));
	
		
	}
	
	private void DFS(Component c, Edge incoming, double price, double minprice, double maxprice, boolean alice, boolean bob)
	{
		
		if(c.bestMax > price + maxprice) bob = false;
		else c.bestMax = price+maxprice;
		if(c.bestMin < price + minprice) alice = false;
		else c.bestMin = price+minprice;
		if(alice || bob ){
		
		
		
		double nextPrice;
		double nextMaxPrice;
		if(c.leaf){
			nextPrice = price;
			maxprice+=c.cost;
			if(nextPrice+maxprice>bobsPrice) bobsPrice = nextPrice+maxprice;
			if(nextPrice+minprice<alicesPrice) alicesPrice = nextPrice+minprice;
		}else{
			
		
		for(Edge e : c.edges)
		{
			nextPrice = price;
			nextMaxPrice = maxprice;
			Component nextComponent = components.get(graph.get(e.nodeB).component);
			if(e.nodeA == incoming.nodeB)
			{
				nextPrice+=e.cost;
				nextMaxPrice += c.cost;
			}
			else
			{
				nextPrice+=e.cost+c.cost;
			}
			 DFS(nextComponent, e,nextPrice, minprice, nextMaxPrice, alice, bob);
			
			}
//			if(minprice>nextMin) minprice = nextMin;
//			if(maxprice<nextMax) maxprice = nextMax;
		}
		}
		
		
		
		
	}

}

public class Main {

	// MAIN CLASS STUFF

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

		try {
			readFromStdInFast();
		} catch (IOException e) {

			e.printStackTrace();
		}

		start = System.currentTimeMillis();

		Graph rawGraph = new Graph(numberOfHuts, p1, p2, p3, p4, K);

		end = System.currentTimeMillis();

//		System.out.println("Graph " + rawGraph + " generated in "
//				+ (end - start) + " ms");

		start = System.currentTimeMillis();
		CondensedGraph condensedGraph = new CondensedGraph(rawGraph);
		end = System.currentTimeMillis();

//		System.out.println("Zkondenzoval jsem nejakej bordel za "
//				+ (end - start) + " ms (nebo se to minimalne nezacyklilo): "
//				+ condensedGraph.components.size());
		condensedGraph.bobsRoute();

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
