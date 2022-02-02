package com.github.test.game.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.test.game.domain.Score;
import com.github.test.game.dto.ConditionRQ;
import com.github.test.game.dto.PlayerHistoryRS;
import com.github.test.game.service.GameService;

@RestController
//@RequestMapping("/game")
public class GameController {

	@Autowired
	private GameService gameService;

	/**
	 * For Creating a single end Score
	 * 
	 * @param score score of a person at a given time
	 * @return stored score value
	 */
	@PostMapping("/createScore")
	public Score list(@RequestBody Score score) {
		return gameService.save(score);

	}

	/**
	 * To query based on condition
	 * 
	 * @param conditionRq condition can be
	 * 
	 *                    { "id": 1, "playerName": "Prerna", "score": 1, "endTime":
	 *                    "2022-01-31T12:55:57.372+00:00", "pageNo": 0, "pageSize":
	 *                    5 }
	 * @return matched result
	 */
	@PostMapping("/searchScoreList")
	public List<Score> searchScoreList(@RequestBody ConditionRQ conditionRq) {
		return gameService.scoreListPage(conditionRq);

	}

	/**
	 * To delete a score by id
	 * 
	 * @param scoreId id of the score to delete
	 * @return Ok - when deleted. Id not found - when not found.
	 * @throws Exception
	 */
	@DeleteMapping("/deleteScore/{id}")
	public String deleteScore(@PathVariable("id") Integer scoreId) throws Exception {
		return gameService.deleteById(scoreId);

	}

	/**
	 * To search a score by id
	 * 
	 * @param scoreId id of the score to search
	 * @return core if available
	 */
	@GetMapping("/getScore/{id}")
	public Optional<Score> getScore(@PathVariable("id") Integer scoreId) {
		return gameService.findByScoreId(scoreId);

	}

	/**
	 * To get the history of a player
	 * 
	 * @param playerName name of the player
	 * @return history of the player if available
	 */
	@GetMapping("/history/{playerName}")
	public @ResponseBody PlayerHistoryRS getHistory(@PathVariable("playerName") String playerName) {
		return gameService.findHistory(removeHtml(playerName));

	}

	private String removeHtml(String playerName) {
		
		return playerName.replaceAll("%20"," ");
	}


}
