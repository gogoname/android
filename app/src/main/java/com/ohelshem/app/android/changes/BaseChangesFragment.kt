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

package com.ohelshem.app.android.changes

import com.ohelshem.api.model.Change
import com.ohelshem.api.model.UpdateError
import com.ohelshem.app.android.drawableRes
import com.ohelshem.app.android.stringResource
import com.ohelshem.app.android.utils.BaseMvpFragment
import com.ohelshem.app.android.utils.BasePresenter
import com.ohelshem.app.controller.timetable.TimetableController.Companion.DayType
import com.yoavst.changesystemohelshem.R
import kotlinx.android.synthetic.main.list_fragment.*

abstract class BaseChangesFragment<K> : ChangesView, BaseMvpFragment<ChangesView, K>() where K : BasePresenter<ChangesView>, K : IBaseChangesPresenter {
    override val layoutId: Int = R.layout.list_fragment

    private val day by stringResource(R.string.day)
    private val weekDays by lazy { resources.getStringArray(R.array.week_days) }

    override final fun setData(changes: List<Change>) {
        progressActivity.showContent()
        showData(changes)
    }

    abstract fun showData(changes: List<Change>)

    override fun onError(error: UpdateError) = Unit

    override fun onEmptyData(dayType: DayType) {
        recyclerView?.adapter = null
        if (progressActivity.isError)
            progressActivity.showContent()
        if (dayType == DayType.Holiday || dayType == DayType.Summer) {
            progressActivity.showEmpty(drawableRes(R.drawable.ic_beach), getString(R.string.holiday_today), getString(R.string.holiday_today_subtitle))
        } else {
            when (dayType) {
                DayType.Saturday -> progressActivity.showEmpty(drawableRes(R.drawable.ic_beach), getString(R.string.shabat_today), getString(R.string.shabat_today_subtitle))
                DayType.Friday -> progressActivity.showEmpty(drawableRes(R.drawable.ic_beach), getString(R.string.friday_today), getString(R.string.friday_today_subtitle))
                else -> progressActivity.showError(drawableRes(R.drawable.ic_error), getString(R.string.no_changes), getString(R.string.no_changes_subtitle), getString(R.string.go_to_timetable)) {
                    presenter.launchTimetableScreen(screenManager)
                }
            }
        }
    }

    override val isShowingData: Boolean
        get() = progressActivity?.isContent == true
}