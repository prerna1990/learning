package com.github.test.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.LinkedHashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = GameApplication.class)
@AutoConfigureMockMvc
class GameApplicationTests {

	@Autowired
	MockMvc mvc;

	@Test
	void contextLoads() {
	}

	@Test
	public void test() {
		String content = getContent();
		ResultActions resp;
		try {
			String uri = "/game/addall";
			resp = mvc.perform(MockMvcRequestBuilders.post(uri).content(content).accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON));
			ResultMatcher matcher = new ResultMatcher() {

				@Override
				public void match(MvcResult result) throws Exception {
					assertEquals(200, result.getResponse().getStatus());
					String responseContent = result.getResponse().getContentAsString();
					assertNotNull(responseContent);
					ObjectMapper mapper = new ObjectMapper();
					List<LinkedHashMap<String, Object>> scores = mapper.readValue(responseContent, List.class);
					assertNotNull(scores);
					assertNotNull(scores.get(0).get("id"));
					assertNotNull(scores.get(0).get("playerName"));
					assertNotNull(scores.get(0).get("score"));
					assertNotNull(scores.get(0).get("time"));
				}
			};
			resp.andExpect(matcher);
		} catch (Exception e) {
			assertNull(e);
		}

	}

	private String getContent() {
		return "[{" + "" + "   \"playerName\": \"XYZ Mishra\"," + "   \"score\": 5,"
				+ "   \"time\": \"2001-01-10T04:32\"" + "   " + "},{" + "" + "   \"playerName\": \"XYZ Mishra\","
				+ "   \"score\": 5," + "   \"time\": \"2022-01-30T04:32:17.864+00:00\"" + "   " + "},{" + ""
				+ "   \"playerName\": \"XYZ Mishra\"," + "   \"score\": 10,"
				+ "   \"time\": \"2022-01-20T04:32:17.864+00:00\"" + "   " + "}," + "{" + ""
				+ "   \"playerName\": \"XYZ Mishra\"," + "   \"score\": 100,"
				+ "   \"time\": \"2022-01-15T04:32:17.864+00:00\"" + "   " + "}," + "{" + ""
				+ "   \"playerName\": \"abc Mishra\"," + "   \"score\": 5,"
				+ "   \"time\": \"2022-01-15T04:32:17.864+00:00\"" + "   " + "}" + "]";
	}

}
