package com.github.test.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.testng.Assert.assertNull;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeClass;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.test.game.domain.Score;
import com.github.test.game.dto.PlayerHistoryRS;
import com.github.test.game.repository.PlayerRepository;

@AutoConfigureMockMvc
@SpringBootTest(classes = { GameApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameApplicationTests {
	@LocalServerPort
	private int port;
	@Autowired
	PlayerRepository repository;
	private final RestTemplate restTemplate = new RestTemplate();

	@BeforeClass
	public void setup() {

	}

	@Test
	public void testCreateScore() {
		String baseUrl = getUrl("createScore");
		String json = "{" + "" + "   \"playerName\": \"Prerna Mishra\"," + "   \"score\": 17,"
				+ "   \"time\": \"Sun, 06 Nov 1994 08:49:37 GMT\"" + "}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<String>(json, headers);
		ResponseEntity<Score> response = restTemplate.postForEntity(baseUrl, request, Score.class);
		assertNotNull(response.getBody().getId());
		assertEquals(response.getBody().getPlayerName(), "Prerna Mishra");
		assertEquals(response.getBody().getScore(), 17);
		assertNotNull(response.getBody().getTime());

	}

	@Test
	public void testDeleteScore() {
		Score score = new Score();
		score.setPlayerName("Prerna Mishra");
		score.setTime(new Timestamp(System.currentTimeMillis()));
		score = repository.save(score);
		String baseUrl = getUrl("deleteScore/" + score.getId());
		try {
			restTemplate.delete(baseUrl);
		} catch (Exception e) {
			assertNull(e);
		}
	}

	@Test
	public void testSearchScore() {
		Score score = new Score();
		score.setPlayerName("Pooja Mishra");
		score.setTime(new Timestamp(System.currentTimeMillis()));
		score.setScore(3);
		score = repository.save(score);

		String baseUrl = getUrl("getScore/" + score.getId());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<Score> response = restTemplate.getForEntity(baseUrl, Score.class);
		assertEquals(response.getBody().getId(), score.getId());
		assertEquals(response.getBody().getPlayerName(), "Pooja Mishra");
		assertEquals(response.getBody().getScore(), score.getScore());
		assertNotNull(response.getBody().getTime());

	}

	/**
	 * "Get all scores by playerX" 
	 * "Get all score after 1st November 2020" "Get all
	 * scores by player1, player2 and player3 before 1st december 2020" "Get all
	 * scores after 1 Jan 2020 and before 1 Jan 2021"
	 */

	@Test
	public void testsearchScoreListWithPlayerName() {

		// Pre-data load before end-point call
		List<Score> savedScoreList = (List<Score>) repository.saveAll(createScoreList());

		String baseUrl = getUrl("/searchScoreList");

		// "Get all scores by playerX"

		String criteriaForPlayerByNameAndTimeJson = "{" + "" + "   \"playerName\": \"Prerna Mishra\"" + "}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// For criteriaForPlayerByNameAndTimeJson
		HttpEntity<String> request1 = new HttpEntity<String>(criteriaForPlayerByNameAndTimeJson, headers);
		ResponseEntity<List<Score>> responseList1 = restTemplate.exchange(baseUrl, HttpMethod.POST, request1,
				new ParameterizedTypeReference<List<Score>>() {
				});

		assertNotNull(responseList1);
		assertEquals(responseList1.getBody().get(0).getScore(), 5);
		assertEquals(responseList1.getBody().get(1).getScore(), 10);

	}

	@Test
	public void testsearchScoreListWithPlayerNameAndEndTime() {
		// Pre-data load before end-point call
		List<Score> savedScoreList = (List<Score>) repository.saveAll(createScoreList());
		String baseUrl = getUrl("/searchScoreList");

		// "Get all scores by playerX"

		String criteriaForPlayerByNameAndTimeJson = "{" + "" + "   \"playerName\": \"Prerna Mishra\","
				+ "   \"endTime\": \"2022-01-30T04:32:17.864+00:00\"" + "   " + "}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// For criteriaForPlayerByNameAndTimeJson
		HttpEntity<String> request1 = new HttpEntity<String>(criteriaForPlayerByNameAndTimeJson, headers);
		ResponseEntity<List<Score>> responseList1 = restTemplate.exchange(baseUrl, HttpMethod.POST, request1,
				new ParameterizedTypeReference<List<Score>>() {
				});

		assertNotNull(responseList1);

	}

	@Test
	public void testsearchScoreListWithListOfPlayersName() {
		// Pre-data load before end-point call
		List<Score> savedScoreList = (List<Score>) repository.saveAll(createScoreList());
		String baseUrl = getUrl("/searchScoreList");
		String criteriaForListOfPlayersByName = "{" + 
				"   \"endTime\": \"Wed, 2 Feb 2022 08:49:37 GMT\"," + 
				"   \"playerNames\":    [" + 
				"      \"Prerna Mishra\"," + 
				"      \"Suresh Benedy\"," + 
				"      \"Ramesh\"," + 
				"      \"Raju\"" + 
				"   ]" + 
				"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request4 = new HttpEntity<String>(criteriaForListOfPlayersByName, headers);
		ResponseEntity<List<Score>> responseList4 = restTemplate.exchange(baseUrl, HttpMethod.POST, request4,
				new ParameterizedTypeReference<List<Score>>() {
				});

		// for criteriaForListOfPlayersByName
		assertNotNull(responseList4);
		
	}

	@Test
	public void testsearchScoreListWithEndTimeAndStartTime() {
		// Pre-data load bafore end-point call
		List<Score> savedScoreList = (List<Score>) repository.saveAll(createScoreList());
		String baseUrl = getUrl("/searchScoreList");
		String criteriaForStartAndEndTime = "{" + 
				"  " + 
				"  " + 
				"  " + 
				"   \"startTime\": \"2022-01-15T08:28:25.294+00:00\"," + 
				"   \"endTime\": \"2022-01-21T08:28:25.294+00:00\"" + 
				"   " + 
				"}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request3 = new HttpEntity<String>(criteriaForStartAndEndTime, headers);
		ResponseEntity<List<Score>> responseList3 = restTemplate.exchange(baseUrl, HttpMethod.POST, request3,
				new ParameterizedTypeReference<List<Score>>() {
				});

		// for criteriaForStartAndEndTime
		assertNotNull(responseList3);
		
	}
	
	@Test
	public void testSearchScoreHistory() {
		// Pre-data load bafore end-point call
				List<Score> savedScoreList = (List<Score>) repository.saveAll(createScoreList());

		String baseUrl = getUrl("history/Prerna%20Mishra"); 

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<PlayerHistoryRS> response = restTemplate.getForEntity(baseUrl, PlayerHistoryRS.class);
		assertNotNull(response);
		
		
		

	}

	private List<Score> createScoreList() {
		List<Score> scoreList = new ArrayList<Score>();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = getJsonString();
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

	private String getJsonString() {
		// TODO Auto-generated method stub
		return "[{" + "" + "   \"playerName\": \"Prerna Mishra\"," + "   \"score\": 5,"
				+ "   \"time\": \"2001-01-10T04:32\"" + "   " + "},{" + ""
				+ "   \"playerName\": \"Suresh Benedy\"," + "   \"score\": 5,"
				+ "   \"time\": \"2022-01-30T04:32:17.864+00:00\"" + "   " + "},{" + ""
				+ "   \"playerName\": \"Prerna Mishra\"," + "   \"score\": 10,"
				+ "   \"time\": \"2022-01-20T04:32:17.864+00:00\"" + "   " + "}," + "{" + ""
				+ "   \"playerName\": \"Prerna Mishra\"," + "   \"score\": 100,"
				+ "   \"time\": \"2022-01-15T04:32:17.864+00:00\"" + "   " + "}," + "{" + ""
				+ "   \"playerName\": \"Prerna Mishra\"," + "   \"score\": 5,"
				+ "   \"time\": \"2022-01-15T04:32:17.864+00:00\"" + "   " + "}" + "]";
	}

	private String getUrl(String path) {
		// TODO Auto-generated method stub
		return "http://localhost:" + port + "/" + path;
	}

}
