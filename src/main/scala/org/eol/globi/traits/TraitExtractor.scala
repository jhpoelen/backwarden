package org.eol.globi.traits

import java.io.{InputStreamReader, File}

import com.github.tototoshi.csv.CSVReader
import dispatch._, Defaults._
import org.anormcypher.{Cypher, Neo4jREST}
import play.api.libs.json.Json

import scala.io.Source

object TraitExtractor {

  def likelyPageIdsForINaturalistTaxa(limit: Long) = {
    val query = Cypher(
      """START study = node:studies('title:INAT*') WITH study MATCH study-[:COLLECTED]->specimen-[:CLASSIFIED_AS]->taxon WHERE has(taxon.externalId) and taxon.externalId =~ 'EOL:.*' WITH distinct(taxon.externalId) as externalId RETURN replace(externalId, 'EOL:', '') as eolPageId""")
    query.apply().map { _[String]("eolPageId") }
  }

  implicit def connection: Neo4jREST = {
    Neo4jREST("api.globalbioticinteractions.org", 7474, "/db/data/")
  }

  case class Trait(id: Option[String], data_point_uri_id: Option[Long])

  def traitsForPageId(eolPageId: String): String = {
    val urlString = "http://eol.org/api/traits/" + eolPageId
    System.err.println(urlString)
    val response = Http(url(urlString) OK as.String)
    response()
  }

  def extractGloBITraits(jsonTraits: String): List[(String, String)] = {
    implicit val peopleReader = Json.reads[Trait]
    val traits = (Json.parse(jsonTraits.replaceAll("@id", "id")) \ "@graph").as[List[Trait]]

    val taxonTraits = traits.filter { someTrait => """http://eol.org/pages.*""".r.findFirstIn(someTrait.id.getOrElse("")) != None}
    val taxonPageURL = taxonTraits.head.id

    def isGloBITrait(someTrait: Trait): Boolean = {
      """globi:assoc:""".r.findFirstIn(someTrait.id.getOrElse("")) != None
    }
    val traitsWithIdAndURI = traits.filter { someTrait => isGloBITrait(someTrait) && someTrait.data_point_uri_id != None}
    traitsWithIdAndURI.map { someTrait => (someTrait.id.getOrElse(""), taxonPageURL.getOrElse("") + "/data#data_point_" + someTrait.data_point_uri_id.getOrElse(""))}
  }

  def printProviderAndConsumer(lookup: List[(String, String)], printer: (String) => Unit) = {
     val reader = CSVReader.open(new InputStreamReader(getClass.getResourceAsStream("/references.csv")))
     val uri = reader.toStream().take(1)(0).zipWithIndex.filter { x => "uri".equals(x._1)}.head

     reader.foreach { row =>
       lookup.filter { someTrait => someTrait._1.equals(row(0))}
         .foreach { selectedTrait => printer(List(row(uri._2), selectedTrait._2).mkString(",")) }
     }
   }

   def extractGloBIRefId(eolAssociationURI: String): String = {
     val assocId: Option[String] = """globi:assoc:\d+""".r.findFirstIn(eolAssociationURI)
     val id = """\d+""".r.findFirstIn(assocId.getOrElse("")).getOrElse("")
     val globiRef: String = "globi:ref:" + id
     globiRef
   }


}
