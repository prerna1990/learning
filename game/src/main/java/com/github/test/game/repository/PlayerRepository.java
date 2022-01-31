package com.github.test.game.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.github.test.game.domain.Score;

public interface PlayerRepository extends PagingAndSortingRepository<Score, Integer> {

	@Query(value = "select * FROM Score WHERE lower(playerName) = lower(?) order by time", nativeQuery = true)
	public List<Score> findPlayers(String playerName);

	@Query(value = "select * FROM Score WHERE time > ? and time < ? ", nativeQuery = true)
	public List<Score> findAllPlayersForPeriod(Timestamp start, Timestamp end);

	@Query(value = "select * FROM Score WHERE time > ? ", nativeQuery = true)
	public List<Score> findAllPlayersForPeriod(Timestamp start);

	@Query(value = "select * FROM Score WHERE playerName in ? and time < ? ", nativeQuery = true)
	public List<Score> findAllPlayersForPeriod(List<String> playerNames, Timestamp end);

	public List<Score> findByPlayerNameAndScore(String playerName, Integer score);

	public List<Score> findByScore(Integer score);

	public Optional<Score> findById(Integer id);

	@Query(value = "select * FROM Score WHERE lower(playerName) = lower(?) ", nativeQuery = true)
	public List<Score> findAllByPlayerName(String playerName, Pageable sortedByName);
}
