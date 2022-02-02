package com.github.test.game.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "topScore", "lowScore", "averageScore" })
public class PlayerHistoryRS {

	@JsonProperty("topScore")
	private ScoreAndTime topScore;
	@JsonProperty("lowScore")
	private ScoreAndTime lowScore;
	@JsonProperty("averageScore")
	private Double averageScore;

	@JsonProperty("scoreList")
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
