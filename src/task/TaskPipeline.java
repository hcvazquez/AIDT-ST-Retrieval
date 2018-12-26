package task;

public class TaskPipeline {

	public static void main(String[] args) {
		
		AcquireData.main(args);
		ProcessData.main(args);
		AggregateData.main(args);
		AnalyzeRankingItems.main(args);
		EvaluateRankings.main(args);
		//Show Ranking Results - Implemented as a iPython notebook (notebooks/ShowResultsipynb)

	}

}
