package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.AllPermission;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Collection;
import java.util.StringTokenizer;

public class Main {
	public static long inputSize;
	public static long aSize, bSize, cSize, dSize, difference, permutationDiff;
	public static Graph originaGraph = new Graph();
	public static HashMap<Double, Node> map = new HashMap<Double, Node>();
	public static Node createNode(double index) {
		Node ziskano = map.get(index);
		if (ziskano == null) {
			Node newNode = new Node(index);
			map.put(index, newNode);
			return newNode;
		} else {
			return ziskano;
		}
	}

	public static void main(String[] parametry) throws IOException {
		
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(System.in));
			StringTokenizer stringTokenizer = new StringTokenizer(
					bufferedReader.readLine());
		
			inputSize = Integer.parseInt(stringTokenizer.nextToken());
			long inputIndex, tmpA, tmpB;

			for (inputIndex = 0; inputIndex < inputSize; inputIndex++) {
				stringTokenizer = new StringTokenizer(bufferedReader.readLine());
				tmpA = Integer.parseInt(stringTokenizer.nextToken());
				tmpB = Integer.parseInt(stringTokenizer.nextToken());
				originaGraph.addEgde(createNode(tmpA), createNode(tmpB));
			}
			Tarjan tarjanInstance = new Tarjan();

			ArrayList<ArrayList<Node>> tarjan = tarjanInstance
					.run(originaGraph);
			aSize = tarjan.get(0).size();
			bSize = tarjan.get(1).size();
			cSize = tarjan.get(2).size();
			
			countDifference();
			System.out.println(aSize+" "+bSize+" "+cSize+" "+dSize);
			if (dSize % 2 == 0) {
				countEven();
			} else {
				countOdd();
			}

		
	}

	static void countEven() {
		// System.out.println("D je sude.");
		long middle, axis, middleAxis, allMiddle, allAxis, allMiddleAxis;
		BigInteger Bmiddle, Baxis, BmiddleAxis, BallMiddle, BallAxis, BallMiddleAxis;
		BigInteger allNonsymmetric;
		BigInteger nonSymmetric;

		BigInteger rl;
		rl = new BigInteger("0");

		long result = 0;
		long first = dSize / 2 - 1;

		for (int i = 1; i < first; i++) {
			result += (dSize - i - 2) * Math.floor(i / 2);
			if (i % 2 == 1) {
				result += Math.ceil((dSize - i - 1) / 2.0);
			}
		}

		for (int i = 0; i < Math.ceil(first / 2.0); i++) {
			result += first - 2 * i;
		}
		
		rl = new BigInteger(String.valueOf(result));

		middle = (long) Math.floor((dSize / 2 - 1) / 2);
		middleAxis = (dSize % 4 == 0) ? 1 : 0;
		axis = (long) Math.floor((dSize / 2 - 1) / 2)
				+ (long) Math.floor((dSize-1) / 4);
		nonSymmetric = rl.subtract(new BigInteger(String.valueOf(middle
				+ middleAxis + axis)));
		
		allNonsymmetric = nonSymmetric.multiply(
				new BigInteger(String.valueOf(combinational(dSize, 3))))
				.multiply(new BigInteger(String.valueOf(permutationDiff)));

		allMiddle = middle * countCombinations() * permutationDiff;
		BallMiddle = new BigInteger(String.valueOf(allMiddle));
		allAxis = axis * countAxisCombinations() * permutationDiff;
		allAxis += axis * (2 * (dSize / 2 - 1)) * difference;
		BallAxis = new BigInteger(String.valueOf(allAxis));
		allMiddleAxis = middleAxis * countMiddleAxisCombinations()
				* permutationDiff;
		allMiddleAxis += middleAxis * ((dSize / 2) + (2 * (dSize / 4 - 1)))
				* difference;
		BallMiddleAxis = new BigInteger(String.valueOf(allMiddleAxis));
//		System.out.println("rl "+rl);
//		System.out.println("stred " + middle);
//		System.out.println("osova " + axis);
//		System.out.println("oboji " + middleAxis);
//		System.out.println(nonSymmetric);
//		System.out.println("rl "+rl);
//		System.out.println("allstred " + allMiddle);
//		System.out.println("allosova " + allAxis);
//		System.out.println("alloboji " + allMiddleAxis);
//		System.out.println(allNonsymmetric.intValue());
		System.out.println(allNonsymmetric.add(BallMiddle).add(BallAxis).add(BallMiddleAxis));
	}

	static long countMiddleAxisCombinations() {
		long result = 0;
		result += 0.5 * (dSize / 4) * (dSize - 2 + dSize / 2);
		result += 0.5 * (dSize / 4 - 1) * (dSize / 2 - 2);
		result += 0.5 * (dSize / 4 - 2) * (dSize / 4 - 1) * (dSize / 2 - 1);
		result += 0.5 * (dSize / 4 - 1) * (dSize / 4);
		result += (dSize / 4 - 1) * (dSize / 4);
		if (dSize > 7) {
			result += combinational((dSize / 4), 2) * (dSize / 2 - 1);
		}
		for (long i = dSize/2-3; i >= dSize/4; i--) {
			  for (long j = i; j >= dSize/4; j--) {
			  result += j;
			  }
			  }
		result -= (dSize / 2) + (2 * (dSize / 4 - 1));
		return result;
	}

	static long countAxisCombinations() {
		long result = 0;
		result += 0.5 * (dSize / 2 - 1) * dSize;
		if (dSize > 5) {
			result += combinational((dSize / 2 - 1), 2) * (dSize / 2);
		}
		if (dSize > 7) {
			result += combinational((dSize / 2 - 1), 3);
		}
		result += 0.5 * (dSize / 2 - 1) * dSize / 2;
		result -= 2 * (dSize / 2 - 1);
		return result;
	}

	static long countCombinations() {
		long tmpMiddle = 0;
		for (long i = 1; i < dSize / 2; i++) {
			tmpMiddle += i * (dSize - 1 - i);
		}
		return tmpMiddle;
	}

	static void countOdd() {
		BigInteger tmpResult = new BigInteger("0");
		long first = ((dSize - 1) / 2) - 1;
		long summands = ((dSize - 1) / 2) - 1;

		long root;
		long leaf;

		for (root = 1; root < (summands + 1); root++) {
			for (leaf = 0; leaf < root; leaf++) {
				tmpResult = tmpResult.add(new BigInteger(String.valueOf(first
						- leaf)));
			}
		}

		tmpResult = tmpResult.multiply(
				new BigInteger(String.valueOf(combinational(dSize, 3))))
				.multiply(new BigInteger(String.valueOf(permutationDiff)));

		System.out.println(tmpResult);
	}

	static long combinational(long upper, long lower) {
		long coeff = 1;
		if (lower == 3) {
			return upper * (upper - 1) * (upper - 2) / 6;

		}
		if (lower == 2) {
			return upper * (upper - 1) / 2;
		}
		return coeff;
	}

	static void countDifference() {
		if ((aSize - bSize == bSize - cSize)) {
			difference = 1;
			permutationDiff = 1;
		} else if ((!(aSize == bSize)) && (!(bSize == cSize))
				&& (!(aSize == cSize))) {
			difference = 3;
			permutationDiff = 6;
		} else {
			difference = 2;
			permutationDiff = 3;
		}
	}

}

class Graph {

	private HashMap<Node, List<Edge>> adjacentEdges = new HashMap<Node, List<Edge>>();

	public void addEgde(Node from, Node to) {
		List<Edge> list;
		if (!adjacentEdges.containsKey(from)) {
			list = new ArrayList<Edge>();
			adjacentEdges.put(from, list);
		} else {
			list = adjacentEdges.get(from);
		}
		list.add(new Edge(from, to));
	}

	public List<Edge> get(Node source) {
		List<Edge> toReturn = adjacentEdges.get(source);
		if (toReturn == null) {
			return Collections.emptyList();
		} else {
			return toReturn;
		}
	}

	public Graph getReversedList() {
		Graph list = new Graph();
		for (List<Edge> edges : adjacentEdges.values()) {
			for (Edge edge : edges) {
				list.addEgde(edge.to, edge.from);
			}
		}
		return list;
	}

	public Set<Node> getSourceNodeSet() {
		return adjacentEdges.keySet();
	}

	public Collection<Edge> getAllEdges() {
		List<Edge> edges = new ArrayList<Edge>();
		for (List<Edge> e : adjacentEdges.values()) {
			edges.addAll(e);
		}
		return edges;
	}
}

class Edge {
	Node from;
	Node to;

	public Edge(Node from, Node to) {
		this.from = from;
		this.to = to;
	}

}

class Node implements Comparable<Node> {
	public double name;
	public double lowlink = -1; // used for Tarjan's algorithm
	public double index = -1; // used for Tarjan's algorithm

	public Node(double nameToInsert) {
		this.name = nameToInsert;
	}

	public int compareTo(final Node param) {
		return param == this ? 0 : -1;
	}

}

class Tarjan {

	private double index = 0;
	private ArrayList<Node> stack = new ArrayList<Node>();
	private ArrayList<ArrayList<Node>> SCC = new ArrayList<ArrayList<Node>>();

	/* The funtion tarjan has to be called for every unvisited node of the graph */
	public ArrayList<ArrayList<Node>> run(Graph praph) {
		SCC.clear();
		index = 0;
		stack.clear();
		if (praph != null) {
			List<Node> nodeList = new ArrayList<Node>(praph.getSourceNodeSet());
			for (Node node : nodeList) {
				if (node.index == -1) {
					tarjan(node, praph);
				}
			}
		}
		return SCC;
	}

	private ArrayList<ArrayList<Node>> tarjan(Node node, Graph list) {
		node.index = index;
		node.lowlink = index;
		index++;
		stack.add(0, node);
		// System.out.println("hledam " + v.name);
		for (Edge edge : list.get(node)) {
			Node node2 = edge.to;
			if (node2.index == -1) {
				tarjan(node2, list);
				node.lowlink = Math.min(node.lowlink, node2.lowlink);
			} else if (stack.contains(node2)) {
				node.lowlink = Math.min(node.lowlink, node2.index);
			}
		}
		if (node.lowlink == node.index) {
			Node node3;
			ArrayList<Node> component = new ArrayList<Node>();
			do {
				node3 = stack.remove(0);
				component.add(node3);
			} while (node3 != node);

			// PRIDANO
			// puvodni: SCC.add(component);
			if (component.size() > 1) {
				SCC.add(component);
			} else {
				Main.dSize = Main.dSize + 1;
			}
			// KONEC PRIDANO
		}
		return SCC;
	}
}