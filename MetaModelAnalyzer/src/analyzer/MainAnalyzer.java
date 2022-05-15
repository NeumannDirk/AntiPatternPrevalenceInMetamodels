package analyzer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

import org.eclipse.emf.edit.command.AddCommand;

import hypergraph.HyperGraph;
import metrics.CountingComplexity;
import metrics.NumberOfClassesMetric;
import transformator.EcoreToHypergraphConverter;

public class MainAnalyzer {

	StringBuilder sb = new StringBuilder();
	
	public static void main(String[] args) {	
		String dir = System.getProperty("user.dir") + "\\model";
		dir = "D:\\data\\ap_mm";
//		System.out.print(dir);
		ArrayList<String> ecoreFiles = new ArrayList<String>();
		if (true) {
			getAllEcores(new File(dir), ecoreFiles);
//			ecoreFiles = (ArrayList<String>) ecoreFiles.subList(0, 1000);
		} else {
			ecoreFiles = new ArrayList<String>();
			ecoreFiles.add("D:\\Repositories\\MyEcore\\model\\myEcore.ecore");
//			ecoreFiles.add("D:\\data\\own_examples\\myEcore2.ecore");
//			ecoreFiles.add("D:\\data\\own_examples\\myEcore3.ecore");
		}
		System.out.println("Number of meta-models: " + ecoreFiles.size());
		new MainAnalyzer().run(ecoreFiles);
	}

	private static void getAllEcores(File curDir, ArrayList<String> list) {
		File[] filesList = curDir.listFiles();
		for (File f : filesList) {
			if (f.isDirectory())
				getAllEcores(f, list);
			if (f.isFile() && f.getName().endsWith(".ecore")) {
				list.add(f.getAbsolutePath());
			}
		}

	}

	private int not_readable_file = 0;
	private int empty_file = 0;
	private ArrayList<IAnalyzer> analyzers = new ArrayList<IAnalyzer>();
	private ArrayList<IAnalyzer> metrics = new ArrayList<IAnalyzer>();

	private IAnalyzer numClasses = new NumberOfClassesMetric();
	private IAnalyzer countComplex = new CountingComplexity();
	public MainAnalyzer() {		
		this.metrics.add(new NumberOfClassesMetric());
		this.metrics.add(new CountingComplexity());
		
		this.analyzers.add(new ClassHasMoreThanOneID());
		this.analyzers.add(new MME_complete());
		this.analyzers.add(new MME_restricted());
		this.analyzers.add(new Unnamed_complete());
		this.analyzers.add(new Unnamed_noPackage());
		this.analyzers.add(new EnumerationHasAttributes());
		this.analyzers.add(new ReferenceHasNoType());
		this.analyzers.add(new DiamondInheritance());
		this.analyzers.add(new ClassNotInPackageContained());
		this.analyzers.add(new MultiplePossibleContainers());
	}

	String badModels = "";

	public static double roundAvoid(double value, int places) {
	    double scale = Math.pow(10, places);
	    return Math.round(value * scale) / scale;
	}
	
	FileWriter writer;
	private void run(ArrayList<String> ecoreFiles) {
		
		
		
		String header = "Nr.,Test(0=no),#HyperNodes,#HyperEdges,Entropy,#Classes,CountingComplexity,"
				+ "ClassHasMoreThanOneID,MalformedMultiplicityElement_complete,MalformedMultiplicityElement_restricted,UnnamedElement_complete,UnnamedElement_noPackage,"
				+ "EnumerationHasAttributes,ReferenceHasNoType,DiamondInheritance,"
				+ "ClassNotInPackageContained,MultiplePossibleContainers\n";
		try {
			this.writer = new FileWriter("D:\\metamodel_analysis_results.csv");	
			this.writer.write(header);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		sb.append("Number of meta-models: " + ecoreFiles.size());
		ArrayList<Integer> badones = new ArrayList<Integer>();
		badones.add(44803);
		badones.add(44805);
		badones.add(44806);
		badones.add(44809);
		badones.add(44810);
		badones.add(44811);
		
		int containsTest = 0;
		int notContainsTest = 0;
		HashSet<Long> lastModified = new HashSet<Long>();
//		for (int i = 0; i < ecoreFiles.size(); i++) {
		for (int i = 0; i < ecoreFiles.size(); i++) {
			boolean found = false;
			found |= ecoreFiles.get(i).contains("test");
			found |= ecoreFiles.get(i).contains("Test");
			if (found) {
				containsTest++;
			}
//			File file = new File(ecoreFiles.get(i));			
////			System.out.println(file.lastModified());
//			BasicFileAttributes bfa;
//			try {
//				bfa = Files.readAttributes(Paths.get(ecoreFiles.get(i)), BasicFileAttributes.class);
//				System.out.println(bfa.creationTime());
//				System.out.println(bfa.lastAccessTime());
//				System.out.println(bfa.lastModifiedTime());
//			} catch (IOException e) { e.printStackTrace(); }			
//			lastModified.add(file. lastModified());
//		}
//		sb.append("\ncontains \"test\" or \"Test\": " + containsTest);
//		sb.append("\nnot contains \"test\" or \"Test\": " + (ecoreFiles.size() - containsTest));
//		sb.append("\nthat is: " + roundAvoid(containsTest*100.0d/ecoreFiles.size(), 2) + "%");
//		sb.append("\nDifferent last modified dates: " + lastModified.size());
//		System.out.println(sb.toString());
//		for(Long l : lastModified) {
//			System.out.println(new Date(l));
//		}

		

//		for (int i = 0; i < ecoreFiles.size(); i++) {
//		for (int i = 0; i < 250; i++) {
			if (i % 1000 == 0) {
				System.out.println(i);
			}
			String s = ecoreFiles.get(i);
			EList<EObject> emodels = null;
			Resource myMetaModel = null;
			try {
				ResourceSet resourceSet = new ResourceSetImpl();
				resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
						new EcoreResourceFactoryImpl());
				myMetaModel = resourceSet.getResource(URI.createFileURI(s), true);
				emodels = myMetaModel.getContents();
			} catch (Exception e) {
				this.not_readable_file++;
				continue;
			}
			if (emodels.size() == 0) {
				this.empty_file++;
				continue;
			}
			if (badones.contains(i)) {
				this.not_readable_file++;
				badModels += (ecoreFiles.get(i) + "\n");
				continue;
			}
			
			ArrayList<EClass> eclasses = new ArrayList<EClass>();
			IAnalyzer.getAllClasses(emodels, eclasses, null);
			
			HyperGraph hyperGraph = EcoreToHypergraphConverter.createHypergraph(myMetaModel);
//			System.out.println("H(" + i + ") = " + hyperGraph.calculateEntropy());
			
			int ergNumClasses = this.numClasses.analyze(myMetaModel.getAllContents(), eclasses);
			int ergCountComplex = this.countComplex.analyze(myMetaModel. getAllContents(), eclasses);
			
//			for (IAnalyzer analyzer : this.metrics) {
//				analyzer.analyze(myMetaModel.getAllContents(), eclasses);
//			}
			
//			for (IAnalyzer analyzer : this.analyzers) {
//				analyzer.analyze(myMetaModel.getAllContents(), eclasses);
//			}			
			
			


			StringBuilder modelSummary = new StringBuilder()
					.append(i).append(",")
					.append(found ? 1 : 0).append(",")
					.append(hyperGraph.nodes.size()).append(",")
					.append(hyperGraph.edges.size()).append(",")
					.append(roundAvoid(hyperGraph.calculateEntropy(), 3)).append(",")
					.append(ergNumClasses).append(",")
					.append(ergCountComplex).append(",");
//					.append("-----").append(",");
			for (IAnalyzer analyzer : this.analyzers) {
				modelSummary.append(analyzer.analyze(myMetaModel.getAllContents(), eclasses)).append(",");
			}
//			modelSummary.append(ecoreFiles.get(i)).append("\n");
			modelSummary.append("\n");
//			if (i % 100 == 0)
//				System.out.println(modelSummary.toString());
//			modelSummary.append(4711).append("\n");
			
			myMetaModel = null;
			eclasses = null;
			writeToCSV(modelSummary.toString());			
		}
//		System.out.println(modelSummary.toString());
//		printSummary();
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeToCSV(String content) {
		try {			
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void printSummary() {
		for (IAnalyzer analyzer : this.analyzers) {
			analyzer.printSummary();
		}
		System.out.println("not_readable_file" + " = " + this.not_readable_file);
		System.out.println("empty_file" + " = " + this.empty_file);
		System.out.println("bad models with tree-iterator problems:\n" + badModels);
		
		sb.append("\nNot Readable Files" + " = " + this.not_readable_file);
		sb.append("\nEmpty Files" + " = " + this.empty_file);
		sb.append("\nBad Models with tree-iterator problems:\n" + badModels);
		
		try {
			FileWriter console_writer = new FileWriter("results\\console.log");
			console_writer.write(sb.toString());
			console_writer.flush();
			console_writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
