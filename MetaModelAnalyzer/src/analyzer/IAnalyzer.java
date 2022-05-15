package analyzer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.*;

public abstract class IAnalyzer {	
	
	protected AnalysisResults ar = new AnalysisResults();
	
	public abstract void printSummary();
	public abstract int analyze(TreeIterator<EObject> iterator,ArrayList<EClass> eclasses);
	
	public static void getAllClasses(EList<EObject> contents, ArrayList<EClass> eclasses, Integer packageHash) {
		for (EObject content : contents) {	
			if(content.getClass() == EClassImpl.class) {
				eclasses.add((EClass)content);
			}
			else if (content.getClass() == EPackageImpl.class) {
				int ph = content.hashCode();
				if(packageHash == null || ph != packageHash) {
					getAllClasses(((EPackageImpl)content).eContents(), eclasses, ph);					
				}
//				System.out.println("lol: " + ((EPackageImpl)content).hashCode());								
			}
			else if (content.getClass() == EEnumImpl.class) { }
			else if (content.getClass() == EDataTypeImpl.class) { }
			else if (content.getClass() == EAnnotationImpl.class) { }	
			else if (content.getClass() == EGenericTypeImpl.class) { }	
			else if (content.getClass() == EFactoryImpl.class) { }	
			else if (content.getClass() == ETypeParameterImpl.class) { }	
			else if (content.getClass() == EParameterImpl.class) { }	
			else if (content.getClass() == EAttributeImpl.class) { }	
			else if (content.getClass() == EOperationImpl.class) { }
			else if (content.getClass() == EEnumLiteralImpl.class) { }
			else if (content.getClass() == EReferenceImpl.class) { }
			else if (content.getClass() == EStringToStringMapEntryImpl.class) { }
			else {
				System.out.println("wow: " + content.getClass());
			}
		}
	}
}
