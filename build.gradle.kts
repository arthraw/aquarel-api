val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val exposedVersion: String by project
val mysqlVersion: String by project
val dotenvVersion: String by project
val bcryptVersion: String by project
val flywayCoreVersion: String by project
val flywayMysqlVersion: String by project

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
    implementation("io.github.cdimascio:dotenv-kotlin:$dotenvVersion")

    // Ktor
    implementation("io.ktor:ktor-server-core:2.3.0")
    implementation("io.ktor:ktor-server-netty:2.0.0")
    implementation("io.ktor:ktor-server-auth-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")

    //Gson
    implementation ("com.google.code.gson:gson:2.10.1")

    // Bcrypt
    implementation("org.mindrot:jbcrypt:$bcryptVersion")

    // Exposed ORM
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")

    // Flyway Migrations
    implementation("org.flywaydb:flyway-mysql:$flywayMysqlVersion")
    implementation("org.flywaydb:flyway-core:$flywayCoreVersion")

    // Database
    implementation("mysql:mysql-connector-java:$mysqlVersion")

    // Netty server
    implementation("io.ktor:ktor-server-netty-jvm")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    // Tests
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation("io.ktor:ktor-client-content-negotiation:2.1.1")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}
