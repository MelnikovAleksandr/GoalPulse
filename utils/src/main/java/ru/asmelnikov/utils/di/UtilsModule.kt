package ru.asmelnikov.utils.di

import org.koin.dsl.module
import ru.asmelnikov.utils.StringResourceProvider
import ru.asmelnikov.utils.color_gen.ColorGenerator

val utilsModule = module {

    single<StringResourceProvider> { StringResourceProvider.StringResourceProviderImpl(context = get()) }

    single<ColorGenerator> { ColorGenerator.ColorGeneratorImpl(context = get()) }
}
