package aggregators;

import Jama.Matrix;
import ranking.RankedItem;
import ranking.Ranking;

import java.util.ArrayList;
import java.util.List;

import Jama.EigenvalueDecomposition;

public abstract class MarkovChainAggregator implements Aggregator{
	
	@Override
	public Ranking aggregate(List<Ranking> rankings) {
		List<RankedItem> uniqueItems = getUniqueItems(rankings);
	    int N = uniqueItems.size();
	    if(N>0){
		    double[][] transition = getTransitionMatrix(uniqueItems,rankings);
		    //checkTransitionMatrix(transition, N);
			Matrix stationaryMatrix = computeUsingXIterations(N, transition, 50);	
	        for(int i = 0; i<stationaryMatrix.getRowDimension(); i++){
	        	System.out.println(stationaryMatrix.get(i, 0));	
	        }
			return getRankingFromMatrix(uniqueItems, stationaryMatrix);
	    }else{
	    	return new Ranking(uniqueItems);
	    }
	}
	
	private Matrix computeMatrix(int N, double[][] transition) {
	    Matrix stationaryMatrix = null;
	    if(stationaryMatrix == null){
		    try{
		    	stationaryMatrix = computeByFindingEigenvector(N, transition);
		    }catch(Exception e){
		    	
		    }finally{
			    if(stationaryMatrix == null){
			    	System.out.println("Error computing matrix with Eigenvector");
			    	try{
			    		stationaryMatrix = computeBySolvingLinearSystemEquations(N, transition);	
			    	}catch(Exception e){
			    		
			    	}finally{
			    		System.out.println("Error computing matrix with LinearSystemEquations");
			    	    if(stationaryMatrix == null){
			    	    	try{
			    	    		stationaryMatrix = computeUsingXIterations(N, transition,50);	
			    	    	}catch(Exception e){
			    	    		
			    	    	}finally{
			    	    		if(stationaryMatrix == null){
			    	    			System.out.println("Error computing matrix with  XIterations");
			    	    		}
			    	    		
			    	    	}
			    	    }
			    		
			    	}
			 
			    }
		    }
	    }
	    return stationaryMatrix;
	}
	
    public Matrix computeUsingXIterations(int N, double[][] transition, int iter){
        // compute using x number iterations of power method
        Matrix A = new Matrix(transition);
        A = A.transpose();
        Matrix x = new Matrix(N, 1, 1.0 / N); // initial guess for eigenvector
        for (int i = 0; i < iter; i++) {
            x = A.times(x);
            x = x.times(1.0 / x.norm1());       // rescale
        }
        return x;
    }
    
    public Matrix computeByFindingEigenvector(int N, double[][] transition){   
	    // compute by finding eigenvector corresponding to eigenvalue = 1
        Matrix A = new Matrix(transition);
        A = A.transpose();
	    EigenvalueDecomposition eig = new EigenvalueDecomposition(A);
	    Matrix V = eig.getV();
	    double[] real = eig.getRealEigenvalues();
	    for (int i = 0; i < N; i++) {
	        if (Math.abs(real[i] - 1.0) < 1E-6) {
	        	Matrix x = V.getMatrix(0, N-1, i, i);
	            x = x.times(1.0 / x.norm1());
	            return x;
	        }
	    }
		return null;
    }
    
    public Matrix computeBySolvingLinearSystemEquations(int N, double[][] transition){   
	    // If ergordic, stationary distribution = unique solution to Ax = x
	    // up to scaling factor.
	    // We solve (A - I) x = 0, but replace row 0 with constraint that
	    // says the sum of x coordinates equals one
        Matrix A = new Matrix(transition);
        A = A.transpose();
	    Matrix B = A.minus(Matrix.identity(N, N));
	    for (int j = 0; j < N; j++)
	        B.set(0, j, 1.0);
	    Matrix b = new Matrix(N, 1);
	    b.set(0, 0, 1.0);
	    Matrix x = B.solve(b);
	    return x;
    }
    
    public Matrix computeUsingAggregateOrdering(int N, double[][] transition){   
	    // If ergordic, stationary distribution = unique solution to Ax = x
	    // up to scaling factor.
	    // We solve (A - I) x = 0, but replace row 0 with constraint that
	    // says the sum of x coordinates equals one
         double[][] result = new double[N][1];
		for(int e=0;e<N;e++){
			double agg = 0.0;
			for(int i=0;i<N;i++){
				agg += transition[i][e];
			}
			result[e][0]=agg;	
		}
		
		return new Matrix(result);
    }


	private boolean checkTransitionMatrix(double[][] transition,int N) {
		for(int i=0;i<N;i++){
			double check = 0.0;
			for(int e=0;e<N;e++){
				check += transition[i][e];
			}
			if(check!=1.0){
				System.out.println("ERRoR: chaeck="+check);
			}else{
				System.out.println("GOOD!");
			}
			
		}
		
		return false;
		
	}

	private Ranking getRankingFromMatrix(List<RankedItem> uniqueItems, Matrix stationaryMatrix) {
        if(stationaryMatrix.getRowDimension()==uniqueItems.size()){
        	List<RankedItem> items = new ArrayList<RankedItem>();
        	for(int i = 0; i<stationaryMatrix.getRowDimension(); i++){
        		RankedItem ri = new RankedItem(uniqueItems.get(i).getItem(),stationaryMatrix.get(i, 0));
            	items.add(ri);
            }
        	return new Ranking(items);
        }	
		return null;
	}

	public List<RankedItem> getUniqueItems(List<Ranking> rankings){
		
		List<RankedItem> uniqueItems = new ArrayList<RankedItem>();
		
		for(Ranking r:rankings){
			for(RankedItem ri:r.getRankingList()){
				if(!uniqueItems.contains(ri)){
					uniqueItems.add(ri);
				}
			}
		}
		
		return uniqueItems;
		
	}

	public abstract double[][] getTransitionMatrix(List<RankedItem> uniqueItems, List<Ranking> rankings);



}
