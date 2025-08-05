package ru.netology.nmedia.di

import android.content.Context
import androidx.room.Room
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.BuildConfig.BASE_URL
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.api.authInterceptor
import ru.netology.nmedia.api.loggingInterceptor
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl

class DependencyContainer(
    private val context: Context
) {
    companion object {
        @Volatile
        private var instance: DependencyContainer? = null

        fun initApp(context: Context) {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = DependencyContainer(context)
                    }
                }
            }
        }

        fun getInstance(): DependencyContainer {
            return instance ?: throw IllegalStateException("DependencyContainer is not initialized. Call initApp() first.")
        }
    }


    private val BASE_URL = "${BuildConfig.BASE_URL}/api/slow/"


    private val logging = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    val appAuth = AppAuth(context)
    val okHttp = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor())
        .addInterceptor(authInterceptor(appAuth))
        .build()

//    private val okhttp = OkHttpClient.Builder()
//        .addInterceptor(logging)
//        .addInterceptor { chain ->
//            appAuth.authStateFlow.value.token?.let { token ->
//                val newRequest = chain.request().newBuilder()
//                    .addHeader("Autorization", token)
//                    .build()
//                return@addInterceptor chain.proceed(newRequest)
//            }
//            chain.proceed(chain.request())
//        }
//        .build()
//
//    fun okhttp(vararg interceptors: Interceptor): OkHttpClient = OkHttpClient.Builder()
//        .apply {
//            interceptors.forEach {
//                this.addInterceptor(it)
//            }
//        }
//        .build()

    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttp)
        .build()

    private val appBd = Room.databaseBuilder(context, AppDb::class.java, "app.db")
        .fallbackToDestructiveMigration()
        .build()

    val apiService = retrofit.create<ApiService>()
    private val postDao = appBd.postDao()

    val repository: PostRepository = PostRepositoryImpl(
        postDao,
        apiService,
    )

}