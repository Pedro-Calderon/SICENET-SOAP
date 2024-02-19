import android.content.Context
import android.util.Log
import com.example.marsphotos.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private val httpConnectTimeoutSeconds = 10
private val httpWriteTimeoutSeconds = 10
private val httpReadTimeoutSeconds = 10

val userAgent = "Your User Agent"

fun newHttpClient(context: Context): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor { Log.d("OkHttp", it) }

    loggingInterceptor.level = if (BuildConfig.DEBUG)
        HttpLoggingInterceptor.Level.BODY // debug log level
    else
        HttpLoggingInterceptor.Level.NONE  // release log level

    return OkHttpClient.Builder()
        .followRedirects(true)
        .followSslRedirects(true)
        .retryOnConnectionFailure(true)
        .setCookieStore(context)
        .addInterceptor(loggingInterceptor)
        .addNetworkInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", userAgent)
                .build()
            chain.proceed(request)
        }
        .cache(null)
        .connectTimeout(httpConnectTimeoutSeconds.toLong(), TimeUnit.SECONDS)
        .writeTimeout(httpWriteTimeoutSeconds.toLong(), TimeUnit.SECONDS)
        .readTimeout(httpReadTimeoutSeconds.toLong(), TimeUnit.SECONDS)
        .build()
}

fun <T> newRetrofit(context: Context, baseUrl: String, service: Class<T>): T {

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(newHttpClient(context))
        .build()

    return retrofit.create(service)
}