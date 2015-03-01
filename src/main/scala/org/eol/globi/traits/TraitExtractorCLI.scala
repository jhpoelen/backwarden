package org.eol.globi.traits

import dispatch._, Defaults._
import org.eol.globi.traits.TraitExtractor.{printProviderAndConsumer, extractGloBIRefId, extractGloBITraits, traitsForPageId}

object TraitExtractorCLI extends App {

  def getEOLPageIds: List[String] = {
    if (args.length == 0) {
      TraitExtractor.likelyPageIdsForINaturalistTaxa(100).toList
    } else {
      args.toList
    }
  }

  def printProviderConsumerPairs(pageIds: List[String], printer: (String) => Unit) = {
    println("provider,consumer")
    pageIds.foreach(pageId => {
      try {
        val traits = traitsForPageId(pageId)
        val traitMap = extractGloBITraits(traits)
        val traitPairs = traitMap map { traitPair => (extractGloBIRefId(traitPair._1), traitPair._2)}
        printProviderAndConsumer(traitPairs, printer)
      } catch {
          case ex: Throwable => sys.error("failed to retrieve [" + pageId + "], because of [" + ex.getMessage + "]")
          case _: Throwable => sys.error("failed to retrieve [" + pageId + "])")
      }
    }
    )
  }

  try {
    printProviderConsumerPairs(getEOLPageIds, println)
  } finally {
    Http.shutdown()
  }

}
