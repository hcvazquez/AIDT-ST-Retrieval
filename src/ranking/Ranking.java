package ranking;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.math3.stat.correlation.KendallsCorrelation;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

public class Ranking implements Serializable {

	private static final long serialVersionUID = 1L;

	protected List<RankedItem> rankingList;
	protected List<Double> k_precision;
	protected List<Double> k_recall;
	protected List<Double> k_fMeasure;
	
	protected String id;
	
	protected Comparator<RankedItem> comparator = new Comparator<RankedItem>() {
		@Override
		public int compare(RankedItem r1, RankedItem r2) {
			return r1.compareTo(r2) * -1;
		}
	};

	public void setComparator(Comparator<RankedItem> comparator) {
		this.comparator = comparator;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Ranking(List<RankedItem> results) {
		super();
		this.rankingList = results;
		sort();
	}

	public Ranking(String id,String filename) {
		super();
		this.rankingList = loadRankingFromFile(filename);
		this.id = id;
		sort();
	}
	
	public Ranking getRankingAtK(int k){
		sort();
		return new Ranking(rankingList.subList(0, k));
	}

	public List<RankedItem> getRankingList() {
		return rankingList;
	}

	public int size() {
		return rankingList.size();
	}

	public void setRankingList(ArrayList<RankedItem> rankingList) {
		this.rankingList = rankingList;
	}

	public boolean contains(RankedItem item) {
		return rankingList.contains(item);
	}

	public void sort() {
		rankingList.sort(comparator);
	}

	/**
	 * This method need an Order Ranking
	 * 
	 * @return
	 */
	public ArrayList<Double> getKPrecisionList(int k) {

		ArrayList<Double> precisionList = new ArrayList<Double>();

		int sumVerified = 0;
		for (int i = 0; i < k; i++) {
			if (i<rankingList.size() && rankingList.get(i).isHit()) {
				sumVerified++;
			}
			if (sumVerified != 0) {
				precisionList.add((double) sumVerified / (double) (i + 1));
			} else {
				precisionList.add(0.0);
			}

		}

		this.k_precision = precisionList;

		return precisionList;
	}
	
	public ArrayList<Double> getKHitList(int k) {

		ArrayList<Double> hitList = new ArrayList<Double>();

		int sumVerified = 0;
		for (int i = 0; i < k; i++) {
			if (i<rankingList.size() && rankingList.get(i).isHit()) {
				sumVerified++;
			}
			if (sumVerified != 0) {
				hitList.add((double)sumVerified);
			} else {
				hitList.add(0.0);
			}

		}

		//this.k_hits = hitList;  TODO

		return hitList;
	}

	public Double getKMaxPrecision(int k) {
		if (k_precision == null) {
			k_precision = getKPrecisionList(k);
		}
		Double max = 0.0;
		for (Double d : k_precision) {
			if (d > max) {
				max = d;
			}
		}
		return max;
	}

	public ArrayList<Double> getKRecallList(int k, int goldenSize) {

		ArrayList<Double> recallList = new ArrayList<Double>();

		int sumVerified = 0;
		for (int i = 0; i < k; i++) {
			if (i<rankingList.size() && rankingList.get(i).isHit()) {
				sumVerified++;
			}
			if (sumVerified != 0) {
				recallList.add((double) sumVerified / (double) goldenSize);
			} else {
				recallList.add(0.0);
			}

		}

		this.k_recall = recallList;

		return recallList;
	}

	public Double getKMaxRecall(int k, int goldenSize) {
		if (k_recall == null) {
			k_recall = getKRecallList(k, goldenSize);
		}
		Double max = 0.0;
		for (Double d : k_recall) {
			if (d > max) {
				max = d;
			}
		}
		return max;
	}

	// =2*((J4*K4)/(J4+K4))
	public ArrayList<Double> getKFMeasureList(int k, int goldenSize) {

		if (k_precision == null) {
			k_precision = getKPrecisionList(k);
		}

		if (k_recall == null) {
			k_recall = getKRecallList(k,goldenSize);
		}

		ArrayList<Double> fMeasureList = new ArrayList<Double>();

		for (int i = 0; i < k; i++) {
			if (k_precision.get(i).doubleValue() + k_recall.get(i).doubleValue() != 0) {
				fMeasureList.add(
						(double) ((double) 2 * ((k_precision.get(i).doubleValue() * k_recall.get(i).doubleValue())
								/ (k_precision.get(i).doubleValue() + k_recall.get(i).doubleValue()))));
			} else {
				fMeasureList.add(0.0);
			}

		}

		this.k_fMeasure = fMeasureList;

		return fMeasureList;
	}

	public Double getKMaxFMeasure(int k, int goldenSize) {
		if (k_fMeasure == null) {
			k_fMeasure = getKFMeasureList(k,goldenSize);
		}
		Double max = 0.0;
		for (Double d : k_fMeasure) {
			if (d > max) {
				max = d;
			}
		}
		return max;
	}

	public void saveRankingInFile(String fileName) {
		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter(fileName);
			pw = new PrintWriter(fichero);

			for (RankedItem item : rankingList) {
				pw.println(item.getName() + ":" + item.getScore());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(fichero!=null){
					fichero.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

	public List<RankedItem> loadRankingFromFile(String fileName) {

		List<RankedItem> results = new ArrayList<RankedItem>();
		
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(fileName);

			br = new BufferedReader(fr);
			String linea;
			
			while ((linea = br.readLine()) != null) {
				String[] nameAndScore = linea.split(":");
				if (nameAndScore.length == 2) {
					results.add(new RankedItem(nameAndScore[0], Double.valueOf(nameAndScore[1])));
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if(fr!=null){
					fr.close();	
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}	
		}
		return results;
	}

	public Double calculateKSpearmanCorrelation(int k) {
		double[] rank = new double[k];
		double[] golden = new double[k];
		this.fixRank(golden, rank);
		for (int i = 0; i < k; i++) {
			RankedItem item = rankingList.get(i);
			rank[i] = item.getScore();
			golden[i] = (item.isHit()) ? 1 : 0;
		}
		double spearman = new SpearmansCorrelation().correlation(golden, rank);

		return spearman;
	}

	public Double calculateKPearsonsCorrelation(int k) {
		double[] rank = new double[k];
		double[] golden = new double[k];
		this.fixRank(golden, rank);

		for (int i = 0; i < k; i++) {
			RankedItem item = rankingList.get(i);
			rank[i] = item.getScore();
			golden[i] = (item.isHit()) ? 1 : 0;
		}

		PearsonsCorrelation pearsons = new PearsonsCorrelation();

		return pearsons.correlation(golden, rank);
	}

	public Double calculateKKendallsCorrelation(int k) {
		double[] rank = new double[k];
		double[] golden = new double[k];
		this.fixRank(golden, rank);

		for (int i = 0; i < k; i++) {
			RankedItem item = rankingList.get(i);
			rank[i] = item.getScore();
			golden[i] = (item.isHit()) ? 1 : 0;
		}
		double kendalls = new KendallsCorrelation().correlation(golden, rank);

		return kendalls;
	}

	private int countVerified(double[] golden) {
		int sum = 0;
		for (double i : golden) {
			if (i == 1.0) {
				sum++;
			}
		}
		return sum;
	}

	private void fixRank(double[] golden, double[] rank) {
		int countVerified = countVerified(golden);
		for (int i = 0; i < rank.length; i++) {
			if (i < countVerified) {
				rank[i] = 1.0;
			} else {
				rank[i] = 0.0;
			}
		}
	}

}
