package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: simekadam
 * Date: 12/11/12
 * Time: 8:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static final double CONSTANT = Math.pow(10, 6);
    public static double R;
    public static double H;
    public static double M;
    public static double E;
    public static double t;
    public static double N, A, B, C, D, L;
    public static Edge graph[];

    public static void readInput() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            N = Double.parseDouble(br.readLine());
            A = Double.parseDouble(br.readLine());
            B = Double.parseDouble(br.readLine());
            C = Double.parseDouble(br.readLine());
            D = Double.parseDouble(br.readLine());
            L = Double.parseDouble(br.readLine());
        } catch (IOException e) {
            System.out.println("Posralo se cteni");
        }
    }

    public static double R(double t) {
        return (1000000.0 + t * B) * A / 1000000.0;
    }

    public static double H(double U, double t) {
        return (U * C * (1000000.0 + (t * B))) / 1000000.0;
    }

    public static double E(double m, double n, double t) {

        double tmpResult = 0;
//        else tmpResult=0;
        tmpResult = ((t + m + (t * n)) % D) / 1000000.0;

        return tmpResult;
    }

    public static void initGraph() {
        graph = new Edge[((int) N * ((int) N - 1)) / 2];
        int k = 0;
        int i = 0;
        int j = 0;

        for (i = 0; i < N; i++)
            for (j = i + 1; j < N; j++) {
                graph[k] = new Edge(i, j);
                graph[k].weight = i * (j + 1) % D + ((i * j) % (D + 1));
                //graph[k++].weight += E(0,i, j, 0);
                k++;

            }
        // System.out.println((int) all);
    }

    public static void main(String[] args) {

        readInput();
        initGraph();

        t = 0;
        long start = System.currentTimeMillis();
        Arrays.sort(graph);
        while (true) {
            double Hnt = H(N, t);
            double Rt = R(t);
//            System.out.println(t);
            boolean sort = false;
            double tmpweight = 0;
            for (int i = 0; i < graph.length; i++) {
                Edge aGraph = graph[i];
                aGraph.weight += E(aGraph.b1, aGraph.b2, t);
//                if (tmpweight > aGraph.weight)
//                {
//                    Edge tmp = graph[i-1];
//                    graph[i] = tmp;
//                    graph[i-1] = aGraph;
//                }
//                tmpweight = aGraph.weight;


            }

            if (Hnt - Rt > L) {
//                Arrays.sort(graph);
                double cost = spanningTreeCost();
                if (Hnt - Rt - cost > L || t > 1000000.0) {
                    if (t > 1000000.0) System.out.println("NO SOLUTION FOUND!");
                    else System.out.println((int) (t));
                    long end = System.currentTimeMillis();
                    System.out.println(end-start);
                    break;
                }
            }
            t++;
        }

    }

    public static double spanningTreeCost() {
        double cost;
        double spanningTreeLength = N - 1;
        double components[] = new double[(int) N];
        double backstop = 0;
        double componentsi = 0;
        for (componentsi = 0; componentsi < N; componentsi++) {
            components[((int) componentsi)] = componentsi;
        }
        cost = 0;
        int edgeIndex = 0;
        double component1, component2;
        while (backstop < spanningTreeLength) {
            component1 = components[graph[(edgeIndex)].b1];
            component2 = components[graph[(edgeIndex)].b2];
            if (component1 != component2) {
                cost += graph[(edgeIndex)].weight;
                for (componentsi = 0; componentsi < N; componentsi++) {
                    if (components[((int) componentsi)] == component2)
                        components[((int) componentsi)] = component1;
                }
                backstop++;
            }
            edgeIndex++;
        }

        return cost;

    }

}

class Edge implements Comparable<Edge> {

    double weight;
    int b1;
    int b2;

    Edge(int b1, int b2) {

        this.b1 = b1;
        this.b2 = b2;
//		System.out.println(getId(b1, b2));
    }



    @Override
    public int compareTo(Edge o) {
        if (this.weight < o.weight) return -1;
        else if (this.weight == o.weight) return 0;
        else return 1;
//        return (int) (o.weight - this.weight);
    }

}