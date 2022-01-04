package com.example.demo.service.parse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Runes {
	public Map<String, String> runes(long main, long mainFirst, long sub) throws Exception{
		Map<String, String> result = new LinkedHashMap<String, String>();
		StringBuilder urlBuilder = new StringBuilder(
				"https://ddragon.leagueoflegends.com/cdn/11.24.1/data/en_US/runesReforged.json");
		URL url = new URL(urlBuilder.toString());
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();
		BufferedReader br;
		if (responseCode == 200) { // 정상 호출
			br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} else { // 에러 발생
			br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		}
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = br.readLine()) != null) {
			response.append(inputLine);
		}
		br.close();

		JSONParser json = new JSONParser();
		JSONArray object = (JSONArray) json.parse(response.toString());
		ObjectMapper objectMapper = new ObjectMapper();
		List<Map<String, Object>> list = new ArrayList<>();
		List<Map<String, Object>> slots = new ArrayList<>();
		List<Map<String, Object>> runes = new ArrayList<>();
		list = objectMapper.readValue(object.toString(), new TypeReference<List<Map<String, Object>>>() {
		});
		for (int i = 0; i < list.size(); i++) {
			if ((int) list.get(i).get("id") == main) {
				slots = (List<Map<String, Object>>) list.get(i).get("slots");
				break;
			}
		}
		runes = (List<Map<String, Object>>) slots.get(0).get("runes");
		for (int j = 0; j < runes.size(); j++) {
			if ((int) runes.get(j).get("id") == mainFirst) {
				result.put("mainRune", (String) runes.get(j).get("icon"));
				break;
			}
		}
		for (int i = 0; i < list.size(); i++) {
			if ((int) list.get(i).get("id") == sub) {
				result.put("subRune", (String) list.get(i).get("icon"));
				break;
			}
		}
		return result;
	}
}
