package controllers


import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import play.api.db.Databases

class URLControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "URLController GET" should {
    Databases.withDatabase("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/surly", "test", Map("username" -> "root")) {
      db =>
        "not find a non-existent short URL" in {
          val controller = new URLController(db, stubControllerComponents())
          val resp = controller.get(999).apply(FakeRequest(GET, "/999"))

          status(resp) mustBe NOT_FOUND
        }
    }
  }
}
