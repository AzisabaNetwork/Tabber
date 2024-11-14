java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

dependencies {
    implementation(project(":api-impl"))
    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    compileOnly("net.william278:velocityscoreboardapi:1.0.5")
    compileOnly(files("libs/TAB.v5.0.1.jar"))
}

tasks {
    shadowJar {
        archiveBaseName.set("${parent!!.name}-${project.name}")
    }
}
