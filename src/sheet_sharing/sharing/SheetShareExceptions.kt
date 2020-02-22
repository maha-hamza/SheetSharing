package sheet_sharing.sharing

import java.lang.RuntimeException

class BodyDeserializationException(msg: String?) : RuntimeException(msg)

class InvalidSelectionFormatException(msg: String?) : RuntimeException(msg)
