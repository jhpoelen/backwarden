package org.eol.globi.traits

import java.io.{File, Serializable}

import com.github.tototoshi.csv.CSVReader
import dispatch._, Defaults._
import org.scalatest._
import play.api.libs.json._

import scala.io.Source

class TraitExtractor$Test extends FlatSpec with Matchers {

  "a request for eol taxa" should "return a list of page ids" in {
    TraitExtractor.likelyPageIdsForINaturalistTaxa(20000).head shouldBe a[String]
  }

//  "requested traits from eol" should "have some trait id" in {
//    val eolPageId: String = TraitExtractor.pageIdsForTaxa(1).head
//    val response: String = TraitExtractor.traitsForPageId(eolPageId)
//    (Json.parse(response) \\ "@id").map { _.as[String]} should contain ("http://eol.org/pages/" + eolPageId)
//  }

  "on parse and filtering traits" should "only return the globi-based traits" in {
    val traitJson = Source.fromInputStream(getClass.getResourceAsStream("/traits.json")).mkString
    val globiAssocToEOLTraitId: List[(String, String)] = TraitExtractor.extractGloBITraits(traitJson)
    globiAssocToEOLTraitId should contain ("http://eol.org/resources/713/associations/globi:assoc:889328-eol:40021-ate-eol:10535","http://eol.org/pages/40021/data#data_point_17228895")
  }

  "associate association id" should "be replaced with study uri" in {
    val eolAssociationURI: String = "http://eol.org/resources/713/associations/globi:assoc:2102468-eol:70995-interacts_with-eol:329131"
    val globiRef: String = TraitExtractor.extractGloBIRefId(eolAssociationURI)
    globiRef should be("globi:ref:2102468")
  }

  "read a csv" should "read that csv" in {
    val lookup = Map("globi:ref:889328" -> "http://eol.org/pages/40021/data#data_point_17228895")
    //TraitExtractor.printProviderAndConsumer(lookup, println)
  }



}