plugins {
    id("kotlin")
    id("java-gradle-plugin")
    `maven-publish`
    id("com.jfrog.bintray")
}

val kotlinVersion: String by project
val androidGradlePluginVersion: String by project
val mockitoKotlinVersion: String by project

dependencies {
    implementation(project(":subprojects:gradle:cd"))
    implementation(project(":subprojects:gradle:impact-shared"))
    implementation(project(":subprojects:gradle:files"))
    implementation(project(":subprojects:gradle:utils"))
    implementation(project(":subprojects:gradle:android"))
    implementation(project(":subprojects:gradle:kotlin-dsl-support"))

    implementation("com.android.tools.build:gradle:$androidGradlePluginVersion")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    implementation("org.ow2.asm:asm:7.1")

    testImplementation(project(":subprojects:gradle:test-project"))
    testImplementation("com.nhaarman:mockito-kotlin:$mockitoKotlinVersion")
}

gradlePlugin {
    plugins {
        create("dependenciesLintPlugin") {
            id = "com.avito.android.dependencies-lint"
            implementationClass = "com.avito.android.lint.DependenciesLintPlugin"
            displayName = "Dependencies lint"
        }
    }
}
