package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {

	static int numberOfBuildings;
	static int numberOfNodes;
	static int p1;
	static int p2;
	static double discount;

	static Edge[] graph;
	static Edge[] spanningTree;
	static double cost;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int backbone1 = 0;
		int backbone2 = -1;
		try {
			readFromStdInFast();

			// System.out.print("Edges: " + numberOfBuildings + "\nNodes: "
			// + numberOfNodes + "\np1: " + p1 + "\np2: " + p2
			// + "\ndiscount: " + discount + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// System.out.println("kurva");
			e.printStackTrace();
		}
		// long start = System.currentTimeMillis();
		// System.out.println(discount);
		initGraph();

		// radime
		// start = System.currentTimeMillis();
		// for(Edge e: graph){
		// System.out.print(e.weight+",");
		// }
		// System.out.println();
		Arrays.sort(graph);
		// end = System.currentTimeMillis();

		// System.out.println(end-start);

		// for(Edge e: graph){
		// System.out.print(e.weight+",");
		// }
		// System.out.println();

		// a ted zacneme kruskalovat

		int spanningTreeLength = numberOfBuildings - 1;
		spanningTree = new Edge[spanningTreeLength];
		int components[] = new int[numberOfBuildings];
		int backstop = 0;
		int componentsi = 0;
		for (componentsi = 0; componentsi < numberOfBuildings; componentsi++) {
			components[componentsi] = componentsi;
		}
		cost = 0;
		int edgeIndex = 0;
		int component1, component2;
		while (backstop < spanningTreeLength) {
			component1 = components[graph[edgeIndex].b1];
			component2 = components[graph[edgeIndex].b2];
			if (component1 != component2) {
				cost += graph[edgeIndex].weight;
				System.out.println(graph[edgeIndex].weight);
				for (componentsi = 0; componentsi < numberOfBuildings; componentsi++) {
					if (components[componentsi] == component2)
						components[componentsi] = component1;
				}
				backstop++;
			}
			edgeIndex++;
		}
		// System.out.println("Cost is" + cost);
		Edge discountedEdges[] = new Edge[graph.length];
		int discountArrayCounter;
		double lowestCost = Double.MAX_VALUE;
		int discEdgeIndex;
		Edge tmpEdge;
		if (numberOfNodes == 1) {

			for (int i = 0; i < numberOfBuildings; i++) {
				int j;
				discountArrayCounter = 0;
				int count = graph.length;
				for (j = 0; j < count; j++) {
					if (graph[j].b1 == i || graph[j].b2 == i) {
						discountedEdges[discountArrayCounter] = new Edge(
								graph[j]);
						// System.out.print(graph[j].weight + "  "
						// +graph[j].weight*(discount/100) +"  ");
						discountedEdges[discountArrayCounter].weight = graph[j].weight
								- (graph[j].weight * (discount / 100));
						// System.out.println(discountedEdges[discountArrayCounter].weight);
						discountArrayCounter++;
					}
				}

				backstop = 0;
				for (componentsi = 0; componentsi < numberOfBuildings; componentsi++) {
					components[componentsi] = componentsi;
				}
				double tmpCost = 0;
				edgeIndex = 0;
				discEdgeIndex = 0;

				while (backstop < spanningTreeLength) {
					tmpEdge = (discountedEdges[discEdgeIndex].weight < graph[edgeIndex].weight)
							&& (discountArrayCounter > discEdgeIndex) ? discountedEdges[discEdgeIndex++]
							: graph[edgeIndex++];

					// System.out.println(discEdgeIndex);
					component1 = components[tmpEdge.b1];
					component2 = components[tmpEdge.b2];
					if (component1 != component2) {
						// System.out.println(tmpEdge.weight);
						tmpCost += tmpEdge.weight;

						for (componentsi = 0; componentsi < numberOfBuildings; componentsi++) {
							if (components[componentsi] == component2)
								components[componentsi] = component1;
						}
						backstop++;
					}
					// edgeIndex++;
				}
				// System.out.println(tmpCost);

				if (lowestCost > tmpCost){
					lowestCost = tmpCost;
				backbone1 = i;
				}
				}
		} else {
			int k;
			int i;
			int j;

			int count = graph.length;
			for (i = 0; i < numberOfBuildings; i++) {
				for (k = i + 1; k < numberOfBuildings; k++) {

					discountArrayCounter = 0;
					for (j = 0; j < count; j++) {
						if (graph[j].b1 == i || graph[j].b2 == i
								|| graph[j].b1 == k || graph[j].b2 == k) {
							discountedEdges[discountArrayCounter] = new Edge(
									graph[j]);
							// System.out.print(graph[j].weight + "  "
							// +graph[j].weight*(discount/100) +"  ");
							discountedEdges[discountArrayCounter].weight = graph[j].weight
									- (graph[j].weight * (discount / 100));
							// System.out.println(discountedEdges[discountArrayCounter].weight);
							discountArrayCounter++;
						}
					}
					// System.out.println(discountArrayCounter);

					backstop = 0;
					for (componentsi = 0; componentsi < numberOfBuildings; componentsi++) {
						components[componentsi] = componentsi;
					}
					double tmpCost = 0;
					edgeIndex = 0;

					discEdgeIndex = 0;

					while (backstop < spanningTreeLength) {
						tmpEdge = (discountedEdges[discEdgeIndex].weight < graph[edgeIndex].weight)
								&& (discountArrayCounter > discEdgeIndex) ? discountedEdges[discEdgeIndex++]
								: graph[edgeIndex++];

						// System.out.println(discEdgeIndex);
						component1 = components[tmpEdge.b1];
						component2 = components[tmpEdge.b2];
						if (component1 != component2) {
							// System.out.println(tmpEdge.weight);
							tmpCost += tmpEdge.weight;

							for (componentsi = 0; componentsi < numberOfBuildings; componentsi++) {
								if (components[componentsi] == component2)
									components[componentsi] = component1;
							}
							backstop++;
						}
						// edgeIndex++;
					}
					// System.out.println(tmpCost);

					if (lowestCost > tmpCost){
						lowestCost = tmpCost;
						backbone1 = i;
						backbone2 = k;
					}
				}

			}

		}

		// System.out.println("Cost is: " + lowestCost);
		// long end = System.currentTimeMillis();
		// System.out.println(end - start);
		DecimalFormat df = new DecimalFormat("0.00");
		if (backbone2 == -1) {
			System.out.println(df.format(cost) + " " + df.format(lowestCost)
					+ "\n" + backbone1);
		} else {
			System.out.println(df.format(cost) + " " + df.format(lowestCost)
					+ "\n" + backbone1 + " " + backbone2);
		}

	}

	static void readFromStdInFast() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// read number of nodes:
		StringTokenizer st = new StringTokenizer(br.readLine());
		// System.out.println(st.countTokens());
		numberOfBuildings = Integer.parseInt(st.nextToken());
		numberOfNodes = Integer.parseInt(st.nextToken());
		p1 = Integer.parseInt(st.nextToken());
		p2 = Integer.parseInt(st.nextToken());
		discount = Integer.parseInt(st.nextToken());

	}

	static void initGraph() {
		graph = new Edge[(numberOfBuildings * (numberOfBuildings - 1)) / 2];
		int k = 0;
		int i = 0;
		int j = 0;
		int all = 0;
		for (i = 0; i < numberOfBuildings; i++) {
			for (j = i + 1; j < numberOfBuildings; j++) {
				graph[k++] = new Edge(i, j, p1, p2);
				all += graph[k - 1].weight;

			}
		}
		// System.out.println((int) all);
	}

}
