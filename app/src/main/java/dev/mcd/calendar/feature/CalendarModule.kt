package dev.mcd.calendar.feature

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mcd.calendar.feature.calendar.domain.GetMonthData

@Module
@InstallIn(SingletonComponent::class)
class CalendarModule {

    @Provides
    fun getMonthData() = GetMonthData()
}
