package shanksi.legonxtapplication.nxt.parts

class Port(val portId: Int, val label: String ) {
    companion object {
        val motorA: Port = Port(0, "A")
        val motorB: Port = Port(1, "B")
        val motorC: Port = Port(2, "C")

        val sensor1: Port = Port(0, "S1")
        val sensor2: Port = Port(1, "S2")
        val sensor3: Port = Port(2, "S3")
        val sensor4: Port = Port(3, "S4")
    }
}