package analyzer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

public class MultiplePossibleContainers extends IAnalyzer {

	@Override
	public void printSummary() {
		// TODO Auto-generated method stub

	}

	private ArrayList<EReference> allContainments(ArrayList<EClass> eClasses) {
		ArrayList<EReference> ret = new ArrayList<EReference>();		
		for(EClass ec : eClasses) {
			ret.addAll(ec.getEReferences().stream()
					.filter(x -> x.getEReferenceType() != null)
					.filter(x -> eClasses.contains(x.getEReferenceType()))
					.filter(x -> x.isContainment())
					.collect(Collectors.toList())
			);			
		}
		return ret;
	}
	
	@Override
	public int analyze(TreeIterator<EObject> iterator, ArrayList<EClass> eclasses) {
		ArrayList<EReference> allRefs = this.allContainments(eclasses);
		HashSet<EClass> eClassesWithMultipleContainers = new HashSet<EClass>();
		
		for(EReference ref1 : allRefs) {
			for(EReference ref2 : allRefs) {
				if(ref1 != ref2 ) {
					if (ref1.getEReferenceType() == ref2.getEReferenceType()) {
						eClassesWithMultipleContainers.add(ref1.getEReferenceType());
					}
				}
			}
		}
		
		return eClassesWithMultipleContainers.size();
	}

}
