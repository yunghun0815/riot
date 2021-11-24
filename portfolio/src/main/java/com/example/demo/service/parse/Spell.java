package com.example.demo.service.parse;

public class Spell {
	public String spell(int key) {
		String spell = null;
		switch (key) {
		case 1:
			spell = "SummonerBoost.png"; // 정화
		case 3:
			spell = "SummonerExhaust.png"; // 탈진
		case 4:
			spell = "SummonerFlash.png"; // 점멸
		case 6:
			spell = "SummonerHaste.png"; // 유체화
		case 7:
			spell = "SummonerHeal.png"; // 회복
		case 11:
			spell = "SummonerSmite.png"; // 강타
		case 12:
			spell = "SummonerTeleport.png"; // 순간이동
		case 13:
			spell = "SummonerMana.png"; // 총명
		case 14:
			spell = "SummonerDot.png"; // 점화
		case 21:
			spell = "SummonerBarrier.png"; // 방어막
		case 30:
			spell = "SummonerPoroRecall.png"; // 포로귀환
		case 31:
			spell = "SummonerPoroThrow.png"; // 포로던지기
		case 32:
			spell = "SummonerSnowball.png"; // 눈덩이
		case 39:
			spell = "SummonerSnowURFSnowball_Mark.png"; // 눈덩이
		default:
			break;
		}
		return spell;
	}
}
