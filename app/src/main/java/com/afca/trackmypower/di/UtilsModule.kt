package com.afca.trackmypower.di

import android.content.Context
import com.afca.trackmypower.helpers.utils.StringProvider
import com.afca.trackmypower.helpers.utils.StringProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {
    @Provides
    fun providedStringProvider(@ApplicationContext context: Context): StringProvider = StringProviderImpl(context)
}