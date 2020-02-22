package sheet_sharing.shares

fun validateSelection(selection: String): Boolean {
    return """
                ^((?>(?>'[\w\s]+')|(?>[\w\s]+))(?>![A-Z]{1}[0-9]{1})?(?>:[A-Z]{1}[0-9])?)
            """
        .trimIndent()
        .toRegex()
        .matches(selection)
}