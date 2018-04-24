package controllers

import javax.inject.Inject
import play.api.mvc._

// Health check
class PingController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def ping() = Action { implicit request: Request[AnyContent] =>
    Ok("pong")
  }
}
