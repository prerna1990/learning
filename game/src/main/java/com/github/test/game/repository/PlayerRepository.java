package com.github.test.game.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.github.test.game.domain.Score;

public interface PlayerRepository extends PagingAndSortingRepository<Score, Integer> {

	@Query(value = "select * FROM Score WHERE lower(playerName) = lower(?) order by time", nativeQuery = true)
	public List<Score> findPlayers(String playerName);

	public List<Score> findByPlayerNameAndScore(String playerName, Integer score);

	public List<Score> findByScore(Integer score);

	public Optional<Score> findById(Integer id);

	@Query(value = "FROM Score WHERE lower(playerName) = lower(:playerName) ")
	public List<Score> findAllByPlayerName(@Param("playerName")String playerName, Pageable sortedByName);
	
	@Query(value = "FROM Score WHERE time > :start and time < :end ")
	public List<Score> findAllPlayersForPeriod(@Param("start")Timestamp start, @Param("end")Timestamp end, Pageable sortedByName);

	@Query(value = "FROM Score WHERE time > :start ")
	public List<Score> findAllPlayersForPeriod(@Param("start")Timestamp start, Pageable sortedByName);

	@Query(value = "FROM Score WHERE playerName in :playerName and time < :end ")
	public List<Score> findAllPlayersForPeriod(@Param("playerName") List<String> playerNames, @Param("end")Timestamp end, Pageable sortedByName);
}
