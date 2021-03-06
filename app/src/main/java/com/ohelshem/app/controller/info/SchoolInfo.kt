package com.ohelshem.app.controller.info

import com.ohelshem.api.model.ClassInfo

interface SchoolInfo {

    /**
     * Returns how many classes there are in a layer.
     */
    operator fun get(layer: Int): Int

    /**
     * Validate that the given layer and class exists.
     */
    fun validate(layer: Int, clazz: Int): Boolean

    fun getAbsoluteClass(layer: Int, clazz: Int): Int {
        var absolute = (MinLayer until layer).sumBy { get(it) }
        absolute += clazz

        return absolute - 1
    }

    val allClasses: List<ClassInfo>

    companion object {
        const val MinLayer = 9
        const val MaxLayer = 12
    }
}