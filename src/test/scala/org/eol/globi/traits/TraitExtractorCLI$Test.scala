package org.eol.globi.traits

import java.io.StringWriter

import org.scalatest._
import play.api.libs.json._

import scala.io.Source

class TraitExtractorCLI$Test extends FlatSpec with Matchers {

  "a request for eol taxa" should "return a list of page ids" in {
    val rows: Array[String] = generateConsumerProvidersPairs(List("7666"))
    rows should contain("http://dx.doi.org/10.1007/s00300-005-0048-7,http://eol.org/pages/7666/data#data_point_16870802")
  }

  "another request for eol taxa" should "return a list of page ids" in {
    val rows: Array[String] = generateConsumerProvidersPairs(List("472631"))
    rows should contain("http://inaturalist.org/observations/1158453,http://eol.org/pages/472631/data#data_point_18721221")
    rows should contain("http://inaturalist.org/observations/1158453,http://eol.org/pages/472631/data#data_point_18721222")
    rows should contain("http://inaturalist.org/observations/1158453,http://eol.org/pages/472631/data#data_point_17585196")
  }


  def generateConsumerProvidersPairs(pageids: List[String]): Array[String] = {
    val stringWriter = new StringWriter
    val printer = (x: String) => stringWriter.write(x + "\n")
    TraitExtractorCLI.printProviderConsumerPairs(pageids, printer)
    val rows: Array[String] = stringWriter.toString.split("\n")
    rows
  }
}