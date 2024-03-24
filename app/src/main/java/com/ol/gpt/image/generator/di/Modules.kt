package com.ol.gpt.image.generator.di

import com.ol.gpt.image.generator.coroutines.AppCoroutineScope
import kotlinx.coroutines.CoroutineScope
import org.koin.dsl.module

val coroutineScopeModule = module {
    factory<CoroutineScope> { AppCoroutineScope() }
}