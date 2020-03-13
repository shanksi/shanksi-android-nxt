package shanksi.legonxtapplication.nxt

import android.util.Log

class InputValues {
    var inputPort = 0

    var valid = true
    var isCalibrated = false
    var sensorType = 0
    var sensorMode = 0

    var rawADValue = 0
    var normalizedADValue = 0
    var scaledValue: Short = 0
    var calibratedValue: Short = 0

    fun printValues(tag:String) {
        Log.v(tag, "  input port: $inputPort")
        Log.v(tag, "  valid: $valid")
        Log.v(tag, "  isCalibrated: $isCalibrated")
        Log.v(tag, "  sensorType: $sensorType")
        Log.v(tag, "  sensorMode: $sensorMode")
        Log.v(tag, "  rawADValue: $rawADValue")
        Log.v(tag, "  normalizedADValue: $normalizedADValue")
        Log.v(tag, "  scaledValue: $scaledValue")
        Log.v(tag, "  calibratedValue: $calibratedValue")
    }
}