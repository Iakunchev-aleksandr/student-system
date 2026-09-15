package com.school.web;

import org.springframework.stereotype.Component;

// Перевод оценок между внутренним числовым представлением (2..5) и буквенным (A..D).
// A=5 (лучшая), B=4, C=3, D=2. Доступен в шаблонах как @gradeScale.
@Component("gradeScale")
public class GradeScale {

    public String letter(Integer value) {
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

    public Integer value(String letter) {
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

    // Список букв для выпадающих списков (от лучшей к худшей).
    public String[] letters() {
        return new String[] {"A", "B", "C", "D"};
    }
}
