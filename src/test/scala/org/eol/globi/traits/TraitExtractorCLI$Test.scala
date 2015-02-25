package org.eol.globi.traits

import org.scalatest._
import play.api.libs.json._

import scala.io.Source

class TraitExtractorCLI$Test extends FlatSpec with Matchers {

  "a request for eol taxa" should "return a list of page ids" in {
    TraitExtractorCLI.main(List("7666").toArray)
  }


}