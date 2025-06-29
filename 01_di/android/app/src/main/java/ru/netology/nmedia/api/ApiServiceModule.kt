package ru.netology.nmedia.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.di.DependencyContainer
import javax.inject.Singleton
@InstallIn(SingletonComponent::class)
@Module
object ApiServiceModule {
    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        // Воспользуйтесь уже настроенным retrofit из DependencyContainer или создайте новый
        // Предположим, что ты используешь DependencyContainer для получения retrofit
        // Тогда:
        return DependencyContainer.instance!!.retrofit.create(ApiService::class.java)
    }
}

//
//@InstallIn(SingletonComponent::class)
//@Module
//object ApiServiceModule {
//    @Provides
//    @Singleton
//    fun provideApiService(auth: AppAuth): ApiService {
//        return retrofit(okhttp(loggingInterceptor(), authInterceptor(auth)))
//            .create(ApiService::class.java)
//    }
//}