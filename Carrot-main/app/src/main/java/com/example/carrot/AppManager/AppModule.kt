package com.example.carrot.AppManager

import android.content.Context
import androidx.room.Room
import com.example.carrot.Repositories.CartRepository
import com.example.carrot.Repositories.CartRepositoryImpl
import com.example.carrot.Repositories.ProductRepositoryImpl
import com.example.carrot.Repositories.ProductRepository
import com.example.carrot.Repositories.UserRepository
import com.example.carrot.Repositories.UserRepositoryIml
import com.example.carrot.ViewModels.ProductCartService
import com.example.carrot.db.AppDatabase
import com.example.carrot.db.CartDao
import com.example.carrot.db.ProductDao
import com.example.carrot.db.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

   @Provides
   @Singleton
   fun provideUserRepository(userDao: UserDao): UserRepository {
       return UserRepositoryIml(userDao)
   }

    @Provides
    @Singleton
    fun provideProductRepository(productDao: ProductDao): ProductRepository {
        return ProductRepositoryImpl(productDao )
    }

    @Provides
    @Singleton
    fun provideCartRepository(cartDao: CartDao, productRepository: ProductRepository): CartRepository {
        return CartRepositoryImpl(
            cartDao,
            productRepository = productRepository
        )
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideProductDao(appDatabase: AppDatabase): ProductDao {
        return appDatabase.productDao()
    }

    @Provides
    @Singleton
    fun provideCartDao(appDatabase: AppDatabase): CartDao {
        return appDatabase.cartDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DBNAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideProductCartService(cartRepository: CartRepository, productRepository: ProductRepository): ProductCartService {
        return ProductCartService(cartRepository = cartRepository, productRepository = productRepository)
    }
}