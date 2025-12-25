package com.github.farzadazimipour.kstate.core

import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class LoggerTest {

    @Test
    fun `DefaultLogger logs messages to console`() {
        val originalOut = System.out
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))
        
        try {
            DefaultLogger.log("Test message")
            val output = outputStream.toString()
            assert(output.contains("[KState] Test message"))
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `DefaultLogger logs errors to console`() {
        val originalOut = System.out
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))
        
        try {
            val exception = RuntimeException("Test exception")
            DefaultLogger.error("Test error", exception)
            val output = outputStream.toString()
            assert(output.contains("[KState ERROR] Test error"))
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `NoOpLogger does nothing`() {
        // This test just ensures NoOpLogger doesn't throw exceptions
        NoOpLogger.log("Test message")
        NoOpLogger.error("Test error", RuntimeException("Test exception"))
        // If we reach here without exceptions, the test passes
    }
}