package analyzer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EAttributeImpl;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

public class ClassHasMoreThanOneID extends IAnalyzer {
//	private HashMap<Integer,Integer> results = new HashMap<Integer,Integer>();  

	@Override
	public void printSummary() {	
		this.ar.printGen("id");
	}

	@Override
	public int analyze(TreeIterator<EObject> iterator,ArrayList<EClass> eclasses) {
//		System.out.print("Classes that have more than one ID: ");
		int numberOfAntipatterns = 0;
		if (eclasses.size() > 0) {
			for (EClass ec : eclasses) {
				int numberOfIDs = 0;
				for (EAttribute att : ec.getEAllAttributes()) {
					if (att.isID()) {
						numberOfIDs++;
					}
				}
				if (numberOfIDs > 1) {
					numberOfAntipatterns++;
				}
			}			
		}
		
//		System.out.println(numberOfAntipatterns);
		return numberOfAntipatterns;
	}
}
