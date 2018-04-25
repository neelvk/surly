package controllers

import java.sql.{Connection, Statement}

import com.terway.base.ShortenRequest
import im.duk.base62.Base62
import javax.inject.Inject
import play.api.Configuration
import play.api.db._
import play.api.mvc._

class URLController @Inject()(config:Configuration, db: Database, cc: ControllerComponents) extends AbstractController(cc) {

  private val baseURL = config.get[String]("base.url")

  val base62 = new Base62()

  def get(id: String) = Action { implicit request: Request[AnyContent] =>
    val idNum = base62.decodeBase62(id)
    db.withConnection {
      conn =>
        val stmt = conn.prepareStatement("SELECT target FROM urls WHERE id = ?")
        stmt.setLong(1, idNum)
        val rs = stmt.executeQuery()
        if (rs.next()) {
          val target = rs.getString("target")
          TemporaryRedirect(target) // May not work on all browsers, sends 307 not 302
        } else {
          NotFound
        }
    }
  }

  def create() = Action { implicit request: Request[AnyContent] =>
    val json = request.body.asJson.map {
      _.as[ShortenRequest]
    }

    json match {
      case Some(sr:ShortenRequest) =>
        db.withConnection {
          conn =>
            val query = conn.prepareStatement("SELECT id FROM urls WHERE target = ?")
            query.setString(1, sr.target)
            val rs = query.executeQuery()
            if (rs.next) {
              val id = rs.getLong("id")
              Ok(s"$baseURL${base62.encodeBase10(id)}")
            } else {
              createEntry(sr)(conn)
            }
        }

      case _ => BadRequest
    }
  }

  private def createEntry(sr: ShortenRequest)(implicit conn: Connection) = {
    val stmt = conn.prepareStatement("INSERT INTO urls (target) VALUES (?)", Statement.RETURN_GENERATED_KEYS)
    stmt.setString(1, sr.target)
    val status = stmt.executeUpdate()
    if (status == 0) {
      InternalServerError
    } else {
      val genKeys = stmt.getGeneratedKeys
      if(genKeys.next()) {
        val id = genKeys.getLong(1)
        Ok(s"$baseURL${base62.encodeBase10(id)}")
      } else {
        InternalServerError
      }
    }
  }
}
