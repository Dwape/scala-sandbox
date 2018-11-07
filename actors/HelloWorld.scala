package actors

import akka.actor._

// Within an actor only one message is processed at a time

// Actor functionality with Akka

class HelloWorld extends Actor {

  override def receive: Receive = {

    case msg =>
      println(msg)

  }
}

object Test extends App {
  val system = ActorSystem("mySystem")

  val ref: ActorRef = system.actorOf(Props[HelloWorld], "hello")

  // Two ways of sending messages
  //ref.tell() // Message and sender
  //ref.!("Hello") // Message
  ref ! "Hello"
}
