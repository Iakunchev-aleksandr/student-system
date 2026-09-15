package com.school.web;

import org.springframework.stereotype.Component;

// Перевод оценок между внутренним числовым представлением (2..5) и буквенным (A..D).
// A=5 (лучшая), B=4, C=3, D=2.
@Component("gradeScale")
public class GradeScale {

    // Буквы от лучшей к худшей — для выпадающих списков.
    public static final String[] LETTERS = {"A", "B", "C", "D"};

    public static String toLetter(Integer value) {
        if (value == null) {
            return null;
        }
        return switch (value) {
            case 5 -> "A";
            case 4 -> "B";
            case 3 -> "C";
            case 2 -> "D";
            default -> String.valueOf(value);
        };
    }

    public static Integer toValue(String letter) {
        if (letter == null) {
            return null;
        }
        return switch (letter.trim().toUpperCase()) {
            case "A" -> 5;
            case "B" -> 4;
            case "C" -> 3;
            case "D" -> 2;
            default -> null;
        };
    }

    // Инстанс-методы (используются в контроллере).
    public String letter(Integer value) { return toLetter(value); }
    public Integer value(String letter) { return toValue(letter); }
    public String[] letters() { return LETTERS; }
}
