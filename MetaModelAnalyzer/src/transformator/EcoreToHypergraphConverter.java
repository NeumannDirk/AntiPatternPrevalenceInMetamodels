package transformator;

import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EAnnotationImpl;
import org.eclipse.emf.ecore.impl.EAttributeImpl;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.impl.EClassifierImpl;
import org.eclipse.emf.ecore.impl.EDataTypeImpl;
import org.eclipse.emf.ecore.impl.EEnumImpl;
import org.eclipse.emf.ecore.impl.EEnumLiteralImpl;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.impl.EGenericTypeImpl;
import org.eclipse.emf.ecore.impl.ENamedElementImpl;
import org.eclipse.emf.ecore.impl.EOperationImpl;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.impl.EParameterImpl;
import org.eclipse.emf.ecore.impl.EReferenceImpl;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.impl.ETypeParameterImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

import helper.MetaModelHelper;
import hypergraph.HyperEdge;
import hypergraph.HyperGraph;
import hypergraph.HyperNode;

public class EcoreToHypergraphConverter {

	public static void main(String[] args) {
		System.out.println("Start");
		String dummyInput = "D:\\Repositories\\MyEcore\\model\\myEcore.ecore";		
		Resource metaModel = getMetaModel(dummyInput);		
		HyperGraph hypergraph = createHypergraph(metaModel);
		System.out.println("\n\n" + hypergraph);
		hypergraph.calculateEntropy();
	}

	private static HyperGraph createHypergraph(Resource metaModel) {
		HyperGraph hypergraph = new HyperGraph();  
		EList<EObject> emodels = metaModel.getContents();

		if (emodels.size() > 0) {
			ArrayList<EObject> eclasses = MetaModelHelper.getClasses(metaModel);
			for(EObject potentialHyperNode : eclasses) {
				HyperNode hyperNode = new HyperNode(potentialHyperNode);
				hypergraph.nodes.add(hyperNode);
//				System.out.println(hyperNode);
			}
		}
		
		if (hypergraph.nodes.size() > 0) {
			for(HyperNode hyperNode : hypergraph.nodes) {
//				System.out.println("\n\n\n" + hyperNode);
				Class hyperNodeClass = hyperNode.originalModelElement.getClass();
				if(hyperNodeClass == EEnumImpl.class) {
					continue;
				} else if (hyperNodeClass == EDataTypeImpl.class) {
					continue;
				} else if (hyperNodeClass == EGenericTypeImpl.class) {
					System.out.println("Mal schauen was hier noch kommt!");
				} else {					
					EClass nodeAsClass = (EClass) hyperNode.originalModelElement;

					//check for supertypes
					//getEAllGenericSuperTypes() also returns superclasses of superclasses 
					EList<EGenericType> superTypes = nodeAsClass.getEGenericSuperTypes();
					if (superTypes.size() > 0) {
						HyperEdge hyperEdge = HyperEdge.tryCreateHyperEdge(hypergraph, hyperNode, superTypes);
						if(hyperEdge != null) {							
							hypergraph.edges.add(hyperEdge);
						}
					}
//					for(EGenericType superType : superTypes) {
//						HyperEdge hyperEdge = new HyperEdge(hyperNode, superType);
//					}
					
					//check for attributes
					//getEAllAttributes also returns derived
					EList<EAttribute> attributes = nodeAsClass.getEAttributes();
					for(EAttribute eatt : attributes) {
						HyperEdge hyperEdge = HyperEdge.tryCreateHyperEdge(hypergraph, hyperNode, eatt);
						if(hyperEdge != null) {							
							hypergraph.edges.add(hyperEdge);
						}
					}
					
					//check for references
					//getEAllReferences also returns derived
					EList<EReference> references = nodeAsClass.getEReferences();
					for(EReference eref : references) {
						HyperEdge hyperEdge = HyperEdge.tryCreateHyperEdge(hypergraph, hyperNode, eref);
						if(hyperEdge != null) {							
							hypergraph.edges.add(hyperEdge);
						}
					}
					
					//check for operations
					//getEAllOperations also returns derived
					EList<EOperation> operations = nodeAsClass.getEOperations();
					for(EOperation eop : operations) {
						System.out.println(nodeAsClass.getName() + "::" + eop.getName());
						for(EParameter epa : eop.getEParameters()) {
							System.out.println("\t" + epa.getName() + " : " + epa.getEType());
							System.out.println("\t" + epa.getName() + " : " + epa.getEGenericType().getEClassifier().getName());
						}
						HyperEdge hyperEdge = HyperEdge.tryCreateHyperEdge(hypergraph, hyperNode, eop);
						System.out.println("\t--> " + hyperEdge + "\n");
						if(hyperEdge != null) {				
							hypergraph.edges.add(hyperEdge);
						}
					}
				}
				
//				TreeIterator<EObject> t = n.originalModelElement.eAllContents();
//				
//				while (t.hasNext()) {
//					EObject eo = t.next();
//					System.out.println(eo);
//					if(eo instanceof EGenericTypeImpl) {
//						EGenericTypeImpl abc = (EGenericTypeImpl)eo;
//						EClassifier ec = abc.getEClassifier();
//						System.out.println(ec.getName());
////						EClassifierImpl eci = (EClassifierImpl)ec;
////						EClass ecc = (EClass) eci;
//					}
////					if (eo instanceof ENamedElementImpl){
////						System.out.println(((ENamedElementImpl)eo).getName());
////					}
//				}
				
			}
		}
		return hypergraph;
	}

	private static Resource getMetaModel(String inputFile) {
		EList<EObject> emodels = null;
		Resource myMetaModel = null;
		try {
			ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
					new EcoreResourceFactoryImpl());
			myMetaModel = resourceSet.getResource(URI.createFileURI(inputFile), true);
			
		} catch (Exception e) {
			System.out.println("Not readable!");
		}
		return myMetaModel;
	}
	
	
}
