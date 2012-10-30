
public class main {

	public class Node
	{
		
		public double id;
		public double index;
		public int component;
		
	}
	public class Edge
	{
		public double id;
		public double cost;
		public int nodeA;
		public int nodeB;
		public int component;
		public short direction;
		
	}
	
	public class Component
	{
		public double cost;
		//only IO edges
		public Edge[] edges;
		
	}
	
	public class Graph
	{
		
		public Edge edges[];
		
		
		
	}
	public static class CondensedGraph
	{
		
		public Component components[];
		
		private CondensedGraph()
		{
			
		}
		
		public static CondensedGraph condensedGraphFactory()
		{
			return new CondensedGraph();
		}
		
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
