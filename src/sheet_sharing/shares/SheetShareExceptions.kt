package sheet_sharing.shares

import java.lang.RuntimeException

class BodyDeserializationException(msg: String?) : RuntimeException(msg)

class InvalidSelectionFormatException(msg: String?) : RuntimeException(msg)

class RecipientNotFoundException(msg: String?) : RuntimeException(msg)

class InvalidEmailFormatException(msg: String?) : RuntimeException(msg)

class SelectionDoesntMatchActualSheetException(msg: String?) : RuntimeException(msg)
