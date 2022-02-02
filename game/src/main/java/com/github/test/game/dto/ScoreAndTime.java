package com.github.test.game.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "score", "time" })
public class ScoreAndTime {
	@JsonProperty("score")
	private Integer score;
	@JsonProperty("time")
	private Timestamp time;

	public ScoreAndTime() {
		
	}
	public ScoreAndTime(Integer score, Timestamp time) {
		this.score = score;
		this.time = time;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}
}
