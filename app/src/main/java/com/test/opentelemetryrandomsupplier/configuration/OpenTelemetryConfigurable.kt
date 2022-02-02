package com.test.opentelemetryrandomsupplier.configuration

import com.test.opentelemetryrandomsupplier.processor.TestSpanProcessor
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.OpenTelemetrySdkBuilder
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.samplers.Sampler

class OpenTelemetryConfigurable {

    private val builder: Lazy<OpenTelemetrySdkBuilder> = lazy { OpenTelemetrySdk.builder() }

    constructor()

    fun configure() {
        builder.value.setTracerProvider(buildProvider())
            .setPropagators(buildPropagators())
            .buildAndRegisterGlobal()
    }

    private fun buildProvider(): SdkTracerProvider {
        return SdkTracerProvider.builder()
            .setSampler(Sampler.alwaysOn())
            .addSpanProcessor(TestSpanProcessor())
            .build()
    }

    private fun buildPropagators(): ContextPropagators {
        return ContextPropagators.create(
            W3CTraceContextPropagator.getInstance()
        )
    }
}
