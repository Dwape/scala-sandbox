package exercises

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

// maybe we can remove user and group simply forwards message to every user
case class Message(text: String, author: String)

case class SendMessage(text: String, group: ActorRef)

case class Subscribe(user: ActorRef, name: String)

case class Unsubscribe(user: ActorRef, name: String)

case class UserJoined(user: String)

case class UserLeft(user: String)

class Group extends Actor {

  var users: Map[String, ActorRef] = Map()

  override def receive: Receive = {
    case Message(text, user) =>
      users.foreach{case (_,b) => b.forward(Message(text, user))} // should we send the message to the original sender?
    case Subscribe(user, name) =>
      users.foreach{case (_,b) => b ! UserJoined(name)}
      users = users + (name->user)
    case Unsubscribe(user, name) =>
      users = users - name
      users.foreach{case (_,b) => b ! UserLeft(name)}
  }
}

class User(name: String) extends Actor { //(name: String)

  override def receive: Receive = {
    case SendMessage(text, group) =>
      group ! Message(text, name)
    case Message(text, user) =>
      println(s"[$name] $user: $text")
    case UserJoined(user) =>
      println(s"[$name] $user joined the group")
    case UserLeft(user) =>
      println(s"[$name] $user left the group")
  }
}

object Whatscala extends App {
  val system = ActorSystem("chat")
  val group = system.actorOf(Props[Group], "group")
  val user = system.actorOf(Props(new User("Dwape")), "Dwape") // creates a user
  group ! Subscribe(user, "Dwape")
  user ! SendMessage("Hello", group) // remove user name?

  val user2 = system.actorOf(Props(new User("Dude")), "Dude")
  group ! Subscribe(user2, "Dude")

  user2 ! SendMessage("Hi", group)

  group ! Unsubscribe(user2, "Dude")
}
