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
    val formattedSheet = sheet
        .replace("""!.*$""".toRegex(), "") //remove ending
        .replace("""^[\"']+|[\"']+$""".toRegex(), "") //remove quotes
        .replace(" ", "")
        .toUpperCase()

    return when (SheetEnum.values().map { it.name }.contains(formattedSheet)) {
        true -> SheetEnum.valueOf(formattedSheet)
        false -> throw SelectionDoesntMatchActualSheetException("$formattedSheet Not a valid sheet")
    }
}

fun validateEquivalentSheetEnum(sheet: String): Boolean {
    val formattedSheet = sheet
        .replace("""!.*$""".toRegex(), "") //remove ending
        .replace("""^[\"']+|[\"']+$""".toRegex(), "") //remove quotes
        .replace(" ", "")
        .toUpperCase()

    return SheetEnum.values().map { it.name }.contains(formattedSheet)
}