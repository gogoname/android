package com.ohelshem.app.controller.timetable

import com.ohelshem.api.model.Hour
import com.ohelshem.app.controller.storage.SharedStorage
import com.ohelshem.app.model.HourData
import com.ohelshem.app.model.OverrideData
import com.ohelshem.app.model.WrappedHour
import org.jetbrains.anko.collections.forEachReversedWithIndex
import java.util.*

class OverridableUserTimetableController(internal val timetableController: BaseTimetableController, private val storage: SharedStorage)
    : TimetableController by timetableController {
    private var overrideTimetable: Array<Array<Hour>>? = null

    override var hasData: Boolean = false
        private set

    override fun init() {
        storage.attachOverridesListener(1) {
            onUpdate(it)
        }
        timetableController.timetableUpdatedCallback = {
            onUpdate(storage.overrides)
        }
        timetableController.init()
    }

    override fun get(day: Int, hour: Int): Hour = this[day][hour]

    override fun get(day: Int): Array<Hour> {
        val timetableForDay = overrideTimetable!![day]
        val temp = timetableForDay.dropLastWhile(Hour::isEmpty)
        return temp.toTypedArray()
    }

    override fun getHourData(day: Int, hour: Int, minutesNow: Int): HourData = timetableController.getHourDataFromTimetable(day, hour, minutesNow, overrideTimetable!!)

    private fun onUpdate(data: List<OverrideData>) {
        val daysOfWeek = timetableController.size - 1
        val goodOverrides = data.toMutableList()
        data.forEachReversedWithIndex { i, (day, hour) ->
            if (day > daysOfWeek || hour >= timetableController[day].size)
                goodOverrides.removeAt(i)
        }
        if (goodOverrides.size != data.size) {
            storage.overrides = goodOverrides
        } else {
            val colors = timetableController.colors
            val lessons = HashMap<String, Int>(20)
            var c = 0
            val timetable = MutableList(timetableController.size) {
                ArrayList<Hour>(timetableController[it].size).apply {
                    for (hour in 0 until timetableController[it].size) {
                        val original = timetableController[it][hour]
                        val override = data[it, hour]
                        val lesson = override?.newName ?: original.name
                        val teacher = override?.newTeacher ?: original.teacher
                        if (lesson.isBlank()) {
                            add(WrappedHour("", "", original.name, original.teacher, TimetableController.ColorEmpty, override?.newRoom ?: 0))
                        } else {
                            var color = lessons[lesson]
                            if (color == null) {
                                if (c + 1 == colors.size) c = 0
                                color = colors[c++]
                                lessons[lesson] = color
                            }
                            if (override == null)
                                add(Hour(lesson, teacher, color))
                            else add(WrappedHour(override.newName, override.newTeacher, original.name, original.teacher, color, override.newRoom))
                        }
                    }
                }
            }

            if (timetable.size == 6 && timetable[5].size == 0) timetable.removeAt(5)
            overrideTimetable = timetable.map(List<Hour>::toTypedArray).toTypedArray()
            hasData = lessons.size > 0
        }
    }

    private operator fun List<OverrideData>.get(day: Int, hour: Int): OverrideData? = firstOrNull { it.day == day && it.hour == hour }

}