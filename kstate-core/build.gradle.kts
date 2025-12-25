plugins {
    alias(libs.plugins.kstate.jvm.library)
    `maven-publish`
    signing
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            
            groupId = "com.github.farzadazimipour.kstate"
            artifactId = "kstate-core"
            version = "1.0.0"
            
            pom {
                name.set("KState Core")
                description.set("Lightweight Kotlin-first state machine library")
                url.set("https://github.com/farzadazimipour/kstate")
                
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                
                developers {
                    developer {
                        id.set("farzadazimipour")
                        name.set("Farzad Azimipour")
                    }
                }
                
                scm {
                    connection.set("scm:git:git://github.com/farzadazimipour/kstate.git")
                    developerConnection.set("scm:git:ssh://github.com/farzadazimipour/kstate.git")
                    url.set("https://github.com/farzadazimipour/kstate")
                }
            }
        }
    }
}
