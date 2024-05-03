package com.shubhasai.indoornavigation.models

data class Location(val floor: Int, val x: Float, val y: Float, val name: String? = null, val iconId: Int? = null)
data class Path(val name: String, val floor: Int, val pathPoints: MutableList<Location>)
