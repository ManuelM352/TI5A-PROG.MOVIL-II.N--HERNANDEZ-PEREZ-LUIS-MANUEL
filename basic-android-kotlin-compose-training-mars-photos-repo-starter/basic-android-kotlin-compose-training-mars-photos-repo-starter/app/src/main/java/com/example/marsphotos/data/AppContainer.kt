package com.example.marsphotos.data

interface AppContainer {
    val marsPhotosRepository : MarsPhotosRepository
}

class DefaultAppContainer: AppContainer{
    private const val BASE_URL =
    "https://android-kotlin-fun-mars-server.appspot.com"
}