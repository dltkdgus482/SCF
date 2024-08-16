package com.scf.user.profile.domain.dto;

import java.util.Map;
import java.util.StringJoiner;


public class ChoiceTextConverter {

    // 객관식
    public static String MultipleChoice(Map<Integer, Integer> choiceTextMap) {
        // 선택하지 않았을 경우
        if (choiceTextMap == null || choiceTextMap.isEmpty()) {
            return null;
        }
        return choiceTextMap.entrySet().iterator().next().getKey() + "="
            + choiceTextMap.entrySet().iterator().next().getValue();
    }


    // 빈칸채우기
    public static String FillInBlank(Map<Integer, Integer> choiceTextMap) {
        // 전부 입력이 안 된 경우
        if (choiceTextMap == null || choiceTextMap.isEmpty()) {
            return null;
        }
        StringJoiner joiner = new StringJoiner(";");
        for (Map.Entry<Integer, Integer> entry : choiceTextMap.entrySet()) {
            joiner.add(entry.getKey() + "=" + entry.getValue());
        }

        return joiner.toString();

    }

    // 주관식
    public static String ShortAnswer(String answer) {
        // answer 입력이 안 되었을 경우
        if (answer.length() == 0 || answer == null) {
            return null;
        }
        return answer;
    }


}
