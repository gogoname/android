/*
 * Copyright 2016 Yoav Sternberg.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ohelshem.app.android.changes.layer

import com.ohelshem.api.model.Change
import com.ohelshem.app.android.changes.BaseChangesPresenter
import com.ohelshem.app.controller.info.SchoolInfo
import com.ohelshem.app.controller.storage.Storage
import com.ohelshem.app.controller.timetable.TimetableController

class LayerChangesPresenter(private val schoolInfo: SchoolInfo, storage: Storage, timetableController: TimetableController) : BaseChangesPresenter(storage, timetableController) {
    override fun List<Change>.isConsideredAsChanges(): Boolean = true

    override fun List<Change>.filterNeeded(): List<Change> = this

    val classesAtLayer: Int
        get() = schoolInfo[storage.userData.layer]

    val userLayer: Int
        get() = storage.userData.layer
}