package config

import com.uchuhimo.konf.Config

import sheet_sharing.shares.SheetShareController
import sheet_sharing.shares.SheetShareService
import org.jetbrains.exposed.sql.Database
import org.koin.Logger.slf4jLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import javax.sql.DataSource

fun initKoin(config: Config) {
    startKoin {
        modules(modules(config))
        slf4jLogger()
    }
}

fun modules(config: Config, overrides: List<Module> = emptyList()): List<Module> {
    return listOf(
        module {
            single { config }

            single { connectionPool(config) }
            single { Database.connect(get<DataSource>()) }

            single { SheetShareController() }
            single { SheetShareService() }

        }
    ) + overrides
}

inline fun <reified T : Any> inject(): T {
    return GlobalContext.get().koin.get()
}

inline fun <reified T : Any> lazyInject(): Lazy<T> {
    return lazy { inject<T>() }
}