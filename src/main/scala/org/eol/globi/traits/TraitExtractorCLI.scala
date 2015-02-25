package org.eol.globi.traits

import dispatch._, Defaults._
import org.eol.globi.traits.TraitExtractor

object TraitExtractorCLI extends App {

  def pageIds: List[String] = {
    if (args.length == 0) {
      TraitExtractor.likelyPageIdsForINaturalistTaxa(100).toList
    } else {
      args.toList
    }
  }

  println("provider,consumer")
  val traitMap = pageIds.foreach( pageId => {
    val traits = TraitExtractor.traitsForPageId(pageId)
    val traitMap = TraitExtractor.extractGloBITraits(traits)
      .foldLeft(Map[String, String]())((acc, someTrait) => acc ++ Map(TraitExtractor.extractGloBIRefId(someTrait._1) -> someTrait._2))
    TraitExtractor.printProviderAndConsumer(traitMap, println)
  }
  )
  Http.shutdown()
}
