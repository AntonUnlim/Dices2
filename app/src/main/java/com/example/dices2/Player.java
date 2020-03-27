package com.example.dices2;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.dices2.Consts.*;

public class Player {
    private String name;
    private Map<String, Integer> values;

    public Player(String name) {
        this.name = name;
        values = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public int getValue(String rowName) {
        return values.get(rowName);
    }

    public void setValue(String rowName, String value) {

        values.put(rowName, getIntValueFromString(value));
    }

    public int getSchool() {
        int result = 0;
        for (String rowName : namesOfSchoolRows) {
            Integer value = values.get(rowName);
            if (value != null) {
                result += value;
            }
        }
        if (isSchoolFinished() && result < 0) {
            result -= 50;
        }
        return result;
    }

    public int getTotal() {
        int result = 0;
        for (String rowName : namesOfRowsWithoutSchool) {
            Integer value = values.get(rowName);
            if (value != null) {
                result += value;
            }
        }
        return result + getSchool();
    }

    private int getIntValueFromString(String value) {
        if (value.equals("-") || value.equals("+") || value.equals("")) {
            return 0;
        } else if (value.charAt(0) == '+') {
            return Integer.parseInt(value.substring(1));
        } else {
            return Integer.parseInt(value);
        }
    }

    public boolean isSchoolFinished() {
        for (String schoolClass : namesOfSchoolRows) {
            if (values.get(schoolClass) == null) {
                return false;
            }
        }
        return true;
    }

    public void clearAllValues() {
        values.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
