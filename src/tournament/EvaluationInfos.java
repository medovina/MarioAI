package tournament;

import java.util.ArrayList;
import java.util.List;

public class EvaluationInfos {
	
	private List<EvaluationInfo> results = new ArrayList<EvaluationInfo>();
	
	public int totalVictories;
	public double avgVictories;
	public int totalDeaths;
	public double avgDeaths;
	public int totalTimedout;
	public double avgTimedout;
	
	/**
	 * In seconds; if the simulation runs headless, the time is approximated from the number of simulation frames.
	 */
	public int totalTimeSpent;
    public double avgTimeSpent;
    
    public double totalDistance;
    public double avgDistance;

    public int totalScore;
    public double avgScore;
		
	public List<EvaluationInfo> getResults() {
		return results;
	}

	public void addResult(EvaluationInfo result) {		
		switch(result.getResult()) {
		case LEVEL_TIMEDOUT:
			++totalTimedout;
			break;
		case MARIO_DIED:
			++totalDeaths;
			break;
		case VICTORY:
			++totalVictories;
			break;
		default:
			// IGNORING
			return;
		}
		
		results.add(result);
		
        totalTimeSpent += result.timeSpent;
        totalDistance += result.completionFraction();
        totalScore += result.score;
		
		avgVictories = (double) totalVictories / results.size();
		avgDeaths    = (double) totalDeaths   / results.size();
        avgTimedout  = (double) totalTimedout / results.size();
        
        avgTimeSpent = (double) totalTimeSpent / results.size();
        avgDistance = totalDistance / results.size();
        avgScore = (double) totalScore / results.size();
	}
	
	public void addResults(List<EvaluationInfo> results) {
		for (EvaluationInfo info : results) {
			addResult(info);
		}
	}

	public int getTotalRuns() {
		return totalDeaths + totalTimedout + totalVictories;
	}
	
	public String getCSVHeader() {
        return "games;victories;victoryRate;deaths;deathRate;" +
               "timeouts;timeoutRate;avgDistance;avgScore";
	}
	
	public String getCSV() {
        return String.format("%d;%d;%.4f;%d;%.4f;%d;%.4f;%.4f;%.2f",
            results.size(), totalVictories, avgVictories, totalDeaths, avgDeaths,
			totalTimedout, avgTimedout, avgDistance, avgScore);
	}
	
	@Override
	public String toString() {
        return String.format(
            "won = %d (%.1f%%), died = %d (%.1f%%), timed out = %d (%.1f%%), avg distance = %.1f%%, avg score = %.1f",
            totalVictories, 100 * avgVictories,
            totalDeaths, 100 * avgDeaths,
            totalTimedout, 100 * avgTimedout, 100 * avgDistance, avgScore);
	}
	
}
