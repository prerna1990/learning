package com.github.test.game.dto;

import java.util.List;

public class PlayerHistoryRS {

	private ScoreAndTime topScore;
	private ScoreAndTime lowScore;
	private Double averageScore;

	private List<ScoreAndTime> scoreList;

	public ScoreAndTime getTopScore() {
		return topScore;
	}

	public void setTopScore(ScoreAndTime topScore) {
		this.topScore = topScore;
	}

	public ScoreAndTime getLowScore() {
		return lowScore;
	}

	public void setLowScore(ScoreAndTime lowScore) {
		this.lowScore = lowScore;
	}

	public Double getAverageScore() {
		return averageScore;
	}

	public void setAverageScore(Double averageScore) {
		this.averageScore = averageScore;
	}

	public List<ScoreAndTime> getScoreList() {
		return scoreList;
	}

	public void setScoreList(List<ScoreAndTime> scoreList) {
		this.scoreList = scoreList;
	}

}
