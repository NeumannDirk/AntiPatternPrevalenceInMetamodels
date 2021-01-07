package analyzer;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

public interface IAnalyzer {
	public void printSummary();
	public void analyze(EList<EObject> models);
}
