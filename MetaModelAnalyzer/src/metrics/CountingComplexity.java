package metrics;

import java.util.ArrayList;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

import analyzer.IAnalyzer;

public class CountingComplexity extends IAnalyzer {

	@Override
	public void printSummary() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int analyze(TreeIterator<EObject> iterator, ArrayList<EClass> eclasses) {
		int countingComplexity = 0;
		
		//Definition of hyperedges? 
		for(EClass eclass : eclasses) {
//			eclass.getEAllAttributes()
//			eclass.getEAllContainments
//			eclass.getEAllReferences()
			countingComplexity += eclass.getEAllStructuralFeatures().size();
			
//			eclass.getEAllSuperTypes()
			countingComplexity += eclass.getEAllGenericSuperTypes().size();
			
			//?
			countingComplexity += eclass.getEAllOperations().size();
//			for(EOperation eo : eclass.getEAllOperations()) {
//				System.out.println(eo);				
//			}
		}
		
		
		System.out.println("CountingComplexity: " + countingComplexity);
		return countingComplexity;
	}

}
