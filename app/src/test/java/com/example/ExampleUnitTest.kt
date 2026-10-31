package com.example

import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun jargonGlossary_containsEssentialTrack2Terms() {
    val items = com.example.ui.components.JargonGlossary.items
    assertTrue(items.containsKey("IDV"))
    assertTrue(items.containsKey("Zero Depreciation"))
    assertTrue(items.containsKey("NCB"))
    assertTrue(items.containsKey("Flat vs Reducing APR"))
    assertTrue(items.containsKey("Daily Auto-Settlement Debit"))

    val idv = items["IDV"]
    assertNotNull(idv)
    assertTrue(idv!!.plainEnglish.isNotEmpty())
    assertTrue(idv.finSenseRecommendation.isNotEmpty())

    val apr = items["Flat vs Reducing APR"]
    assertNotNull(apr)
    assertTrue(apr!!.plainEnglish.contains("effective APR"))
  }
}
