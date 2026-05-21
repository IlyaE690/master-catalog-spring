buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:3.4.4")
        classpath("io.spring.gradle:dependency-management-plugin:1.1.7")
        classpath("org.openapitools:openapi-generator-gradle-plugin:7.10.0")
    }
}

plugins {
    id("java")
    jacoco
}

apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")
apply(plugin = "org.openapi.generator")

group = "kfu.itis"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val postgresqlVersion: String by project
val springSecurityVersion: String by project
val jwtVersion: String by project


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("javax.mail:javax.mail-api:1.6.2")
    implementation("org.springframework.security:spring-security-taglibs:${springSecurityVersion}")

    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.session:spring-session-data-redis")


    implementation("io.jsonwebtoken:jjwt-api:${jwtVersion}")
    implementation("io.jsonwebtoken:jjwt-impl:${jwtVersion}")
    implementation("io.jsonwebtoken:jjwt-jackson:${jwtVersion}")

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.flywaydb:flyway-core")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.25")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

}

val openApiSpec = "$projectDir/src/main/resources/api.yaml"
val openApiGeneratedDir: String = layout.buildDirectory.dir("generated").get().asFile.absolutePath

configure<org.openapitools.generator.gradle.plugin.extensions.OpenApiGeneratorGenerateExtension> {
    inputSpec.set(openApiSpec)
    outputDir.set(openApiGeneratedDir)
    generatorName.set("spring")
    modelPackage.set("kfu.itis.api.generated.dto")
    apiPackage.set("kfu.itis.api.generated.api")

    configOptions.set(
        mapOf(
            "useJakartaEe" to "true",
            "useSpringBoot3" to "true",
            "library" to "spring-boot",
            "interfaceOnly" to "true",
            "skipDefaultInterface" to "true",
            "useBeanValidation" to "true",
            "useTags" to "true",
            "dateLibrary" to "java8",
            "openApiNullable" to "false",
            "documentationProvider" to "none",
            "useResponseEntity" to "true"
        )
    )
    additionalProperties.set(
        mapOf(
            "generateApiTests" to "false",
            "generateModelTests" to "false",
            "generateApiDocumentation" to "false",
            "generateModelDocumentation" to "false"
        )
    )
}

sourceSets {
    getByName("main") {
        java {
            srcDir(layout.buildDirectory.dir("generated/src/main/java"))
        }
    }
}

tasks.named("compileJava") {
    dependsOn("openApiGenerate")
}

tasks.test {
    useJUnitPlatform()
}

jacoco {
    toolVersion = "0.8.12"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.85".toBigDecimal()
            }
        }
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}
