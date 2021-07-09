import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods.GET
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpRequest, Uri}
import akka.http.scaladsl.server.{HttpApp, Route}
import akka.http.scaladsl.settings.ServerSettings
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.{Failure, Success}

object SimpleHttpServerApp1 extends HttpApp {
  override def routes: Route =
    pathPrefix("simple") {
      path("num" / IntNumber) { num =>
        get {
          implicit val system = ActorSystem()
          implicit val materializer = ActorMaterializer()
          import system.dispatcher

          val baseUrl = "http://localhost:9000/simple/num/"
          val data = Seq(("GET", "1" , ""))
          val reqs = data.map { case (a, b, c) =>
            HttpRequest(GET, Uri(baseUrl + b), Nil)
          }
          Future.traverse(reqs)(Http().singleRequest(_)) andThen {
            case Success(resps) => resps.foreach(resp =>
              resp.entity.toStrict(5 seconds).map(_.data.utf8String).andThen {
                case Success(content) => println(content)
                case _ => println("Error")
              })
            case Failure(err) => println(s"Request failed $err")
          }
          println("Received request")
          complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, s"sending response with $num"))
        }
      }
    }
}

object SimpleHttpServer1 extends App {
  SimpleHttpServerApp1.startServer("localhost", 8000, ServerSettings(ConfigFactory.load))
}

//ab -c 1 -n 10 -p /Users/rpadmasr/Desktop/myFile.json -T application/json http://127.0.0.1:9000/simple/num/1
//wrk -t10 -c10 -d30s --latency http://127.0.0.1:9000/simple/num/1