package controllers

import java.sql.{Connection, Statement}

import com.terway.base.ShortenRequest
import javax.inject.Inject
import play.api.db._
import play.api.mvc._

class URLController @Inject()(db: Database, cc: ControllerComponents) extends AbstractController(cc) {

  def get(id: Long) = Action { implicit request: Request[AnyContent] =>
    db.withConnection {
      conn =>
        val stmt = conn.prepareStatement("SELECT target FROM urls WHERE id = ?")
        stmt.setLong(1, id)
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
              Ok(rs.getLong("id").toString)
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
        Ok(id.toString)
      } else {
        InternalServerError
      }
    }
  }
}
