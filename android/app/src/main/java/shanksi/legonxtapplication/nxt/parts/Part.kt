package shanksi.legonxtapplication.nxt.parts

import shanksi.legonxtapplication.nxt.NxtBrick

abstract class Part(protected var brick: NxtBrick) {

    /**
     *
     */
    protected fun delay(ms: Int) {
        try {
            Thread.sleep(ms.toLong()) //1000 milliseconds is one second.
        } catch (ex: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    // Called when connected
    protected abstract fun init()

    // Called to cleanup
    protected abstract fun cleanup()
}