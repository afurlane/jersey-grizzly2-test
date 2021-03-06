pipeline {
    // Tools can be global or "per-stage".
    // agent { label 'cloud' }
    agent any
    stages {
        stage('Build') {
            tools {
               jdk "OpenJDK11"
            }
            steps {
                withMaven (
                    // Maven installation declared in the Jenkins "Global Tool Configuration"
                    maven: 'Maven 3.3.9',
                    options: [artifactsPublisher(disabled:true)]) {
                    // /,
                    // Maven settings.xml file defined with the Jenkins Config File Provider Plugin
                    // Maven settings and global settings can also be defined in Jenkins Global Tools Configuration
                    // mavenSettingsConfig: 'my-maven-settings',
                    // mavenLocalRepo: '.repository')

                    // Run the maven build
                    // sh "mvn test sonar:sonar"
                    configFileProvider(
                                    [configFile(fileId: '3eb54662-52ab-4d94-afa2-e20c41c452e5', variable: 'MAVEN_SETTINGS')]) {
                        // sh "mvn -s $MAVEN_SETTINGS clean clover:setup test clover:aggregate clover:clover sonar:sonar clean compile package"
                        sh "mvn -s $MAVEN_SETTINGS clean verify"
                        }
                } // withMaven will discover the generated Maven artifacts, JUnit Surefire & FailSafe reports and FindBugs reports
            }
        }
        stage('Deploy') {
            tools {
               jdk "OpenJDK11"
            }
            steps {
                withMaven (
                    // Maven installation declared in the Jenkins "Global Tool Configuration"
                    maven: 'Maven 3.3.9',
                    options: [artifactsPublisher(disabled:true)]) {
                    // /,
                    // Maven settings.xml file defined with the Jenkins Config File Provider Plugin
                    // Maven settings and global settings can also be defined in Jenkins Global Tools Configuration
                    // mavenSettingsConfig: 'my-maven-settings',
                    // mavenLocalRepo: '.repository')

                    // Run the maven build
                    configFileProvider(
                                    [configFile(fileId: '3eb54662-52ab-4d94-afa2-e20c41c452e5', variable: 'MAVEN_SETTINGS')]) {
                        sh "mvn -s $MAVEN_SETTINGS cargo:deploy"
                    }

                }
            }
        }
    }
    post {
        always {
            junit 'build/reports/**/*.xml'
        }
    }
}