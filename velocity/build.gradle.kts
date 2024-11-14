import java.security.MessageDigest

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

fun download(url: String): ConfigurableFileCollection {
    val fileName = url.substringAfterLast("/")
    val hashedUrl = MessageDigest.getInstance("SHA-512").digest(url.toByteArray()).joinToString("") { "%02x".format(it) }
    val cacheDir = File(project.layout.buildDirectory.asFile.get(), "downloadCache")
    val file = File(cacheDir, "$hashedUrl/$fileName")
    if (!file.exists()) {
        file.parentFile.mkdirs()
        file.writeBytes(uri(url).toURL().readBytes())
    }
    return files(file)
}

dependencies {
    implementation(project(":api-impl"))
    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    compileOnly("net.william278:velocityscoreboardapi:1.0.5")
    compileOnly(download("https://github.com/NEZNAMY/TAB/releases/download/5.0.1/TAB.v5.0.1.jar"))
}

tasks {
    shadowJar {
        archiveBaseName.set("${parent!!.name}-${project.name}")
    }
}
