package com.example.dices2

import java.util.*

class Player(val name: String) : Comparable<Player> {
    private val values: MutableMap<RowName, Int>
    var isSecondNonSchoolPartAdded = false
    var curPlace = 0

    init {
        values = HashMap()
    }

    val school: Int
        get() {
            var result = 0
            for (rowName in Consts.NAMES_OF_SCHOOL_ROWS) {
                val value = values[rowName]
                if (value != null) {
                    result += value
                }
            }
            if (isSchoolFinished && result < 0) {
                result -= 50
            }
            return result
        }

    val total: Int
        get() {
            var result = 0
            for (rowName in Consts.NAMES_OF_ROWS_WITHOUT_SCHOOL) {
                val value = values[rowName]
                if (value != null) {
                    result += value
                }
            }
            return result + school
        }

    val isSchoolFinished: Boolean
        get() {
            for (schoolClass in Consts.NAMES_OF_SCHOOL_ROWS) {
                if (values[schoolClass] == null) {
                    return false
                }
            }
            return true
        }

    val isThreeClassesInSchoolAreFinished: Boolean
        get() {
            var count = 0
            for (schoolClass in Consts.NAMES_OF_SCHOOL_ROWS) {
                if (values[schoolClass] != null) {
                    count++
                }
            }
            return count >= 3
        }

    fun getValue(rowName: RowName): Int {
        return values[rowName]?:0
    }

    fun setValue(rowName: RowName, value: String) {
        if (value == "") {
            values.remove(rowName)
        } else {
            values[rowName] = getIntValueFromString(value = value)
        }
    }

    private fun getIntValueFromString(value: String): Int {
        return if (value == "-" || value == "+") {
            0
        } else if (value[0] == '+') {
            value.substring(1).toInt()
        } else {
            value.toInt()
        }
    }

    fun clearAllValues() {
        values.clear()
    }

    fun clearPlace() {
        curPlace = 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Player) return false
        return name == other.name
    }

    override fun hashCode(): Int {
        return Objects.hash(name)
    }

    override fun compareTo(other: Player): Int {
        val temp = total
        return temp.compareTo(other.total)
    }
}