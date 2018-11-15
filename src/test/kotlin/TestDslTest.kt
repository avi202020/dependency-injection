import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.Test
import java.time.LocalTime

class TestDslTest {

    @Test
    fun testThatTheRightDecisionIsMade() {
        testConfig {
            time = "09:34"
        } whenAvailabilityInformation {
            fromAddress = "Keysers Gate 13, 0186 Oslo, Norway"
            toAddress = "Østbyfaret 18, 0678 Oslo, Norway"
        } assertValidDeliveryTimes {
            Pair(LocalTime.of(10, 0), LocalTime.of(13, 0))
        }
    }

}

fun testConfig(setupFunction: TestSetup.() -> Unit): TestSetup {
    return TestSetup().apply(setupFunction)
}


class TestSetup {
    lateinit var time: String

    infix fun whenAvailabilityInformation(function: TestRequestInfo.() -> Unit): TestRequestInfo {
        return TestRequestInfo(this).apply(function)
    }

}

class TestRequestInfo(private val testSetup: TestSetup) {
    lateinit var fromAddress: String
    lateinit var toAddress: String

    infix fun assertValidDeliveryTimes(function: () -> Pair<LocalTime, LocalTime>) {
        MatcherAssert.assertThat(businessFunction(testSetup.time, fromAddress, toAddress), IsEqual(function()))
    }

}


/**
 * This should really be the production code that you're running
 */
fun businessFunction(time: String, fromAddress: String, toAddress: String): Pair<LocalTime, LocalTime> {
    return LocalTime.of(10, 0) to LocalTime.of(13, 0)
}
