package controllers

import javax.inject._

import com.datastax.driver.dse._
import com.datastax.driver.dse.graph.GraphOptions
import play.api.libs.json.Json
import play.api.mvc._

@Singleton
class DseController @Inject() extends Controller {
  def getBooksAuthored(authorName: String) = Action {
    val iter = getSession().executeGraph(s"g.V().has('name','$authorName').outE('authored').inV()").iterator()
    var hMap = Map.empty[String, Map[String,String]]
    while (iter.hasNext) {
      val v = iter.next().asVertex()
      hMap += (v.getId().get("member_id").toString -> Map("name" -> v.getProperty("name").getValue().toString, "year" -> v.getProperty("year").getValue().toString))
    }
    Ok(Json.toJson(hMap))
  }

  def getBooksAuthoredCount(authorName: String) = Action {
    val iter = getSession().executeGraph(s"g.V().has('name','$authorName').outE('authored')").iterator()
    var count = 0
    while (iter.hasNext) {
      val p = iter.next()
      count += 1
    }
    Ok(Json.toJson(count))
  }

  def getAuthor(authorName: String) = Action {
    val v = getSession().executeGraph(s"g.V().has('author','name','$authorName')").one().asVertex()
    var hMap = Map.empty[String, String]
    val iter = v.getProperties
    while (iter.hasNext) {
      val p = iter.next()
      hMap += (p.getName -> p.getValue.asString())
    }
    Ok(Json.toJson(hMap))
  }

  def getCount = Action {
    val count = getSession().executeGraph("g.V().count()").one().asInt()
    Ok(Json.toJson(count))
  }

  def printUser(session: DseSession, id: Long, stp: String): Unit = {
    val n = session.executeGraph("g.V(['~label':'Person','personId':" + id + ",'soldToParty':'" + stp + "" + "'])").one
    val v = n.asVertex
    System.out.println(v.getId.get("personId"))
  }

  def getSession(): DseSession = {
    DseCluster.builder.addContactPoint("127.0.0.1")
      .withGraphOptions(new GraphOptions().setGraphName("DSE_GRAPH_QUICKSTART")).build.connect()
  }
}
