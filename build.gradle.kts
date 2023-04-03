val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val h2_version: String by project
plugins {
    kotlin("jvm") version "1.8.0"
    id("io.ktor.plugin") version "2.2.3"
    kotlin("plugin.serialization") version "1.8.0"
}
group = "com.example"
version = "0.0.1"
application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    google()
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
    }
    maven {
        url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
    }

}

dependencies {
    implementation("com.microsoft.sqlserver:mssql-jdbc:12.2.0.jre8")
    implementation("io.ktor:ktor-server-core-jvm:2.2.4")
    implementation("joda-time:joda-time:2.12.2")
    implementation("com.h2database:h2:$h2_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.2.4")
    implementation("io.ktor:ktor-server-websockets-jvm:2.2.4")
    implementation("io.ktor:ktor-client-content-negotiation-jvm:2.2.3")
    implementation("com.beust:klaxon:5.5")
    implementation("org.apache.commons:commons-email:1.5")
    implementation ("io.ktor:ktor-server-core:$ktor_version")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.10")
    implementation("org.ktorm:ktorm-core:3.6.0")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm:2.2.4")
    implementation("io.ktor:ktor-server-websockets:2.2.4")
    implementation("io.ktor:ktor-server-freemarker:2.2.4")
    implementation("org.freemarker:freemarker:2.3.32")
    implementation("io.ktor:ktor-freemarker:1.6.8")
    implementation("io.ktor:ktor-server-call-logging-jvm:2.2.4")
    implementation("io.ktor:ktor-server-netty-jvm:2.2.4")
    implementation("io.ktor:ktor-client-cio-jvm:2.2.4")
    implementation("io.ktor:ktor-client-core-jvm:2.2.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.4")
    implementation("io.ktor:ktor-client-serialization-jvm:2.2.4")
    implementation("io.ktor:ktor-client-logging-jvm:2.2.4")
    implementation("io.ktor:ktor-client-websockets-jvm:2.2.4")
    implementation("io.ktor:ktor-client-auth:2.2.4")
    implementation("io.ktor:ktor-server-host-common-jvm:2.2.4")
    implementation("io.ktor:ktor-server-status-pages-jvm:2.2.4")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("joda-time:joda-time:2.9.2")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:2.2.4")
    implementation("io.ktor:ktor-server-webjars:$ktor_version")


}
tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}