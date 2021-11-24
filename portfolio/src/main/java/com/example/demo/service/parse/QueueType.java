package com.example.demo.service.parse;

public class QueueType {
	public String queue(int num) {
		//큐타입 참조
		//https://static.developer.riotgames.com/docs/lol/queues.json 
		String queue = null;
		switch (num) {
			case 400: case 430:
				queue = "일반게임";
				break;
			case 420:
				queue = "솔로랭크";
				break;
			case 440:
				queue = "자유랭크";
				break;
			case 450:
				queue = "칼바람나락";
				break;
			case 800: case 810: case 820: case 830: case 840: case 850:
				queue = "봇전";
				break;
			case 900:
				queue = "우르프";
				break;
			case 920:
				queue = "포로왕";
				break;
			case 1020:
				queue = "단일챔피언";
				break;
			case 2000: case 2010: case 2020:
				queue = "튜토리얼";
				break;
			default:
				queue = "이벤트";
				break;
		}
		
		return queue;
	}
}
