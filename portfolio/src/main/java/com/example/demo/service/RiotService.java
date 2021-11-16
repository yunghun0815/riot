package com.example.demo.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;


@Service
public class RiotService {
	String api_key = "RGAPI-391dd36e-13cb-4370-9c69-606c9fe9a16f";
	public void search(String name, Model model) {
		// 검색한 아이디로 api 호출
		// name 값 받아 와 한글로 인코딩 후 소환사 검색 api 출력  
		// https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/%EB%A7%88%EC%8A%A4%ED%84%B0%EA%B0%84%EB%8B%A4%EC%A0%95%EC%98%81%ED%9B%88?api_key=RGAPI-391dd36e-13cb-4370-9c69-606c9fe9a16f
		// domain = https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/{name}?api_key=
		
		// response에서 id 값 받아 와 전적검색 api 출력 
		// https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/ZNpxZhNhXW77QeRkX3BoPEAHsvm9USDX7vzUSkeo1foo-w?api_key=RGAPI-391dd36e-13cb-4370-9c69-606c9fe9a16f
		
		// domain = https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/{id}?api_key=
		String id = null;
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
	            br.close();
	            System.out.println(response.toString());
	            
	            JSONParser json = new JSONParser();
	            JSONObject object = (JSONObject)json.parse(response.toString());
	            System.out.println(object.toString());
	        } catch (Exception e) {
	            System.out.println(e);
	        }
		 
	}
	public static void main(String[] args) {
		String api_key = "RGAPI-391dd36e-13cb-4370-9c69-606c9fe9a16f";
		String name ="마스터간다정영훈";
		// 검색한 아이디로 api 호출
		// name 값 받아 와 한글로 인코딩 후 소환사 검색 api 출력  
		// https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/%EB%A7%88%EC%8A%A4%ED%84%B0%EA%B0%84%EB%8B%A4%EC%A0%95%EC%98%81%ED%9B%88?api_key=RGAPI-391dd36e-13cb-4370-9c69-606c9fe9a16f
		// domain = https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/{name}?api_key=
		
		// response에서 id 값 받아 와 전적검색 api 출력 
		// https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/ZNpxZhNhXW77QeRkX3BoPEAHsvm9USDX7vzUSkeo1foo-w?api_key=RGAPI-391dd36e-13cb-4370-9c69-606c9fe9a16f
		
		// domain = https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/{id}?api_key=
		String id = null;
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
	            br.close();
	            System.out.println(response.toString());
	        } catch (Exception e) {
	            System.out.println(e);
	        }
	}
}
