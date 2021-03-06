plugins {
    id("kotlin")
    id("java-gradle-plugin")
    id("java-test-fixtures")
    `maven-publish`
    id("com.jfrog.bintray")
}

val funktionaleVersion: String by project
val androidGradlePluginVersion: String by project
val mockitoKotlinVersion: String by project
val mockitoJunit5Version: String by project

dependencies {
    implementation(project(":subprojects:gradle:sentry-config"))
    implementation(project(":subprojects:gradle:graphite-config"))
    implementation(project(":subprojects:gradle:statsd-config"))
    implementation(project(":subprojects:gradle:utils"))
    implementation(project(":subprojects:gradle:logging"))
    implementation(project(":subprojects:gradle:kotlin-dsl-support"))
    implementation(project(":subprojects:gradle:impact-shared"))
    implementation(project(":subprojects:gradle:teamcity"))
    implementation(project(":subprojects:gradle:trace-event"))
    implementation("org.funktionale:funktionale-try:$funktionaleVersion")
    implementation("com.android.tools.build:gradle:$androidGradlePluginVersion")

    testImplementation("com.nhaarman:mockito-kotlin:$mockitoKotlinVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:$mockitoJunit5Version")
    testImplementation(project(":subprojects:gradle:git"))
    testImplementation(project(":subprojects:gradle:test-project"))
    testImplementation(testFixtures(project(":subprojects:common:graphite")))
    testImplementation(testFixtures(project(":subprojects:common:statsd")))
}

gradlePlugin {
    plugins {
        create("buildMetrics") {
            id = "com.avito.android.build-metrics"
            implementationClass = "com.avito.android.plugin.build_metrics.BuildMetricsPlugin"
            displayName = "Build metrics"
        }
    }
}
