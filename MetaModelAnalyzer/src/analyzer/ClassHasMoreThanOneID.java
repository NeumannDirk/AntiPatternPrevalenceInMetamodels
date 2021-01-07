package analyzer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

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

public class ClassHasMoreThanOneID implements IAnalyzer{

	private int more_than_one_id = 0;

	@Override
	public void printSummary() {
		System.out.println("Models with more than one ID in one EClass: " + this.more_than_one_id);		
	}

	@Override
	public void analyze(EList<EObject> models) {		
		for (EObject model : models) {
			EList<EObject> eobjects = model.eContents();
			for (EObject eo : eobjects) {
				if (eo.getClass() == EClassImpl.class) {
					EClass eclass = (EClass) eo;
					System.out.println(eclass.getName());
					int numberOfIDs = 0;
					for (EAttribute att : eclass.getEAllAttributes()) {
						if (att.isID()) {
							System.out.println("\t\"" + att.getName() + "\" is an ID!");
							numberOfIDs++;
						}
					}
					for (EClass e : eclass.getEAllSuperTypes()) {
//						System.out.println("\t\"" + e.getName() + "\" is an supertype!");						
					}
					if (numberOfIDs > 1) {
						this.more_than_one_id++;
						return;
					}
				}
			}
		}
	}
}
