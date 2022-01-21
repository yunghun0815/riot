package com.example.demo.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.service.parse.*;
import org.hibernate.internal.build.AllowSysOut;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.demo.service.RiotService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import antlr.debug.ParserMatchListener;

@Service
public class RiotServiceImpl implements RiotService {
	String api_key = "RGAPI-469f9299-cf01-48c6-9f18-7e4b4798f8c3";

	public void search(String name, Model model) {
		String api_key = "RGAPI-469f9299-cf01-48c6-9f18-7e4b4798f8c3";
		String id = null;
		String puuid = null;
		String summonerName = null;
		String check = null;
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
			if (object.size() > 1) {
				check = "true";
				id = object.get("id").toString(); // 전적 검색 때 사용할 아이디
				puuid = object.get("puuid").toString();
				String accountId = object.get("accountId").toString();
				long profileIconId = (long) object.get("profileIconId");
				long summonerLevel = (long) object.get("summonerLevel");
				summonerName = (String) object.get("name");
				model.addAttribute("puuid", puuid);
				model.addAttribute("check", check);
				model.addAttribute("name", summonerName);
				model.addAttribute("profileIconId", profileIconId);
				model.addAttribute("summonerLevel", summonerLevel);
			} else {
				check = "false";
				model.addAttribute("check", check);
				model.addAttribute("name", name);
				System.out.println("error발생");
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		// 소환사 아이디로 전적 검색
		if (check.equals("true")) {
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
				int o = object.size();
				int b = 0;
				if(o==2 && list.get(0).get("queueType").equals("RANKED_TFT_PAIRS")){
					o--;
					b=1;
				}
				switch (o) {
					case 0:
						model.addAttribute("code", "0");
						model.addAttribute("result", "등록되지 않은 소환사입니다. 오타를 확인 후 다시 검색해주세요.");
						break;
					case 1:
						map = list.get(b);
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
					case 2: case 3:
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
			//	 소환사 puuid로 최근 전적(match V5 id값) 검색
			String[] matchId = null;
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
				matchId = response.toString().replace("[", "").replace("]", "").replace("\"", "").split(",");

			} catch (Exception e) {
				System.out.println(e);
			}
			List<Map<String, Object>> matchList = new ArrayList<>();  // 총 10개의 게임 데이터 저장
			for (int i = 0; i < 10; i++) {
				Map<String, Object> teamMap = new HashMap<>(); //레드, 블루로 나눔
				List<LinkedHashMap<String, Object>> redList = new ArrayList<>(); //레드팀
				List<LinkedHashMap<String, Object>> blueList = new ArrayList<>(); //블루팀
				try {
					StringBuilder urlBuilder = new StringBuilder(
							"https://asia.api.riotgames.com/lol/match/v5/matches/" + URLEncoder.encode(matchId[i], "UTF-8")
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
					Map<String, Object> map = new HashMap<>();
					map = (Map) object.get("info");
					List<Map<String, Object>> participants = new ArrayList<>();
					participants = (List<Map<String, Object>>) map.get("participants");
					Duration duration = new Duration();
					Runes runes = new Runes();
					Spell spell = new Spell();
					TimeStamp time = new TimeStamp();
					Average aver = new Average();
					List<Map<String, Object>> blueGame = new ArrayList<>();
					List<Map<String, Object>> redGame = new ArrayList<>();
					Map<String, Object> playerData = new LinkedHashMap<>();//플레이어 데이터
					Map<String, Object> redPlayerData = new LinkedHashMap<>();//플레이어 데이터
					Map<String, Object> mapp = new LinkedHashMap<>();
					QueueType queue = new QueueType();
					long killPercent = 0;
					long myKillAssi = 0;
					long realDuration = (long) map.get("gameDuration")/1000;

					try {
						if((long)map.get("gameEndTimestamp")>0){
							realDuration = (long) map.get("gameDuration");
						}
					}catch (Exception e){
						realDuration = (long) map.get("gameDuration")/1000;
					}
					for (int j = 0; j < 10; j++) {
						String playerName = (String) participants.get(j).get("summonerName");
						//					if(playerName.replaceAll(" ","").equals(name)){
						if (playerName.equals(summonerName)) {
							if (j < 5) {
								for (int k = 0; k < 5; k++) {
									killPercent += (long) participants.get(k).get("kills");
								}
							} else {
								for (int k = 5; k < 10; k++) {
									killPercent += (long) participants.get(k).get("kills");
								}
							}
							myKillAssi = (long) participants.get(j).get("kills") + (long) participants.get(j).get("assists");
							Map<String, Object> blue = new LinkedHashMap<>();
							playerData.put("individualPosition", participants.get(j).get("individualPosition"));
							playerData.put("win", participants.get(j).get("win"));
							playerData.put("summoner1Id", spell.spell((Long) participants.get(j).get("summoner1Id")));
							playerData.put("summoner2Id", spell.spell((Long) participants.get(j).get("summoner2Id")));
							playerData.put("championName", participants.get(j).get("championName"));
							playerData.put("summonerName", participants.get(j).get("summonerName"));
							playerData.put("kills", participants.get(j).get("kills"));
							playerData.put("deaths", participants.get(j).get("deaths"));
							playerData.put("assists", participants.get(j).get("assists"));
							playerData.put("goldEarned", participants.get(j).get("goldEarned"));
							playerData.put("visionWardsBoughtInGame", participants.get(j).get("visionWardsBoughtInGame"));
							playerData.put("average", aver.average((long) participants.get(j).get("kills"), (long) participants.get(j).get("deaths"), (long) participants.get(j).get("assists")));
							playerData.put("totalDamageDealtToChampions", participants.get(j).get("totalDamageDealtToChampions"));
							playerData.put("wardsPlaced", participants.get(j).get("wardsPlaced"));
							playerData.put("wardsKilled", participants.get(j).get("wardsKilled"));
							playerData.put("totalMinionsKilled", participants.get(j).get("totalMinionsKilled"));
							playerData.put("neutralMinionsKilled", participants.get(j).get("neutralMinionsKilled"));

							playerData.put("mincs", aver.cs((long) participants.get(j).get("totalMinionsKilled"), (long) participants.get(j).get("neutralMinionsKilled"), realDuration / 60));
							playerData.put("champLevel", participants.get(j).get("champLevel"));
							playerData.put("item0", participants.get(j).get("item0"));
							playerData.put("item1", participants.get(j).get("item1"));
							playerData.put("item2", participants.get(j).get("item2"));
							playerData.put("item3", participants.get(j).get("item3"));
							playerData.put("item4", participants.get(j).get("item4"));
							playerData.put("item5", participants.get(j).get("item5"));
							playerData.put("item6", participants.get(j).get("item6"));
							playerData.put("matchId", matchId[i]);
							Map<String, Object> perks = new HashMap<>();
							perks = (Map<String, Object>) participants.get(j).get("perks");
							List<Map<String, Object>> styles = new ArrayList<>();
							styles = (List<Map<String, Object>>) perks.get("styles");
							List<Map<String, Object>> selections = new ArrayList<>();
							selections = (List<Map<String, Object>>) styles.get(0).get("selections");
							Map<String, String> rune = new LinkedHashMap<>();
							rune = runes.runes((long) styles.get(0).get("style"), (long) selections.get(0).get("perk"), (long) styles.get(1).get("style"));
							//System.out.println(runes.runes((long)styles.get(0).get("style"), (long)selections.get(0).get("perk"), (long)styles.get(1).get("style")));
							playerData.put("mainRune", rune.get("mainRune"));
							playerData.put("subRune", rune.get("subRune"));
							break;
							//	blueGame.add(blue);
							//	playerData.put("blue", blueGame);
						} else {
						}
					}
					playerData.put("killPercent", aver.killPer(killPercent, myKillAssi));
					playerData.put("getEndTimestamp", time.timeStamp((long) map.get("gameStartTimestamp")));
					playerData.put("queueId", queue.queue((long) map.get("queueId")));
					playerData.put("gameDuration", duration.duration(realDuration));

					matchList.add(playerData);
					//		matchList.put("red", redPlayerData);
				} catch (Exception e) {
					System.out.println(e);
					//	}
				}

			}
			model.addAttribute("result", matchList);
		}
		//	System.out.println(matchList);
	}
}
//이미지호스팅 
//1. 챔피언이미지 2.룬이미지 3.스펠이미지 4.소환사아이콘이미지 5.아이템이미지
//챔피언 : 
// 주룬 : https://ddragon.leagueoflegends.com/cdn/img/perk-images/Styles/Precision/LegendTenacity/LegendTenacity.png
// 스펠 : https://ddragon.leagueoflegends.com/cdn/11.20.1/img/spell/SummonerSmite.png
// 아이콘 :https://ddragon.leagueoflegends.com/cdn/11.20.1/img/profileicon/4027.png
//아이템 : https://ddragon.leagueoflegends.com/cdn/11.20.1/img/item/3108.png


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
//주 룬 이미지 https://ddragon.leagueoflegends.com/cdn/img/perk-images/Styles/Precision/LegendTenacity/LegendTenacity.png  칼날비
//전체 룬 이미지 https://ddragon.leagueoflegends.com/cdn/img/perk-images/Styles/7201_Precision.png  칼날비
//https://github.com/HamSungJun/FM.GG
//https://developer.riotgames.com/docs/lol#data-dragon_champions
//https://developer.riotgames.com/apis#match-v5/GET_getTimeline 전적결과 match-5
