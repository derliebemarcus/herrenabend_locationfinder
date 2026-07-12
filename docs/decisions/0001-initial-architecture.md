# 0001: Separate content management, presentation, search, and persistence services.

## Status

Accepted

## Date

2026-07-04

## Context

Bilingual portfolio platform combining an Astro SSR frontend, Strapi CMS, Meilisearch, PostgreSQL, nginx, Podman, and systemd.

## Decision drivers

- Accessible and responsive public pages
- Reliable content localization
- Reproducible container builds

## Considered options

1. Retain the established architecture
2. Replace it with a tightly coupled alternative
3. Defer the architectural boundary to deployment-specific code

## Decision

Separate content management, presentation, search, and persistence services.

## Rationale

The architecture allows independent content editing, SSR presentation, faceted search, and durable relational storage while retaining containerized deployment.

## Consequences

- The documented building blocks and interfaces remain explicit contracts.
- Changes to the decision require a superseding ADR.

## Risks

- CMS, frontend, and search schema changes can drift.
- Container or host-level configuration drift can break deployments.

## References

- maintenance issue #37
