package com.skapadia.akka.streams


import java.io.File
import java.util.concurrent.{ExecutorService, Executors}

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.concurrent._
import scala.concurrent.duration._

object StreamApp  {

  def lineSink(filename: String): Sink[String, Future[IOResult]] = {
    Flow[String]
      .alsoTo(Sink.foreach(s => println(s"$filename: $s")))
      .map(s => ByteString(s + "\n"))
      .toMat(FileIO.toFile(new File(filename)))(Keep.right)
  }

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem("streamapp")
    implicit val materializer = ActorMaterializer()

    val source: Source[Int, NotUsed] = Source(1 to 100)
    val factorials: Source[BigInt, NotUsed] = source.scan(BigInt(1))((acc, next) => acc * next)
    val sink1 = lineSink("factorial1.txt")
    val sink2 = lineSink("factorial2.txt")
    val slowSink2 = Flow[String].via(Flow[String].throttle(1, 1.second, 1, ThrottleMode.shaping)).toMat(sink2)(Keep.right)
    val bufferedSink2 = Flow[String].buffer(50, OverflowStrategy.backpressure).via(Flow[String].throttle(1, 1.second, 1, ThrottleMode.shaping)).toMat(sink2)(Keep.right)

    val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
      import GraphDSL.Implicits._

      val bcast = b.add(Broadcast[String](2))
      factorials.map(_.toString) ~> bcast.in
      bcast.out(0) ~> sink1
      bcast.out(1) ~> bufferedSink2
      ClosedShape
    })

    g.run()
  }
}
