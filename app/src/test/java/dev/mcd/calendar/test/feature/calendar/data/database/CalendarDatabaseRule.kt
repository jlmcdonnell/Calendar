package dev.mcd.calendar.test.feature.calendar.data.database

import androidx.room.Room
import dev.mcd.calendar.feature.calendar.data.dao.Events
import dev.mcd.calendar.feature.calendar.data.database.CalendarDatabase
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.robolectric.RuntimeEnvironment
import kotlin.reflect.KProperty

class CalendarDatabaseRule : TestRule {

    lateinit var database: CalendarDatabase

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                database = Room.inMemoryDatabaseBuilder(
                    context = RuntimeEnvironment.getApplication(),
                    klass = CalendarDatabase::class.java,
                ).build()

                base.evaluate()
                database.close()
            }
        }
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Events {
        return database.events()
    }
}

fun calendarDatabaseRule() = CalendarDatabaseRule()
