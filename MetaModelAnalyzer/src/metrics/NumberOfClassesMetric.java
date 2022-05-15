package metrics;

import java.util.ArrayList;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import analyzer.IAnalyzer;

public class NumberOfClassesMetric extends IAnalyzer {

	@Override
	public void printSummary() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int analyze(TreeIterator<EObject> iterator, ArrayList<EClass> eclasses) {
		return eclasses.size();
	}

}
