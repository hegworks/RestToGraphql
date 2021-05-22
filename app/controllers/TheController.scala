package controllers

import javax.inject._
import play.api.mvc._
import repositories.TheRepository

import scala.concurrent.ExecutionContext

import reactivemongo.bson.BSONObjectID
import play.api.libs.json.{Json, __}
import scala.util.{Failure, Success}
import scala.concurrent.{ExecutionContext, Future}

import models.TheModel
import play.api.libs.json.JsValue

/** create the endpoints to expose the actions for the theModels repository.
  *
  * @param executionContext
  * @param theModelRepository
  * @param controllerComponents
  */
@Singleton
class TheController @Inject() (implicit
    executionContext: ExecutionContext,
    val theRepository: TheRepository,
    val controllerComponents: ControllerComponents
) extends BaseController {
  // actions:

  /** the two endpoints responsible of reading data:
    *
    * this will return the theModel list
    *
    * @return
    */
  def findAll(): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      theRepository.findAll().map { theModels =>
        Ok(Json.toJson(theModels))
      }
  }

  /** this will parse the given id and return the associated theModel if it’s found.
    *
    * @param id
    * @return
    */
  def findOne(id: String): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      val objectIdTryResult = BSONObjectID.parse(id)
      objectIdTryResult match {
        case Success(objectId) =>
          theRepository.findOne(objectId).map { theModel =>
            Ok(Json.toJson(theModel))
          }
        case Failure(_) =>
          Future.successful(BadRequest("Cannot parse the theModel id"))
      }
  }

  /** validating the id passed in an argument
    * and check if the json is valid by using the validate helper in the request body.
    *
    * Thanks to the json serialization macro,
    * the Scala object can be serialized implicitly from json and vise-versa.
    *
    * @return
    */
  def create(): Action[JsValue] =
    Action.async(controllerComponents.parsers.json) { implicit request =>
      {

        request.body
          .validate[TheModel]
          .fold(
            _ => Future.successful(BadRequest("Cannot parse request body")),
            theModel =>
              theRepository.create(theModel).map { _ =>
                Created(Json.toJson(theModel))
              }
          )
      }
    }

  def update(id: String): Action[JsValue] =
    Action.async(controllerComponents.parsers.json) { implicit request =>
      {
        request.body
          .validate[TheModel]
          .fold(
            _ => Future.successful(BadRequest("Cannot parse request body")),
            theModel => {
              val objectIdTryResult = BSONObjectID.parse(id)
              objectIdTryResult match {
                case Success(objectId) =>
                  theRepository.update(objectId, theModel).map { result =>
                    Ok(Json.toJson(result.ok))
                  }
                case Failure(_) =>
                  Future.successful(BadRequest("Cannot parse the theModel id"))
              }
            }
          )
      }
    }

  def delete(id: String): Action[AnyContent] = Action.async {
    implicit request =>
      {
        val objectIdTryResult = BSONObjectID.parse(id)
        objectIdTryResult match {
          case Success(objectId) =>
            theRepository.delete(objectId).map { _ =>
              NoContent
            }
          case Failure(_) =>
            Future.successful(BadRequest("Cannot parse the theModel id"))
        }
      }
  }
}

/*
without mongodb

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models.{TheModel}

class TheController @Inject() (cc: ControllerComponents)
    extends AbstractController(cc) {

  implicit val TheModelFormat = Json.format[TheModel]

  // make a TheModel and show it in Json format
  def getAll = Action {
    val themodel = new TheModel(1, "name1", "descriptions of name 1")
    Ok(Json.toJson(themodel))
  }

  // add 2 given numbers
  def add(num1: Int, num2: Int) =  Action {
    var result = num1 + num2
	Ok("" + num1 + " + " + num2 + " = " + result + "")
  }
}

 */
