import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
}

group = "inc.evil"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.zalando:logbook-spring-boot-webflux-autoconfigure:3.0.0")
	implementation("org.zalando:logbook-netty:3.0.0")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:mockserver")
	testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
	testImplementation("org.mock-server:mockserver-client-java:5.15.0")
}

tasks.bootBuildImage {
	imageName.set(project.name)
	environment.set(mapOf("BP_JVM_VERSION" to "17.*"))

	docker {
		publishRegistry {
			username.set(project.findProperty("registryUsername").toString())
			password.set(project.findProperty("registryToken").toString())
			url.set(project.findProperty("registryUrl").toString())
		}
	}
}


tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	jvmArgs(listOf(
		"-DHOST=http://localhost",
		"-DPORT=8083",
		"-DCONNECTOR_NAME=foo.connector"
	))
	useJUnitPlatform()
}

val unitTests = task<Test>("unit-tests") {
	jvmArgs(listOf(
		"-DHOST=http://localhost",
		"-DPORT=8083",
		"-DCONNECTOR_NAME=foo.connector"
	))
	useJUnitPlatform {
		excludeTags("integration-test")
	}
}

