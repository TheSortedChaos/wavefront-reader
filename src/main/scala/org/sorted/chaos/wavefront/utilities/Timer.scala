package org.sorted.chaos.wavefront.utilities

final case class Timer(start: Long, end: Long)

object Timer {
  def start: Timer = {
    val start = System.currentTimeMillis()
    Timer(start, 0L)
  }

  def end(timer: Timer, caption: String): Timer = {
    val end      = System.currentTimeMillis()
    val duration = end - timer.start
    println(s"$caption took $duration ms (= ${duration / 1000} s)")
    Timer(timer.start, end)
  }
}
