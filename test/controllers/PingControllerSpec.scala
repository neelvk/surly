package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._

class PingControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "PingController GET" should {

    "send back a pong" in {
      val controller = new PingController(stubControllerComponents())
      val home = controller.ping().apply(FakeRequest(GET, "/w/ping"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/plain")
      contentAsString(home) must include("pong")
    }
  }
}
