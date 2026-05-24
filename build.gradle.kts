plugins {
    java
    kotlin("jvm") version "2.3.21"
    application
}

group = "com.chess.engine"
version = "6.2.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("Game.GameEngine.Game")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "Game.GameEngine.Game"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
