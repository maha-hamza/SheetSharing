package sheet_sharing.shares

fun validateSelection(selection: String): Boolean {
    return """
                ^((?>(?>'[\w\s]+')|(?>[\w\s]+))(?>![A-Z]{1}[0-9]{1})?(?>:[A-Z]{1}[0-9])?)
            """
        .trimIndent()
        .toRegex()
        .matches(selection)
}

/**
 * I would prefer to send email to verify that the given address is correct rather than validating via regex
 * regex provides many cases and standards are changed by provider
 * Here i would validate using regex as a prove of concept that we need email validation here , but ideally i would send email and wait result
 */
fun validateEmail(email: String): Boolean {
    return """
                ^((?!.*?\.\.)[A-Za-z0-9\.\!\#\$\%\&\'*\+\-\/\=\?\^_`\{\|\}\~]+@[A-Za-z0-9]+[A-Za-z0-9\-\.]+\.[A-Za-z0-9\-\.]+[A-Za-z0-9]+)$
            """
        .trimIndent()
        .toRegex()
        .matches(email)
}