package com.example.demo.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RiotService {
	public void search(String name, Model model) {
		String id = null;
		String api_key = "RGAPI-e374823f-86a8-4dd7-a733-ed3e08ed5886";
		 try {
	            StringBuilder urlBuilder = new StringBuilder("https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + URLEncoder.encode(name, "UTF-8") + "?api_key=" + api_key); 
	            URL url = new URL(urlBuilder.toString());
	            HttpURLConnection con = (HttpURLConnection)url.openConnection();
	            con.setRequestMethod("GET");
	            int responseCode = con.getResponseCode();
	            BufferedReader br;
	            if(responseCode==200) { // 정상 호출
	                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
	            } else {  // 에러 발생
	                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	            }
	            String inputLine;
	            StringBuffer response = new StringBuffer();
	            while ((inputLine = br.readLine()) != null) {
	                response.append(inputLine);
	            }
	            br.close();
	            System.out.println(response.toString());
	            
	            JSONParser json = new JSONParser();
	            JSONObject object = (JSONObject)json.parse(response.toString());
	            id = object.get("id").toString(); // 전적 검색 때 사용할 아이디
	            String accountId = object.get("accountId").toString(); 
	            String puuid = object.get("puuid").toString();
	            Long profileIconId = (Long) object.get("profileIconId");
	            Long revisionDate = (Long) object.get("revisionDate");
	            Long summonerLevel = (Long) object.get("summonerLevel");
	        } catch (Exception e) {
	            System.out.println(e);
	        }
		 try {
	            StringBuilder urlBuilder = new StringBuilder("https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/" + URLEncoder.encode(id, "UTF-8") + "?api_key=" + api_key); 
	            URL url = new URL(urlBuilder.toString());
	            HttpURLConnection con = (HttpURLConnection)url.openConnection();
	            con.setRequestMethod("GET");
	            int responseCode = con.getResponseCode();
	            BufferedReader br;
	            if(responseCode==200) { // 정상 호출
	                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
	            } else {  // 에러 발생
	                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	            }
	            String inputLine;
	            StringBuffer response = new StringBuffer();
	            while ((inputLine = br.readLine()) != null) {
	                response.append(inputLine);
	            }
	            JSONParser json = new JSONParser();
	            JSONArray object = (JSONArray)json.parse(response.toString());
	            ObjectMapper objectMapper = new ObjectMapper();
		        Map<String, Object> map = new HashMap<>();	            
	            switch (object.size()) {
				case 0:
					model.addAttribute("code","0");
					model.addAttribute("result", "등록되지 않은 소환사입니다. 오타를 확인 후 다시 검색해주세요.");
					break;
				case 1:
			        map = objectMapper.readValue(object.toString(), new TypeReference<Map<String, Object>>(){});
			        	if(map.get("queueType").equals("RANKED_FLEX_SR")) {
			        		model.addAttribute("code","solo");
							model.addAttribute("tier_free", (String) map.get("tier"));
							model.addAttribute("rank_free", (String) map.get("rank"));
							model.addAttribute("leaguePoints_free", (int) map.get("leaguePoints"));
							model.addAttribute("wins_free", (int) map.get("wins"));
			        	}else if(map.get("queueType").equals("RANKED_SOLO_5x5")){
			        		model.addAttribute("code","free");
							model.addAttribute("tier_solo", (String) map.get("tier"));
							model.addAttribute("rank_solo", (String) map.get("rank"));
							model.addAttribute("leaguePoints_solo", (int) map.get("leaguePoints"));
							model.addAttribute("wins_solo", (int) map.get("wins"));
			        	}
					break;
				case 2:
					model.addAttribute("code","2");
			        List<Map<String, Object>> list = new ArrayList<>();
			        list = objectMapper.readValue(object.toString(), new TypeReference<List<Map<String, Object>>>(){});
			        for(int i=0; i<2; i++) {
			        	if(list.get(i).get("queueType").equals("RANKED_FLEX_SR")){
			        		model.addAttribute("code","solo");
							model.addAttribute("tier_free", (String) map.get("tier"));
							model.addAttribute("rank_free", (String) map.get("rank"));
							model.addAttribute("leaguePoints_free", (int) map.get("leaguePoints"));
							model.addAttribute("wins_free", (int) map.get("wins"));
			        	}else if(list.get(i).get("queueType").equals("RANKED_SOLO_5x5")) {
			        		model.addAttribute("code","free");
							model.addAttribute("tier_solo", (String) map.get("tier"));
							model.addAttribute("rank_solo", (String) map.get("rank"));
							model.addAttribute("leaguePoints_solo", (int) map.get("leaguePoints"));
							model.addAttribute("wins_solo", (int) map.get("wins"));
			        	}
			        }
			        break;
			    default :
			    	model.addAttribute("code","error");
			    	model.addAttribute("result", "알 수 없는 오류가 발생했습니다. 잠시 후 다시 검색해주세요");
					break;
				}
	            
	            System.out.println(response.toString());
	        } catch (Exception e) {
	            System.out.println(e);
	        }
	}
}
//널처리
//
//이름 없을 때 - 등록되지 않은 소환사입니다. 오타를 확인 후 다시 검색해주세요.
//
//이름 있을 때 - 검색 
//
//랭크 없을 때
//둘다 있을 때  
//솔로랭크만 있을 때
//자유랭크만 있을 때
//
//전적 = 승/(승+패) 

//참고할주소
//https://github.com/HamSungJun/FM.GG
//https://developer.riotgames.com/docs/lol#data-dragon_champions
//https://developer.riotgames.com/apis#match-v5/GET_getTimeline 전적결과 match-5
