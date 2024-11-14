plugins {
    id("net.kyori.blossom") version "2.1.0"
}

dependencies {
    compileOnlyApi("org.jetbrains:annotations:26.0.1")
    compileOnlyApi("net.kyori:adventure-api:4.17.0")
}

sourceSets {
    main {
        blossom {
            javaSources {
                property("version", project.version.toString())
            }
        }
    }
}
