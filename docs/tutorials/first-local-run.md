# First local run

## Goal

Create a verified local or test execution of Marcus Pauli Portfolio Stack.

## Prerequisites

- A checkout of `marcus-portfolio`
- Tooling compatible with Astro, React, Strapi, PostgreSQL, Meilisearch, Podman, nginx
- No production secrets in the repository working tree

## Procedure

1. Run `./scripts/bootstrap.sh` on the target host.
2. Populate secret environment files below `/srv/portfolio/config`.
3. Deploy with `./scripts/deploy.sh`, install the systemd unit, and configure nginx.
4. Verify all containers, public routes, CMS access, and search.

## Verification

```bash
cd frontend && npm ci
cd frontend && npm run lint
cd frontend && npm run typecheck
cd frontend && npm run test:run
cd frontend && npm run build
cd strapi && npm ci && npm run build
```
