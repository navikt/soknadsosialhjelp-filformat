#!groovy
@Library('common')
import common

def common = new common()

application = "soknadsosialhjelp-xsd"
author = "Unknown"
deploy = "Unknown"
releaseVersion = "Unknown"
isMasterBuild = (env.BRANCH_NAME == 'master')

project = "navikt"
repoName = "soknadsosialhjelp-xsd"

def notifyFailed(reason, error, buildNr) {
    currentBuild.result = 'FAILED'

    notifyGithub("${project}", "${repoName}", "${commitHash}", 'failure', "Build #${buildNr} : ${reason}")

    throw error
}

def returnOk(message, buildNr) {
    echo "${message}"
    currentBuild.result = "SUCCESS"

    notifyGithub("${project}", "${repoName}", "${commitHash}", 'success', "Build #${buildNr}")
}

node {
    properties([
            parameters([
                    string(name: 'DeployTilNexus', defaultValue: 'false'),
            ])
    ])
    common.setupTools("maven3", "java8")

    stage('Checkout') {
        deleteDir()
        checkout scm
        commitHash = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()

        author = sh(returnStdout: true, script: 'git --no-pager show -s --format="%an <%ae>" HEAD').trim()
        notifyGithub("${project}", "${repoName}", "${commitHash}", 'pending', "Build #${env.BUILD_NUMBER} has started")

    }

    if (!isMasterBuild) {
        stage('Merge master') {
            sh "git merge origin/master"
        }
    }

    stage('Set version') {
        pom = readMavenPom file: 'pom.xml'
        def gitCommitNumber = sh(returnStdout: true, script: 'git rev-list --count HEAD').trim()
        def version = pom.version.replace("-SNAPSHOT", "")
        releaseVersion =  "${version}.${gitCommitNumber}.${currentBuild.number}"
        sh "mvn versions:set -DnewVersion=${releaseVersion}"
    }

    echo "${params.DeployTilNexus} deploy til nexus"
    if (isMasterBuild || params.DeployTilNexus == "true") {
        stage('Deploy nexus') {
            try {
                sh "mvn -B deploy -DskipTests -P pipeline"
                currentBuild.description = "Version: ${releaseVersion}"
                withEnv(['HTTPS_PROXY=http://webproxy-utvikler.nav.no:8088']) {
                    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'navikt-jenkins-github', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD']]) {
                        sh("git tag -a ${releaseVersion} -m ${releaseVersion} HEAD && git push --tags https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/navikt/soknadsosialhjelp-xsd.git")
                    }
                }
            } catch (Exception e) {
                notifyFailed("Deploy av artifakt til nexus feilet", e, env.BUILD_URL)
            }
        }
    }
}

node {
    returnOk('All good', env.BUILD_URL)
}

def notifyGithub(owner, repo, sha, state, description) {
    def postBody = [
            state: "${state}",
            context: 'continuous-integration/jenkins',
            description: "${description}",
            target_url: "${env.BUILD_URL}"
    ]
    def postBodyString = groovy.json.JsonOutput.toJson(postBody)

    withEnv(['HTTPS_PROXY=http://webproxy-utvikler.nav.no:8088']) {
        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'navikt-jenkins-github', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD']]) {
            sh "curl 'https://api.github.com/repos/${owner}/${repo}/statuses/${sha}?access_token=$GIT_PASSWORD' \
                -H 'Content-Type: application/json' \
                -X POST \
                -d '${postBodyString}'"
        }
    }
}