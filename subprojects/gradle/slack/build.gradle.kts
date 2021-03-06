plugins {
    id("kotlin")
    id("java-test-fixtures")
    `maven-publish`
    id("com.jfrog.bintray")
    id("nebula.integtest")
}

val kotlinVersion: String by project
val jslackVersion: String by project
val funktionaleVersion: String by project
val kotlinCoroutinesVersion: String by project
val okhttpVersion: String by project

dependencies {
    implementation(project(":subprojects:gradle:utils"))
    implementation(project(":subprojects:gradle:logging"))
    implementation(project(":subprojects:common:time"))
    implementation("org.funktionale:funktionale-try:$funktionaleVersion")
    implementation("com.github.seratch:jslack-api-client:$jslackVersion") {
        exclude(group = "com.squareup.okhttp3")
    }
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")

    testImplementation(project(":subprojects:gradle:test-project"))
    testImplementation(testFixtures(project(":subprojects:common:time")))

    testFixturesImplementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    testFixturesImplementation("org.funktionale:funktionale-try:$funktionaleVersion")
}
