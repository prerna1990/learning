package com.github.test.game.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.test.game.customizer.DateSeralizeCustomizer;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "playerName", "score", "startTime", "endTime", "pageNo", "pageSize" })
public class ConditionRQ {
	@JsonProperty("id")
	private Integer id;

	@JsonProperty("playerName")
	private String playerName;

	@JsonProperty("playerNames")
	private List<String> playerNames;

	@JsonProperty("score")
	private Integer score;

	@JsonSerialize(using = DateSeralizeCustomizer.class)
	@JsonProperty("startTime")
	private Timestamp startTime;

	@JsonSerialize(using = DateSeralizeCustomizer.class)
	@JsonProperty("endTime")
	private Timestamp endTime;

	@JsonProperty("pageNo")
	private Integer pageNo;

	@JsonProperty("pageSize")
	private Integer pageSize;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public List<String> getPlayerNames() {

		if (playerNames == null) {
			playerNames = new ArrayList<String>();
		}
		return this.playerNames;

	}

	public void setPlayerNames(List<String> playerNames) {
		this.playerNames = playerNames;
	}
}
