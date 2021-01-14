package analyzer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class AnalysisResults {

	// (index,value) = <value> metamodels with <index> classes were found with the
	// antipattern.
	public ArrayList<Integer> pos_agg = new ArrayList<Integer>();
	// (index,value) = <value> metamodels with <index> classes were found without
	// the antipattern.
	public ArrayList<Integer> neg_agg = new ArrayList<Integer>();
	// (index,value) = <value> metamodels with <index> classes were found in total.
	public ArrayList<Integer> all_agg = new ArrayList<Integer>();

	// (index,value) = one metamodel with <value> classes was found with the
	// antipattern.
	public ArrayList<Integer> pos_flat = new ArrayList<Integer>();
	// (index,value) = one metamodel with <value> classes was found without the
	// antipattern.
	public ArrayList<Integer> neg_flat = new ArrayList<Integer>();
	// (index,value) = one metamodel with <value> classes was found in general.
	public ArrayList<Integer> all_flat = new ArrayList<Integer>();

	public ArrayList<Integer> small_pos_flat = new ArrayList<Integer>();
	public ArrayList<Integer> small_neg_flat = new ArrayList<Integer>();
	public ArrayList<Integer> small_all_flat = new ArrayList<Integer>();

	public ArrayList<Integer> medium_pos_flat = new ArrayList<Integer>();
	public ArrayList<Integer> medium_neg_flat = new ArrayList<Integer>();
	public ArrayList<Integer> medium_all_flat = new ArrayList<Integer>();

	public ArrayList<Integer> large_pos_flat = new ArrayList<Integer>();
	public ArrayList<Integer> large_neg_flat = new ArrayList<Integer>();
	public ArrayList<Integer> large_all_flat = new ArrayList<Integer>();

	public void inc(int numberOfClasses) {
		int newindex = numberOfClasses < 0 ? -numberOfClasses : numberOfClasses;
		// aggregrat
		if (numberOfClasses < 0) {
			while (this.neg_agg.size() <= newindex) {
				this.neg_agg.add(0);
			}
			this.neg_agg.set(newindex, this.neg_agg.get(newindex).intValue() + 1);
			this.neg_flat.add(newindex);
		} else if (numberOfClasses > 0) {
			while (this.pos_agg.size() <= newindex) {
				this.pos_agg.add(0);
			}
			this.pos_agg.set(newindex, this.pos_agg.get(newindex).intValue() + 1);
			this.pos_flat.add(newindex);
		}

		while (this.all_agg.size() <= newindex) {
			this.all_agg.add(0);
		}
		this.all_agg.set(newindex, this.all_agg.get(newindex).intValue() + 1);
		this.all_flat.add(newindex);

		// flat
		if (-1000 < numberOfClasses && numberOfClasses < -99) {
			this.large_neg_flat.add(newindex);
			this.large_all_flat.add(newindex);
		} else if (-100 < numberOfClasses && numberOfClasses < -9) {
			this.medium_neg_flat.add(newindex);
			this.medium_all_flat.add(newindex);
		} else if (-10 < numberOfClasses && numberOfClasses < 0) {
			this.small_neg_flat.add(newindex);
			this.small_all_flat.add(newindex);
		} else if (0 < numberOfClasses && numberOfClasses < 10) {
			this.small_pos_flat.add(newindex);
			this.small_all_flat.add(newindex);
		} else if (9 < numberOfClasses && numberOfClasses < 100) {
			this.medium_pos_flat.add(newindex);
			this.medium_all_flat.add(newindex);
		} else if (99 < numberOfClasses && numberOfClasses < 1000) {
			this.large_pos_flat.add(newindex);
			this.large_all_flat.add(newindex);
		}
	}

	public void listToCSV(ArrayList<Integer> list, String csvFile, String h1, String h2, boolean skipZeros) {
		try {
			FileWriter writer = new FileWriter(csvFile);
			StringBuilder sb = new StringBuilder(h1 + "," + h2 + "\n");
			for (int i = 1; i < list.size(); i++) {
				if (!skipZeros || list.get(i).intValue() != 0) {
					sb.append(i + "," + list.get(i).intValue() + "\n");
				}
			}
			writer.write(sb.toString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printGen(String prefix) {
		this.listToCSV(this.pos_agg, prefix + "_pos_agg.csv", "NoClasses", "NoAP", true);
		this.listToCSV(this.neg_agg, prefix + "_neg_agg.csv", "NoClasses", "NoAP", true);
		this.listToCSV(this.all_agg, prefix + "_all_agg.csv", "NoClasses", "NoAP", true);
		this.listToCSV(this.pos_flat, prefix + "_pos_flat.csv", "NoClasses", "NoAP", false);
		this.listToCSV(this.neg_flat, prefix + "_neg_flat.csv", "NoClasses", "NoAP", false);
		this.listToCSV(this.all_flat, prefix + "_all_flat.csv", "NoClasses", "NoAP", false);

		this.listToCSV(this.small_pos_flat, prefix + "_small_pos_flat.csv", "NoClasses", "NoAP", false);
		this.listToCSV(this.small_neg_flat, prefix + "_small_neg_flat.csv", "NoClasses", "NoAP", false);
		this.listToCSV(this.small_all_flat, prefix + "_small_all_flat.csv", "NoClasses", "NoAP", false);
		this.listToCSV(this.medium_pos_flat, prefix + "_medium_pos_flat.csv", "NoClasses", "NoAP", false);
		this.listToCSV(this.medium_neg_flat, prefix + "_medium_neg_flat.csv", "NoClasses", "NoAP", false);
		this.listToCSV(this.medium_all_flat, prefix + "_medium_all_flat.csv", "NoClasses", "NoAP", false);
		this.listToCSV(this.large_pos_flat, prefix + "_large_pos_flat.csv", "NoClasses", "NoAP", false);
		this.listToCSV(this.large_neg_flat, prefix + "_large_neg_flat.csv", "NoClasses", "NoAP", false);
		this.listToCSV(this.large_all_flat, prefix + "_large_all_flat.csv", "NoClasses", "NoAP", false);
		
		System.out.println(prefix);
		System.out.println("this.pos_agg.size() = " + this.pos_agg.size());
		System.out.println("this.neg_agg.size() = " + this.neg_agg.size());
		System.out.println("this.all_agg.size() = " + this.all_agg.size());
		System.out.println("this.pos_flat.size() = " + this.pos_flat.size());
		System.out.println("this.neg_flat.size() = " + this.neg_flat.size());
		System.out.println("this.all_flat.size() = " + this.all_flat.size());
		System.out.println("this.small_pos_flat.size() = " + this.small_pos_flat.size());
		System.out.println("this.small_neg_flat.size() = " + this.small_neg_flat.size());
		System.out.println("this.small_all_flat.size() = " + this.small_all_flat.size());
		System.out.println("this.medium_pos_flat.size() = " + this.medium_pos_flat.size());
		System.out.println("this.medium_neg_flat.size() = " + this.medium_neg_flat.size());
		System.out.println("this.medium_all_flat.size() = " + this.medium_all_flat.size());
		System.out.println("this.large_pos_flat.size() = " + this.large_pos_flat.size());
		System.out.println("this.large_neg_flat.size() = " + this.large_neg_flat.size());
		System.out.println("this.large_all_flat.size() = " + this.large_all_flat.size());

		ArrayList<int[]> boxPlotes = new ArrayList<int[]>();
		boxPlotes.add(getBoxPlotNumbers(this.pos_flat));
//		boxPlotes.add(getBoxPlotNumbers(this.all_flat));
		boxPlotes.add(getBoxPlotNumbers(this.large_pos_flat));
//		boxPlotes.add(getBoxPlotNumbers(this.large_all_flat));
		boxPlotes.add(getBoxPlotNumbers(this.medium_pos_flat));
//		boxPlotes.add(getBoxPlotNumbers(this.medium_all_flat));
		boxPlotes.add(getBoxPlotNumbers(this.small_pos_flat));
//		boxPlotes.add(getBoxPlotNumbers(this.small_all_flat));
		
		try {
			StringBuilder sb = new StringBuilder("index,lw,lq,me,uq,uw\n");
			for (int i = 0; i < boxPlotes.size(); i++) {
				int[] bp = boxPlotes.get(i);
				sb.append(i+",");
				for(int j = 0; j < bp.length; j++) {
					sb.append(bp[j]);
					if(j != bp.length-1) {
						sb.append(",");
					}
				}
				sb.append("\n");
			}
			FileWriter boxwriter = new FileWriter(prefix + "_boxplots.csv");
			boxwriter.write(sb.toString());
			boxwriter.flush();
			boxwriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int[] getBoxPlotNumbers(ArrayList<Integer> v) {
//		for(int i = 0; i < v.size();i++) {
//			System.out.print(v.get(i)+"_");
//		}
//		System.out.println();
		if(v.size()==0) {
			return new int[] {-1,-1,-1,-1,-1};
		}
		else {
			Collections.sort(v);
			return new int[] { v.get(0), perc(v, 25), perc(v, 50), perc(v, 75), v.get(v.size()-1) };	
		}
	}

	public static int perc(ArrayList<Integer> sortedValues, double percentile) {
		int index = ((int) Math.ceil((percentile / 100.0) * sortedValues.size()))-1;
		if(index < 0 || index >= sortedValues.size()) {
			return -1;
		}
		return sortedValues.get(index);
	}
}
