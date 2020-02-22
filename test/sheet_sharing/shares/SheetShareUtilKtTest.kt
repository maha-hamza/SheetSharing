package sheet_sharing.shares

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SheetShareUtilKtTest {

    @Test
    fun `Should Validate Selection Correct Formats Successfully`() {
        assertTrue(validateSelection("SheetNameWithoutSpacesSingleCell!A1"))
        assertTrue(validateSelection("'Sheet Name With Spaces Single Cell'!B4"))
        assertTrue(validateSelection("OnlyTheSheetNameWithoutQuotes"))
        assertTrue(validateSelection("'SheetNameWithQuotesNoSpaces'"))
        assertTrue(validateSelection("'Sheet Name With Quotes And Spaces'"))
        assertTrue(validateSelection("'Long Sheet Name With Spaces and Range'!B3:B5"))
        assertTrue(validateSelection("SheetNameWithoutSpacesWithRanges!B2:B5"))
    }

    @Test
    fun `Should Detect Wrong Selection Formats (Unbalanced quotes)`() {
        assertFalse(validateSelection("Sheet Name With Spaces Single Cell'!B4"))
        assertFalse(validateSelection("'Sheet Name With Spaces Single Cell!B4"))
    }

}