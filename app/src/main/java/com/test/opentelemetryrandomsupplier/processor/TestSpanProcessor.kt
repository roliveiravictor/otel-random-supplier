package com.test.opentelemetryrandomsupplier.processor

import android.util.Log
import io.opentelemetry.context.Context
import io.opentelemetry.exporter.logging.LoggingSpanExporter
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.common.CompletableResultCode
import io.opentelemetry.sdk.trace.ReadWriteSpan
import io.opentelemetry.sdk.trace.ReadableSpan
import io.opentelemetry.sdk.trace.SpanProcessor
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor
import java.util.concurrent.TimeUnit

private const val BATCH_EXPORT_SIZE = 100
private const val BATCH_SCHEDULE_DELAY_IN_MINUTES = 1L

private val TAG = TestSpanProcessor::class.simpleName

internal class TestSpanProcessor : SpanProcessor {

    private val instance: SpanProcessor by lazy {
        BatchSpanProcessor.builder(LoggingSpanExporter())
            .setMaxExportBatchSize(BATCH_EXPORT_SIZE)
            .setScheduleDelay(BATCH_SCHEDULE_DELAY_IN_MINUTES, TimeUnit.MINUTES)
            .build()
    }

    override fun onStart(parentContext: Context, span: ReadWriteSpan) {
        // Do Nothing
    }

    override fun isStartRequired(): Boolean {
        return false
    }

    override fun onEnd(span: ReadableSpan) {
        instance.onEnd(span)
        Log.d(TAG, "span: $span")
    }

    override fun isEndRequired(): Boolean {
        return true
    }

    override fun shutdown(): CompletableResultCode {
        return instance.shutdown().also { result ->
            result.whenComplete {
                Log.d(TAG, "shutdown success: ${result.isSuccess}")
            }
        }
    }

    override fun forceFlush(): CompletableResultCode {
        return instance.forceFlush().also { result ->
            result.whenComplete {
                Log.d(TAG, "force flush success: ${result.isSuccess}")
            }
        }
    }
}
