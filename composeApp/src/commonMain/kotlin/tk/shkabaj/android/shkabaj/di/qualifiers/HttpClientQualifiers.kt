package tk.shkabaj.android.shkabaj.di.qualifiers

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue

// INFO: provides host base url
object AppHttpClient : Qualifier {
    override val value: QualifierValue
        get() = "AppHttpClient"
}

object CommonHttpClient : Qualifier {
    override val value: QualifierValue
        get() = "CommonHttpClient"
}