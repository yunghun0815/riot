package com.example.demo.service.parse;

public class Spell {
	public String spell(long key) {
		String spell = null;
		switch ((int) key) {
		case 1:
			spell = "SummonerBoost.png"; // 정화
			break;
		case 3:
			spell = "SummonerExhaust.png"; // 탈진
			break;
		case 4:
			spell = "SummonerFlash.png"; // 점멸
			break;
		case 6:
			spell = "SummonerHaste.png"; // 유체화
			break;
		case 7:
			spell = "SummonerHeal.png"; // 회복
			break;
		case 11:
			spell = "SummonerSmite.png"; // 강타
			break;
		case 12:
			spell = "SummonerTeleport.png"; // 순간이동
			break;
		case 13:
			spell = "SummonerMana.png"; // 총명
			break;
		case 14:
			spell = "SummonerDot.png"; // 점화
			break;
		case 21:
			spell = "SummonerBarrier.png"; // 방어막
			break;
		case 30:
			spell = "SummonerPoroRecall.png"; // 포로귀환
			break;
		case 31:
			spell = "SummonerPoroThrow.png"; // 포로던지기
			break;
		case 32:
			spell = "SummonerSnowball.png"; // 눈덩이
			break;
		case 39:
			spell = "SummonerSnowURFSnowball_Mark.png"; // 눈덩이
			break;
		default:
			break;
		}
		return spell;
	}
}
