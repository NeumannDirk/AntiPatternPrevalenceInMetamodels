package hypergraph;

import java.util.ArrayList;
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
		counts.forEach((k,v) -> {System.out.println(v + "x " + k);});
		ArrayList<HyperNode> xx = new ArrayList<HyperNode>(this.nodes);
		
		return 0;
	}
}
