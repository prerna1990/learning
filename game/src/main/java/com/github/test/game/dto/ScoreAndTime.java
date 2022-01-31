package com.github.test.game.dto;

import java.sql.Timestamp;

public class ScoreAndTime {
	private Integer score;
	private Timestamp time;

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
