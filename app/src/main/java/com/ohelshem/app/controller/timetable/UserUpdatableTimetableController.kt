package com.ohelshem.app.controller.timetable

import com.ohelshem.api.model.Hour
import com.ohelshem.app.controller.storage.SharedStorage

class UserUpdatableTimetableController(private val storage: SharedStorage) : BaseTimetableController() {
    override var hasData: Boolean = false
        protected set

    override fun init() {
        storage.attachTimetableListener(1) { onUpdate(it) }
        storage.timetable?.let { onUpdate(it) }
    }

    fun onUpdate(data: Array<Array<Hour>>) {
        timetable = data
        hasData = data.any { it.isNotEmpty() }
    }
}