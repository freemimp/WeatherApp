package uk.co.freemimp.commontest.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

@ExperimentalCoroutinesApi
class TestCoroutineExtension : BeforeEachCallback, AfterEachCallback,
    AfterAllCallback {

    private val dispatcher = UnconfinedTestDispatcher()

    override fun afterEach(context: ExtensionContext?) {
        Dispatchers.resetMain()

    }

    override fun beforeEach(context: ExtensionContext?) {
        Dispatchers.setMain(dispatcher)

    }

    override fun afterAll(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }

}
