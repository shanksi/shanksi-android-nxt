package shanksi.legonxtapplication.nxt.parts

import shanksi.legonxtapplication.nxt.NxtBrick
import shanksi.legonxtapplication.nxt.NxtCommand
import shanksi.legonxtapplication.nxt.NxtCommandType
import java.io.IOException

class TonePlayer(brick: NxtBrick) : Part(brick) {
    override fun init() {}

    override fun cleanup() {}

    /**
     * Plays a tone with given frequency (in Hertz) and duration (in seconds).
     */
    fun playTone(frequency: Int, duration: Int) {
        val request = byteArrayOf(
            NxtCommandType.DIRECT_COMMAND_NOREPLY,
            NxtCommand.PLAY_TONE,
            frequency.toByte(),
            (frequency ushr 8).toByte(),
            duration.toByte(),
            (duration ushr 8).toByte()
        )
        brick.sendData(request)
    }

    /**
     * Plays a standard connect melody.
     */
    @Synchronized
    @Throws(IOException::class)
    fun playConnectMelody() {
        delay(1000)
        playTone(600, 100)
        delay(100)
        playTone(900, 100)
        delay(100)
        playTone(750, 200)
        delay(200)
    }

    /**
     * method uses playTone method
     * @param note is a String representation of the musical note in range C3-C6. See notes[] for allowed values
     * @param duration is note duration in ms
     */
    fun musicTone(note: String?, duration: Int) {
        try {
            if(noteMap.containsKey(note)) playTone(noteMap[note]!!.toInt(), duration)
            delay(duration)
        } catch (ex: IOException) {
        }
    }

    fun playGuido(guido: String) {
        val noteLength = 500
        val guidoNotes = guido.split(" ").toTypedArray()
        for (i in guidoNotes.indices) {
            val guidoSplit =
                guidoNotes[i].split("/").toTypedArray()
            if (guidoSplit.size > 1) {
                guidoDuration = guidoSplit[1].toInt()
            }
            musicTone(guidoSplit[0], noteLength / guidoDuration)
        }
    }

    private val guidoOctave = 1
    private var guidoDuration = 1

    private val notes = arrayOf(
        "C3", "C#3", "Db3", "D3", "D#3", "Eb3",
        "E3", "F3", "F#3", "Gb3", "G3", "G#3", "Ab3", "A3", "A#3", "Bb3",
        "B3", "C4", "C#4", "Db4", "D4", "D#4", "Eb4", "E4", "F4", "F#4",
        "Gb4", "G4", "G#4", "Ab4", "A4", "A#4", "Bb4", "B4", "C5", "C#5",
        "Db5", "D5", "D#5", "Eb5", "E5", "F5", "F#5", "Gb5", "G5", "G#5",
        "Ab5", "A5", "A#5", "Bb5", "B5", "C6"
    )

    private val frequency = floatArrayOf(
        130.81f, 138.59f, 138.59f, 146.83f,
        155.56f, 155.56f, 164.81f, 174.61f, 185.0f, 185.0f, 196.0f,
        207.65f, 207.65f, 220.0f, 233.08f, 233.08f, 246.94f, 261.63f,
        277.18f, 277.18f, 293.66f, 311.13f, 311.13f, 329.63f, 349.23f,
        369.99f, 369.99f, 392.0f, 415.3f, 415.3f, 440.0f, 466.16f, 466.16f,
        493.88f, 523.25f, 554.37f, 554.37f, 587.33f, 622.25f, 622.25f,
        659.26f, 698.46f, 739.99f, 739.99f, 783.99f, 830.61f, 830.61f,
        880.0f, 932.33f, 932.33f, 987.77f, 1046.5f
    )

    private val noteMap = createNoteMap()

    private fun createNoteMap() : HashMap<String, Float>  {
        var map = HashMap<String, Float>()
        for (i in notes.indices) {
            map[notes[i]] = frequency[i]
        }
        return map
    }
}