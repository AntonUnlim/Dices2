package com.example.dices2;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.dices2.Consts.*;

public class Player implements Comparable<Player> {
    private String name;
    private Map<RowName, Integer> values;
    private boolean isSecondNonSchoolPartAdded = false;

    public Player(String name) {
        this.name = name;
        values = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public int getValue(RowName rowName) {
        return values.get(rowName);
    }

    public void setValue(RowName rowName, String value) {
        if (value.equals("")) {
            values.remove(rowName);
        } else {
            values.put(rowName, getIntValueFromString(value));
        }
    }

    public boolean isSecondNonSchoolPartAdded() {
        return isSecondNonSchoolPartAdded;
    }

    public void setSecondNonSchoolPartAdded(boolean secondNonSchoolPartAdded) {
        isSecondNonSchoolPartAdded = secondNonSchoolPartAdded;
    }

    public int getSchool() {
        int result = 0;
        for (RowName rowName : NAMES_OF_SCHOOL_ROWS) {
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
        for (RowName rowName : namesOfRowsWithoutSchool) {
            Integer value = values.get(rowName);
            if (value != null) {
                result += value;
            }
        }
        return result + getSchool();
    }

    private int getIntValueFromString(String value) {
        if (value.equals("-") || value.equals("+")) {
            return 0;
        } else if (value.charAt(0) == '+') {
            return Integer.parseInt(value.substring(1));
        } else {
            return Integer.parseInt(value);
        }
    }

    public boolean isSchoolFinished() {
        for (RowName schoolClass : NAMES_OF_SCHOOL_ROWS) {
            if (values.get(schoolClass) == null) {
                return false;
            }
        }
        return true;
    }

    public boolean isThreeClassesInSchoolAreFinished() {
        int count = 0;
        for (RowName schoolClass : NAMES_OF_SCHOOL_ROWS) {
            if (values.get(schoolClass) != null && !values.get(schoolClass).equals("")) {
                count++;
            }
        }
        return count >= 3;
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

    @Override
    public int compareTo(Player player) {
        Integer temp = getTotal();
        return temp.compareTo(player.getTotal());
    }
}
