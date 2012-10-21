package pal;


import java.util.Comparator;


public class Edge implements Comparable<Edge>{

	double weight;
	int b1;
	int b2;
	
	Edge(int b1, int b2, double p1, double p2){
		this.weight = countCost(getId(b1, b2), p1, p2);
		this.b1 = b1;
		this.b2 = b2;
//		System.out.println(getId(b1, b2));
	}
	Edge(Edge old){
		this.weight = old.weight;
		this.b1 = old.b1;
		this.b2 = old.b2;;
	}
	
	double countCost(double id, double p1, double p2){		
		return (p1*id)%p2;
	}
	
	int getId(int b1, int b2){
		return 1+Math.min(b1,b2)+(Math.max(b1,b2)*(Math.max(b1, b2) - 1)/2);
	}

	

	@Override
	public int compareTo(Edge o) {
//		if(this.weight < o.weight) return - 1;
//		else if(this.weight == o.weight) return 0;
//		else return 1;
		return (int) (this.weight-o.weight);
	}

}
