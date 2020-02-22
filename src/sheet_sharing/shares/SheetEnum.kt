package sheet_sharing.shares

import com.fasterxml.jackson.annotation.JsonProperty

enum class SheetEnum {
    @JsonProperty("HRReport")
    HRRREPORT,
    @JsonProperty("Actuals")
    ACTUALS,
    @JsonProperty("Assumptions")
    ASSUMPTIONS,
    @JsonProperty("Dashboard")
    DASHBOARD
}

fun getEquivalentSheetEnum(sheet: String): SheetEnum {
    val formatedSheet = sheet
        .replace("""!.*$""".toRegex(), "") //remove ending
        .replace("""^[\"']+|[\"']+$""".toRegex(), "") //remove quotes
        .replace(" ", "")
        .toUpperCase()

    return when (SheetEnum.values().map { it.name }.contains(formatedSheet)) {
        true -> SheetEnum.valueOf(formatedSheet)
        false -> throw IllegalArgumentException("$formatedSheet Not a valid sheet")
    }
}