package com.avito.instrumentation.report

import com.avito.instrumentation.TestRunResult
import com.avito.report.FakeReportViewer
import com.avito.report.model.ReportCoordinates
import com.avito.report.model.SimpleRunTest
import com.avito.report.model.Status
import com.avito.report.model.createStubInstance
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path

internal class JUnitReportWriterTest {

    lateinit var file: File

    private val reportViewerUrl = "https://report/"

    @BeforeEach
    fun setup(@TempDir temp: Path) {
        file = File(temp.toFile(), "sample_report.xml")
    }

    @Test
    fun `junit report - contains caseId data`() {
        mockData(
            TestRunResult(
                reportedTests = listOf(SimpleRunTest.createStubInstance(testCaseId = 8888)),
                failed = HasFailedTestDeterminer.Result.NoFailed,
                notReported = HasNotReportedTestsDeterminer.Result.AllTestsReported
            )
        )
        val rawFile = file.readText()
        assertThat(rawFile).contains("caseId=\"8888\"")
    }

    @Test
    fun `junit report - contains skipped case`() {
        mockData(
            TestRunResult(
                reportedTests = listOf(SimpleRunTest.createStubInstance(status = Status.Skipped("reason"))),
                failed = HasFailedTestDeterminer.Result.NoFailed,
                notReported = HasNotReportedTestsDeterminer.Result.AllTestsReported
            )
        )
        val rawFile = file.readText()
        assertThat(rawFile).contains("<skipped/>")
    }

    @Test
    fun `junit report - contains failure data with report viewer link`() {
        val failed = SimpleRunTest.createStubInstance(
            id = "id",
            status = Status.Failure(verdict = "Something went wrong", errorHash = "111")
        )

        mockData(
            TestRunResult(
                reportedTests = listOf(
                    failed
                ),
                failed = HasFailedTestDeterminer.Result.Failed(
                    failed = listOf(failed),
                    suppressed = HasFailedTestDeterminer.Result.Failed.Suppressed(
                        tests = emptyList(),
                        reason = HasFailedTestDeterminer.Result.Failed.Suppressed.Reason.SuppressedByGroups(
                            emptyList()
                        )
                    )
                ),
                notReported = HasNotReportedTestsDeterminer.Result.AllTestsReported
            )
        )
        val rawFile = file.readText()
        assertThat(rawFile).contains("<failure>\nSomething went wrong\nReport Viewer: $reportViewerUrl\n</failure>")
    }

    @Test
    fun `junit report - contains test class and method names`() {
        mockData(
            TestRunResult(
                reportedTests = listOf(
                    SimpleRunTest.createStubInstance(
                        name = "com.avito.android.deep_linking.DeepLinkingActivityIntentFilterTest.resolve_advert_legacyFormat"
                    )
                ),
                failed = HasFailedTestDeterminer.Result.NoFailed,
                notReported = HasNotReportedTestsDeterminer.Result.AllTestsReported
            )
        )
        val rawFile = file.readText()
        assertThat(rawFile).contains("classname=\"com.avito.android.deep_linking.DeepLinkingActivityIntentFilterTest\"")
        assertThat(rawFile).contains("name=\"resolve_advert_legacyFormat\"")
    }

    private fun mockData(testRunResult: TestRunResult) {
        val runIdentifier = ReportCoordinates(
            planSlug = "AvitoAndroid",
            jobSlug = "FunctionalTests",
            runId = "49.0.275.32855"
        )
        JUnitReportWriter(FakeReportViewer(reportViewerUrl)).write(
            runIdentifier, testRunResult, file
        )
    }
}
