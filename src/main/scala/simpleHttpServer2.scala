import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.{HttpApp, Route}
import akka.http.scaladsl.settings.ServerSettings
import com.typesafe.config.ConfigFactory

object SimpleHttpServerApp2 extends HttpApp {
  override def routes: Route =
    pathPrefix("simple") {
      path("num" / IntNumber) { num =>
        get {
            println("Received get request")
            complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, s"sending response $num"))
          }
        }
      }
}

object SimpleHttpServer2 extends App {
  SimpleHttpServerApp2.startServer("localhost", 9000, ServerSettings(ConfigFactory.load))
}

//ab -c 1 -n 10 -p /Users/rpadmasr/Desktop/myFile.json -T application/json http://127.0.0.1:9000/simple/num/1
//wrk -t10 -c10 -d30s --latency http://127.0.0.1:9000/simple/num/1