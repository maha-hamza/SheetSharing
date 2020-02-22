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
