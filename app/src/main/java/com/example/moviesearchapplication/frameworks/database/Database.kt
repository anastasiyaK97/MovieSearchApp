package com.example.moviesearchapplication.frameworks.database

/*
object Database {
    private var INSTANCE: RoomDB? = null

    fun getInstance(context: Context): RoomDB{
        val tempInstance = INSTANCE
        if (tempInstance != null){
            return tempInstance
        }

        synchronized(this){
            val instance = Room.databaseBuilder(
                context.applicationContext,
                RoomDB::class.java,
                "film_database.db"
            )
               // .addCallback()
                .fallbackToDestructiveMigration()
                .build()
            INSTANCE = instance
            return instance
        }
    }
}*/
