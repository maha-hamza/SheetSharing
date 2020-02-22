package sheet_sharing.shares

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException


class SheetEnumKtTest {

    @Test
    fun `Should Return Equivalent SheetEnum out of selection successfully`() {
        val selection = "'HRR REPORT'!B4"
        val result = getEquivalentSheetEnum(selection)
        Assertions.assertEquals(result, SheetEnum.HRRREPORT)
    }


    @Test
    fun `Should Detect Selection that is not existed in SheetEnum `() {
        val selection = "'HRR'!B4"
        assertThrows<IllegalArgumentException> { getEquivalentSheetEnum(selection) }
    }
}