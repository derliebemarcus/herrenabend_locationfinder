@Library('jenkins-shared-library@main') _

ciRepositoryPipeline(
    profile: 'static-site',
    repository: [
        provider: 'forgejo',
        owner: 'siczb',
        name: 'herrenabend_locationfinder',
    ],
    scmStatus: [
        context: 'Continuous Integration / Jenkins',
        title: 'Herrenabend Locationfinder CI',
        credentialId: 'forgejo',
        transport: 'api',
    ],
    features: [
        documentationOnly: [enabled: true],
        repositoryDocumentation: [enabled: true],
    ],
    profileConfig: [
        stages: [[
            name: 'Build Astro Site',
            command: '''#!/usr/bin/env bash
                set -euo pipefail
                image="herrenabend-locationfinder-build-${BUILD_TAG//[^A-Za-z0-9_.-]/-}"
                trap 'podman image rm --force "${image}" >/dev/null 2>&1 || true' EXIT
                podman build \
                  --pull=always \
                  --target build \
                  --file Containerfile \
                  --tag "${image}" .
            ''',
            cleanupWorkspace: false,
        ]],
        container: [
            enabled: true,
            registry: 'registry.home.siczb.de',
            image: 'siczb/herrenabend_locationfinder',
            credentialId: 'harbor-jenkins-user',
            containerfile: 'Containerfile',
            branches: ['main'],
            tags: ['latest'],
        ],
    ],
)
