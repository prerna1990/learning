package com.github.test.game.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.github.test.game.domain.Score;
import com.github.test.game.dto.ConditionRQ;
import com.github.test.game.dto.PlayerHistoryRS;
import com.github.test.game.dto.ScoreAndTime;
import com.github.test.game.repository.PlayerRepository;

@Service
public class GameService {

	@Autowired
	private PlayerRepository repository;

	public String deleteById(Integer id) throws Exception {
		try {
			repository.deleteById(id);
		} catch (Exception e) {
//			return "Id not found " + id;
			throw new Exception("Id not found " + id);
		}
		return "Ok";
	}

	public List<Score> scoreList(ConditionRQ conditionRq) {
		String name = conditionRq.getPlayerName();
		if (name != null && name.trim().length() > 0) {
			return repository.findByPlayerNameAndScore(conditionRq.getPlayerName(), conditionRq.getScore());
		} else {
			if (conditionRq.getScore() != null) {
				return repository.findByScore(conditionRq.getScore());
			} else {
				if (conditionRq.getId() != null) {
					return repository.findByScore(conditionRq.getId());
				}
			}
		}
		return new ArrayList<>();
	}

	public Score save(Score score) {
		if (score.getScore() != null && score.getScore() > 0) {
			return repository.save(score);
		}
		return score;

	}

	public Optional<Score> findByScoreId(Integer scoreId) {
		return repository.findById(scoreId);
	}

	public Iterable<Score> saveAll(List<Score> players) {
		return repository.saveAll(players);
	}

	public List<Score> scoreListPage(ConditionRQ conditionRq) {
		//Default size if no inputs received.
		int pageNo = (conditionRq.getPageNo() != null) ? conditionRq.getPageNo() : 1;
		int pageSize = (conditionRq.getPageSize() != null) ? conditionRq.getPageSize() : 3;

		Timestamp startTime = getStartOfDay(conditionRq.getStartTime());
		Timestamp endTime = getEndOfDay(conditionRq.getEndTime());
		Pageable sortedByName = PageRequest.of(pageNo, pageSize, Sort.by("playerName").and(Sort.by("time")));

		/*
		 * "Get all scores by playerX" "Get all score after 1st November 2020"
		 * "Get all scores by player1, player2 and player3 before 1st december 2020"
		 * "Get all scores after 1 Jan 2020 and before 1 Jan 2021"
		 */
		switch (getTypeOfQuery(conditionRq)) {
		case 1:
			return repository.findAllPlayersForPeriod(conditionRq.getPlayerNames(), endTime, sortedByName);
		case 2:
			return repository.findAllPlayersForPeriod(startTime, endTime, sortedByName);
		case 3:
			return repository.findAllByPlayerName(conditionRq.getPlayerName(), sortedByName);
		case 4:
			return repository.findAllPlayersForPeriod(startTime, sortedByName);
		}
		return new ArrayList<Score>();

	}

	private int getTypeOfQuery(ConditionRQ conditionRq) {

		if (!CollectionUtils.isEmpty(conditionRq.getPlayerNames()) && conditionRq.getEndTime() != null)
			return 1;
		if (conditionRq.getStartTime() != null && conditionRq.getEndTime() != null)
			return 2;
		if (conditionRq.getPlayerName() != null && conditionRq.getPlayerName().trim().length() > 0)
			return 3;
		if (conditionRq.getStartTime() != null)
			return 4;
		return 0;

	}

	public PlayerHistoryRS findHistory(String playerName) {
		List<Score> list = repository.findPlayers(playerName);
		return populate(list);
	}

	private PlayerHistoryRS populate(List<Score> list) {
		PlayerHistoryRS rs = new PlayerHistoryRS();
		IntSummaryStatistics summaryStats = list.stream().mapToInt(n -> n.getScore()).summaryStatistics();
		Optional<Score> min = list.stream().filter(p -> p.getScore() == summaryStats.getMin()).findFirst();
		Optional<Score> max = list.stream().filter(p -> p.getScore() == summaryStats.getMax()).findFirst();
		double averageScore = summaryStats.getAverage();

		if (min != null) {
			ScoreAndTime lowScore = new ScoreAndTime(min.get().getScore(), min.get().getTime());
			rs.setLowScore(lowScore);
		}

		if (max != null) {
			ScoreAndTime topScore = new ScoreAndTime(max.get().getScore(), max.get().getTime());
			rs.setTopScore(topScore);
		}

		rs.setAverageScore(averageScore);
		rs.setScoreList(
				list.stream().map(p -> new ScoreAndTime(p.getScore(), p.getTime())).collect(Collectors.toList()));
		return rs;
	}

	private Timestamp getStartOfDay(Timestamp startTime) {
		if (startTime != null) {
			Calendar cal = getCalendar();
			cal.setTime(new Date(startTime.getTime()));
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			return new Timestamp(cal.getTime().getTime());
		}
		return startTime;

	}

	private Timestamp getEndOfDay(Timestamp endTime) {
		if (endTime != null) {
			Calendar cal = getCalendar();
			cal.setTime(new Date(endTime.getTime()));
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
			return new Timestamp(cal.getTime().getTime());
		}
		return endTime;

	}

	private Calendar getCalendar() {
		return Calendar.getInstance();
	}
}
