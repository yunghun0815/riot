package com.example.demo.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.internal.build.AllowSysOut;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.demo.service.RiotService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RiotServiceImpl implements RiotService {
	String api_key = "RGAPI-90203a04-c4ce-403d-ab87-10b001687d65";
	public void search(String name, Model model) {
		String api_key = "RGAPI-90203a04-c4ce-403d-ab87-10b001687d65";
		String id = null;
		String puuid = null;
		// 소환사 정보 검색
		try {
			StringBuilder urlBuilder = new StringBuilder(
					"https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + URLEncoder.encode(name, "UTF-8")
							+ "?api_key=" + api_key);
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
			JSONObject object = (JSONObject) json.parse(response.toString());
			id = object.get("id").toString(); // 전적 검색 때 사용할 아이디
			puuid = object.get("puuid").toString();
			String accountId = object.get("accountId").toString();
			int profileIconId = (int) object.get("profileIconId");
			int summonerLevel = (int) object.get("summonerLevel");
		} catch (Exception e) {
			System.out.println(e);
		}

		// 소환사 아이디로 전적 검색
		try {
			StringBuilder urlBuilder = new StringBuilder(
					"https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/" + URLEncoder.encode(id, "UTF-8")
							+ "?api_key=" + api_key);
			URL url = new URL(urlBuilder.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			JSONParser json = new JSONParser();
			JSONArray object = (JSONArray) json.parse(response.toString());
			ObjectMapper objectMapper = new ObjectMapper();
			int percent;
			Map<String, Object> map = new HashMap<>();
			List<Map<String, Object>> list = new ArrayList<>();
			list = objectMapper.readValue(object.toString(), new TypeReference<List<Map<String, Object>>>() {
			});
			switch (object.size()) {
			case 0:
				model.addAttribute("code", "0");
				model.addAttribute("result", "등록되지 않은 소환사입니다. 오타를 확인 후 다시 검색해주세요.");
				break;
			case 1:
				map = list.get(0);
				if (map.get("queueType").equals("RANKED_FLEX_SR")) {
					model.addAttribute("code", "free");
					model.addAttribute("tier_free", (String) map.get("tier"));
					model.addAttribute("rank_free", (String) map.get("rank"));
					model.addAttribute("leaguePoints_free", (int) map.get("leaguePoints"));
					model.addAttribute("wins_free", (int) map.get("wins"));
					model.addAttribute("losses_free", (int) map.get("losses"));
					percent = (int) (Double.valueOf(map.get("wins").toString())
							/ ((int) map.get("wins") + (int) map.get("losses")) * 100);
					model.addAttribute("percent_free", percent);
				} else if (map.get("queueType").equals("RANKED_SOLO_5x5")) {
					model.addAttribute("code", "solo");
					model.addAttribute("tier_solo", (String) map.get("tier"));
					model.addAttribute("rank_solo", (String) map.get("rank"));
					model.addAttribute("leaguePoints_solo", (int) map.get("leaguePoints"));
					model.addAttribute("wins_solo", (int) map.get("wins"));
					model.addAttribute("losses_solo", (int) map.get("losses"));
					percent = (int) (Double.valueOf(map.get("wins").toString())
							/ ((int) map.get("wins") + (int) map.get("losses")) * 100);
					model.addAttribute("percent_solo", percent);
				}
				break;
			case 2:
				model.addAttribute("code", "2");
				for (int i = 0; i < 2; i++) {
					if (list.get(i).get("queueType").toString().equals("RANKED_FLEX_SR")) {
						model.addAttribute("tier_free", (String) list.get(i).get("tier"));
						model.addAttribute("rank_free", (String) list.get(i).get("rank"));
						model.addAttribute("leaguePoints_free", (int) list.get(i).get("leaguePoints"));
						model.addAttribute("wins_free", (int) list.get(i).get("wins"));
						model.addAttribute("losses_free", (int) list.get(i).get("losses"));
						percent = (int) (Double.valueOf(list.get(i).get("wins").toString())
								/ ((int) list.get(i).get("wins") + (int) list.get(i).get("losses")) * 100);
						model.addAttribute("percent_free", percent);
					} else if (list.get(i).get("queueType").toString().equals("RANKED_SOLO_5x5")) {
						model.addAttribute("tier_solo", (String) list.get(i).get("tier"));
						model.addAttribute("rank_solo", (String) list.get(i).get("rank"));
						model.addAttribute("leaguePoints_solo", (int) list.get(i).get("leaguePoints"));
						model.addAttribute("wins_solo", (int) list.get(i).get("wins"));
						model.addAttribute("losses_solo", (int) list.get(i).get("losses"));
						percent = (int) (Double.valueOf(list.get(i).get("wins").toString())
								/ ((int) list.get(i).get("wins") + (int) list.get(i).get("losses")) * 100);
						model.addAttribute("percent_solo", percent);
					}
				}
				break;
			default:
				model.addAttribute("code", "error");
				model.addAttribute("result", "알 수 없는 오류가 발생했습니다. 잠시 후 다시 검색해주세요");
				break;
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		// 소환사 puuid로 최근 전적 검색
		try {
			StringBuilder urlBuilder = new StringBuilder(
					
					"https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/" + URLEncoder.encode(puuid, "UTF-8")
							+ "?api_key=" + api_key);
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
			List<String> matchList = new ArrayList<>();
			System.out.println(matchList.toString());
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public static void main(String[] args) {
		String puuid = "HqY8GPxc11kr7mX7z4dWLSHDka2BetteFVvgi4p4xFwS6qUky3102AwFTWJoMdHrZ1gPYynBdUo5eA";
		String api_key = "RGAPI-90203a04-c4ce-403d-ab87-10b001687d65";
		String[] matchId;
		try {
			StringBuilder urlBuilder = new StringBuilder(
					"https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/" + URLEncoder.encode(puuid, "UTF-8") + "/ids"
							+ "?api_key=" + api_key + "&count=" + "100");
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
			matchId = response.toString().replace("[","").replace("]", "").replace("\"", "").split(",");
			System.out.println(matchId[0]);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		//매치아이디 상세결과 https://asia.api.riotgames.com/lol/match/v5/matches/KR_5514522592/timeline?api_key=RGAPI-90203a04-c4ce-403d-ab87-10b001687d65
		try {
			StringBuilder urlBuilder = new StringBuilder(
					"https://asia.api.riotgames.com/lol/match/v5/matches/" + URLEncoder.encode(puuid, "UTF-8") + "/ids"
							+ "?api_key=" + api_key + "&count=" + "100");
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
			matchId = response.toString().replace("[","").replace("]", "").replace("\"", "").split(",");
			System.out.println(matchId[0]);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}


//이미지호스팅 
//1. 챔피언이미지 2.룬이미지 3.스펠이미지 4.소환사아이콘이미지 


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
//https://shyunku.tistory.com/56 전체정리


//챔피언 이미지 https://ddragon.leagueoflegends.com/cdn/11.20.1/img/champion/Graves.png
//프로필 아이콘 이미지 https://ddragon.leagueoflegends.com/cdn/11.20.1/img/profileicon/4027.png
//최종아이템 이미지 https://ddragon.bangingheads.net/cdn/11.20.1/img/item/1038.png
//스펠 이미지 https://ddragon.leagueoflegends.com/cdn/11.20.1/img/spell/SummonerSmite.png

//룬 도메인 https://ddragon.leagueoflegends.com/cdn/img/변수
//룬 이미지 https://ddragon.leagueoflegends.com/cdn/img/perk-images/Styles/7203_Whimsy.png
//주 룬 이미지 https://ddragon.leagueoflegends.com/cdn/img/perk-images/Styles/Domination/HailOfBlades/HailOfBlades.png  칼날비
//전체 룬 이미지 https://ddragon.leagueoflegends.com/cdn/img/perk-images/Styles/7201_Precision.png  칼날비
//https://github.com/HamSungJun/FM.GG
//https://developer.riotgames.com/docs/lol#data-dragon_champions
//https://developer.riotgames.com/apis#match-v5/GET_getTimeline 전적결과 match-5
