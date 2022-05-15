package hypergraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EDataTypeImpl;

public class HyperGraph {
	public HashSet<HyperNode> nodes = new HashSet<HyperNode>();
	public ArrayList<HyperEdge> edges = new ArrayList<HyperEdge>();
	public HashSet<HyperNode> additionalNodes = new HashSet<HyperNode>();
	
	public String summary() {
		StringBuilder sb = new StringBuilder()
				.append(this.nodes.size()).append(" Nodes\n")
				.append(this.edges.size()).append(" Edges\n")
				.append("H = ").append(this.calculateEntropy()).append("\n");
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Nodes:\n");
		for(HyperNode node : this.nodes) {
			sb.append(node.toString());
			sb.append("\n");
		}
//		sb.append("\nAdditionalNodes:\n");
//		for(HyperNode node : this.additionalNodes) {
//			sb.append(node.toString());
//			sb.append("\n" + ((EDataTypeImpl)node.originalModelElement).eClass());
//			sb.append("\n" + ((EDataTypeImpl)node.originalModelElement).getClass());
//			sb.append("\n" + ((EDataTypeImpl)node.originalModelElement).getName());
//			sb.append("\n" + ((EDataTypeImpl)node.originalModelElement).getInstanceClassName());
//			sb.append("\n" + ((EDataTypeImpl)node.originalModelElement).getInstanceClassNameGen());
//			sb.append("\n" + ((EDataTypeImpl)node.originalModelElement).getInstanceTypeName());
//			
//			sb.append("\n");
//		}
		sb.append("\nEdges:\n");
		for(HyperEdge edge : this.edges) {
			sb.append(edge.toString());
			sb.append("\n");
		}
		return sb.toString();
	}

	public HyperNode tryFindNode(EObject searchedFor) {
//		System.out.println("looking for: " + searchedFor);
		for(HyperNode node : this.nodes) {
//			System.out.println("found: " + node.originalModelElement);
			if(node.originalModelElement == searchedFor) {				
				return node;
			}
		}
		for(HyperNode node : this.additionalNodes) {
			if(node.isNodeOf(searchedFor)) {
				return node;
			}
		}
		return null;
		
	}
	
	public double calculateEntropy() {		
		Map<Object, Long> counts = this.edges.stream().collect(Collectors.groupingBy(e -> e.nodes, Collectors.counting()));
//		System.out.println("counts of edges");
//		counts.forEach((k,v) -> {System.out.println(v + "x " + k);});
		ArrayList<HyperNode> xx = new ArrayList<HyperNode>(this.nodes);
		
		Map<HyperNode, ArrayList<HyperEdge>> table = new HashMap<HyperNode, ArrayList<HyperEdge>>();
		for(HyperNode node : this.nodes) {
			table.put(node, new ArrayList<HyperEdge>());
			for(HyperEdge edge : this.edges) {
				if(edge.nodes.contains(node)) {
					table.get(node).add(edge);
				}
			}
		}
		
//		System.out.println("\n\nweights of edges out of " + this.nodes.size());
		Map<Object, Long> weights = table.values().stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
//		weights.forEach((k,v) -> {System.out.println(v + "x " + k);});	
		
		ArrayList<Double> rows = new ArrayList<Double>();
		weights.forEach((k,v) -> {
			double weight = v*1.0 / this.nodes.size();
			rows.add(weight * (-Math.log(weight)));
		});
		double entropy = rows.stream().reduce(0.0d, (a,b) -> a + b);
//		System.out.println(entropy);
				
		return entropy;
	}
}
