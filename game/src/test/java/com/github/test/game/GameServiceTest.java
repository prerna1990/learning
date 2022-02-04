package com.github.test.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.test.game.domain.Score;
import com.github.test.game.dto.ConditionRQ;
import com.github.test.game.dto.PlayerHistoryRS;
import com.github.test.game.repository.PlayerRepository;
import com.github.test.game.service.GameService;

public class GameServiceTest {

	@Mock
	private PlayerRepository repository;

	@InjectMocks
	private GameService gameService = new GameService();

	@BeforeMethod
	public void init() {
		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void testDeleteById() throws Exception {

		Mockito.doNothing().when(repository).deleteById(any(Integer.class));
		gameService.deleteById(2);
		verify(repository).deleteById(any(Integer.class));

	}

	@Test
	public void testScoreListByName() throws Exception {

		ConditionRQ conditionRq = new ConditionRQ();
		conditionRq.setPlayerName("Prerna Mishra");
		conditionRq.setScore(12);

		List<Score> expectedCreateScoreList = createScoreList(jsonNameAndScore);

		when(repository.findByPlayerNameAndScore(conditionRq.getPlayerName(), conditionRq.getScore()))
				.thenReturn(expectedCreateScoreList);

		List<Score> actualScoreList = gameService.scoreList(conditionRq);
		assertNotNull(actualScoreList);

		assertEquals(expectedCreateScoreList, actualScoreList);

		verify(repository).findByPlayerNameAndScore(conditionRq.getPlayerName(), conditionRq.getScore());

	}

	@Test
	public void testScoreListByScore() throws Exception {

		ConditionRQ conditionRq = new ConditionRQ();
		// conditionRq.setPlayerName("Prerna Mishra");
		conditionRq.setScore(12);

		List<Score> expectedCreateScoreList = createScoreList(jsonNameAndScore);

		when(repository.findByScore(conditionRq.getScore())).thenReturn(expectedCreateScoreList);

		List<Score> actualScoreList = gameService.scoreList(conditionRq);
		assertNotNull(actualScoreList);

		assertEquals(expectedCreateScoreList, actualScoreList);

		verify(repository).findByScore(conditionRq.getScore());

	}

	@Test
	public void testsave() throws Exception {

		Score expectedScore = createScoreList(jsonNameAndScore).get(0);
		when(repository.save(expectedScore)).thenReturn(expectedScore);

		Score actualScore = gameService.save(expectedScore);

		assertEquals(actualScore, expectedScore);

		verify(repository).save(expectedScore);

	}

	@Test
	public void testScoreListPageByPlayerNamesAndEndTimePeriod() throws Exception {

		// * "Get all scores by player1, player2 and player3 before 1st december 2020"

		ConditionRQ conditionRq = new ConditionRQ();

		conditionRq.getPlayerNames().add("Prerna Mishra");
		conditionRq.getPlayerNames().add("Suresh Benedy");
		conditionRq.setEndTime(new Timestamp(System.currentTimeMillis()));
		conditionRq.setScore(12);

		// Dummy Values- jsonNameAndScore
		List<Score> expectedCreateScoreList = createScoreList(jsonNameAndScore);

		PageRequest sortedByName = PageRequest.of(1, 3, Sort.by("playerName").and(Sort.by("time")));

		when(repository.findAllPlayersForPeriod(conditionRq.getPlayerNames(), getEndOfDay(conditionRq.getEndTime()),
				sortedByName)).thenReturn(expectedCreateScoreList);

		List<Score> actualScoreList = gameService.scoreListPage(conditionRq);
		assertNotNull(actualScoreList);

		assertEquals(expectedCreateScoreList, actualScoreList);

		verify(repository).findAllPlayersForPeriod(conditionRq.getPlayerNames(), getEndOfDay(conditionRq.getEndTime()),
				sortedByName);

	}

	@Test
	public void testScoreListPageByStartAndEndTimePeriod() throws Exception {
		// * "Get all scores after 1 Jan 2020 and before 1 Jan 2021"
		// return repository.findAllPlayersForPeriod(startTime, endTime, sortedByName);

		ConditionRQ conditionRq = new ConditionRQ();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = dateFormat.parse("23/01/2022");
		long time = date.getTime();

		conditionRq.setStartTime(new Timestamp(time));
		conditionRq.setEndTime(new Timestamp(System.currentTimeMillis()));
		conditionRq.setScore(12);

		// Dummy Values- jsonNameAndScore
		List<Score> expectedCreateScoreList = createScoreList(jsonNameAndScore);

		PageRequest sortedByName = PageRequest.of(1, 3, Sort.by("playerName").and(Sort.by("time")));

		when(repository.findAllPlayersForPeriod(getStartOfDay(conditionRq.getStartTime()),
				getEndOfDay(conditionRq.getEndTime()), sortedByName)).thenReturn(expectedCreateScoreList);

		List<Score> actualScoreList = gameService.scoreListPage(conditionRq);
		assertNotNull(actualScoreList);

		assertEquals(expectedCreateScoreList, actualScoreList);

		verify(repository).findAllPlayersForPeriod(getStartOfDay(conditionRq.getStartTime()),
				getEndOfDay(conditionRq.getEndTime()), sortedByName);

	}

	@Test
	public void testScoreListPageByName() throws Exception {
		// * "Get all scores by playerX"

		ConditionRQ conditionRq = new ConditionRQ();
		conditionRq.setPlayerName("Prerna Mishra");

		// Dummy Values- jsonNameAndScore
		List<Score> expectedCreateScoreList = createScoreList(jsonNameAndScore);

		PageRequest sortedByName = PageRequest.of(1, 3, Sort.by("playerName").and(Sort.by("time")));

		when(repository.findAllByPlayerName(conditionRq.getPlayerName(), sortedByName))
				.thenReturn(expectedCreateScoreList);

		List<Score> actualScoreList = gameService.scoreListPage(conditionRq);
		assertNotNull(actualScoreList);

		assertEquals(expectedCreateScoreList, actualScoreList);

		verify(repository).findAllByPlayerName(conditionRq.getPlayerName(), sortedByName);

	}

	@Test
	public void testScoreListPageByAfterStartTime() throws Exception {
		// "Get all score after 1st November 2020";

		ConditionRQ conditionRq = new ConditionRQ();
		conditionRq.setStartTime(new Timestamp(System.currentTimeMillis()));

		// Dummy Values- jsonNameAndScore
		List<Score> expectedCreateScoreList = createScoreList(jsonNameAndScore);

		PageRequest sortedByName = PageRequest.of(1, 3, Sort.by("playerName").and(Sort.by("time")));

		when(repository.findAllPlayersForPeriod(getStartOfDay(conditionRq.getStartTime()), sortedByName))
				.thenReturn(expectedCreateScoreList);

		List<Score> actualScoreList = gameService.scoreListPage(conditionRq);
		assertNotNull(actualScoreList);

		assertEquals(expectedCreateScoreList, actualScoreList);

		verify(repository).findAllPlayersForPeriod(getStartOfDay(conditionRq.getStartTime()), sortedByName);

	}

	@Test
	public void testFindHistory() throws Exception {

		when(repository.findPlayers("Prerna Mishra")).thenReturn(createScoreList(jsonNameAndScore));

		PlayerHistoryRS playerHistoryResponse = gameService.findHistory("Prerna Mishra");

		assertNotNull(playerHistoryResponse);

		assertEquals(playerHistoryResponse.getAverageScore(), new Double("30.0"));
		assertEquals(playerHistoryResponse.getLowScore().getScore(), 5);
		assertNotNull(playerHistoryResponse.getLowScore().getTime());
		assertEquals(playerHistoryResponse.getScoreList().get(0).getScore(), 5);
		assertNotNull(playerHistoryResponse.getScoreList().get(0).getTime());
		assertEquals(playerHistoryResponse.getScoreList().get(1).getScore(), 10);
		assertNotNull(playerHistoryResponse.getScoreList().get(1).getTime());
		assertEquals(playerHistoryResponse.getScoreList().get(2).getScore(), 100);
		assertNotNull(playerHistoryResponse.getScoreList().get(2).getTime());
		assertEquals(playerHistoryResponse.getScoreList().get(3).getScore(), 5);
		assertNotNull(playerHistoryResponse.getScoreList().get(3).getTime());
		assertEquals(playerHistoryResponse.getTopScore().getScore(), 100);
		assertNotNull(playerHistoryResponse.getTopScore().getTime());

		

		verify(repository).findPlayers("Prerna Mishra");

	}

	private List<Score> createScoreList(String jsonString) {
		List<Score> scoreList = new ArrayList<Score>();
		ObjectMapper mapper = new ObjectMapper();
		// String jsonString = getJsonString1();
		try {
			scoreList = mapper.readValue(jsonString, new TypeReference<List<Score>>() {
			});
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return scoreList;

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

	private static final String jsonNameAndScore = "[{" + "" + "   \"playerName\": \"Prerna Mishra\","
			+ "   \"score\": 5," + "   \"time\": \"2001-01-10T04:32\"" + "   " + "},{" + ""
			+ "   \"playerName\": \"Prerna Mishra\"," + "   \"score\": 10,"
			+ "   \"time\": \"2022-01-20T04:32:17.864+00:00\"" + "   " + "}," + "{" + ""
			+ "   \"playerName\": \"Prerna Mishra\"," + "   \"score\": 100,"
			+ "   \"time\": \"2022-01-15T04:32:17.864+00:00\"" + "   " + "}," + "{" + ""
			+ "   \"playerName\": \"Prerna Mishra\"," + "   \"score\": 5,"
			+ "   \"time\": \"2022-01-15T04:32:17.864+00:00\"" + "   " + "}" + "]";

}
