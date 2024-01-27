val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val mysql_version: String by project
val dotenv_version: String by project
val bcrypt_version: String by project
val flyway_core_version: String by project
val flyway_mysql_version: String by project

plugins {
    kotlin("jvm") version "1.9.22"
    id("io.ktor.plugin") version "2.3.7"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
    id ("org.flywaydb.flyway") version "8.5.4"
}
flyway {
    url = System.getenv("jdbc:mysql://localhost:3306/aquarel")
    user = System.getenv("java/DATABASE_USER")
    password = System.getenv("java/DATABASE_PASSWORD")
    baselineOnMigrate = true
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    // DotEnv
    implementation(kotlin("stdlib"))
    implementation("io.github.cdimascio:dotenv-kotlin:$dotenv_version")

    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")

    // Bcrypt
    implementation("org.mindrot:jbcrypt:$bcrypt_version")

    // Exposed ORM
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")

    // Flyway Migrations
    implementation("org.flywaydb:flyway-mysql:$flyway_mysql_version")
    implementation("org.flywaydb:flyway-core:$flyway_core_version")

    // Database
    implementation("mysql:mysql-connector-java:$mysql_version")

    // Netty server
    implementation("io.ktor:ktor-server-netty-jvm")

    implementation("ch.qos.logback:logback-classic:$logback_version")
    // Tests
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
