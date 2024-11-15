plugins {
    id("net.kyori.blossom") version "2.1.0"
    id("com.google.protobuf") version "0.9.4"
}

dependencies {
    api("dev.cel:cel:0.8.0")
    api("com.google.protobuf:protobuf-java:4.28.3")
    api("org.antlr:antlr4:4.13.2")
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

        proto {
            srcDir("src/main/proto")
        }
    }
}
