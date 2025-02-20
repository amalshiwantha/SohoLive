pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven {
            url = uri("https://zendesk.jfrog.io/zendesk/repo")
        }
        maven {
            url = uri("https://muxinc.jfrog.io/artifactory/default-maven-release-local")
        }
    }
}


rootProject.name = "SohoLive"
include(":app")
 