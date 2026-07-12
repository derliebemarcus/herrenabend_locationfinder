@Library('jenkins-shared-library@main') _

ciRepositoryDocumentationContract(
    scm: scm,
    agentLabel: 'klymene',
)

if (ciDocumentationOnlyShortcut(
    scm: scm,
    agentLabel: 'klymene',
    repository: [
        owner: 'derliebemarcus',
        name: 'herrenabend_locationfinder',
    ],
    github: [
        credentialId: 'github token',
        statusContext: 'Continuous Integration / Jenkins',
        title: 'Herrenabend Locationfinder Quality Gates',
    ],
)) {
    return
}

pipeline {
    agent any

    options {
        ansiColor('xterm')
        timestamps()
        disableConcurrentBuilds()
    }

    tools {
        nodejs '22'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Secret Scanning') {
            steps {
                script {
                    echo "Running gitleaks to detect secrets..."
                    docker.image('zricethezav/gitleaks:latest').inside('--entrypoint=""') {
                        sh 'gitleaks detect --source="." -v || true'
                    }
                    sh 'sudo chown -R $(id -u):$(id -g) ${WORKSPACE} 2>/dev/null || true'
                }
            }
        }

        stage('Security Audit') {
            steps {
                echo "Running npm audit..."
                sh 'npm audit --audit-level=high || echo "Ignoring upstream vulnerabilities for now."'
            }
        }

        stage('Install Dependencies') {
            steps {
                sh 'npm ci --registry=https://artifacts.home.siczb.de/repository/npm-proxy/ || npm ci'
            }
        }

        stage('Build (Dry-Run)') {
            steps {
                echo "Verifying build..."
                sh 'npm run build'
            }
        }

        stage('Push to Harbor') {
            when {
                branch 'main'
            }
            steps {
                script {
                    docker.withRegistry('https://registry.home.siczb.de', 'harbor-jenkins-user') {
                        echo "Building and pushing production image to Harbor..."
                        def appImg = docker.build("registry.home.siczb.de/siczb/herrenabend_locationfinder:latest", "-f Containerfile .")
                        appImg.push()
                    }
                }
            }
        }
    }

    post {
        always {
            githubNotify(
                credentialsId: 'github token',
                status: "${currentBuild.result == 'ABORTED' ? 'FAILURE' : (currentBuild.result ?: 'SUCCESS')}",
                context: 'Continuous Integration / Jenkins',
                description: "Build ${currentBuild.result ?: 'IN PROGRESS'}"
            )
            publishChecks(
                name: 'Jenkins Build',
                title: 'Herrenabend Locationfinder Build',
                summary: "Status: ${currentBuild.result ?: 'IN PROGRESS'}",
                conclusion: (currentBuild.result == 'SUCCESS' ? 'SUCCESS' : (currentBuild.result == 'FAILURE' ? 'FAILURE' : (currentBuild.result == 'UNSTABLE' ? 'NEUTRAL' : 'PENDING'))),
                status: (currentBuild.result ? 'COMPLETED' : 'IN_PROGRESS')
            )
        }
    }
}
