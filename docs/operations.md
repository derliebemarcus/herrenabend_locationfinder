# Operations Guide

## Backups

| Component | Path | Strategy |
|-----------|------|----------|
| PostgreSQL | `/srv/portfolio/data/postgres` | `podman exec portfolio-postgres pg_dumpall -U strapi > backup.sql` |
| Strapi uploads | `/srv/portfolio/data/strapi/uploads` | Incremental sync (`rsync` or object storage) |
| Meilisearch | `/srv/portfolio/data/meili` | Snapshot via `meilisearch --dump` or filesystem backup |

Automate backups via cron and store off-site.

## Restore

1. Stop systemd service: `sudo systemctl stop podman-portfolio`.
2. Restore PostgreSQL data directory and uploads.
3. Restore Meilisearch snapshot (optional).
4. Start service: `sudo systemctl start podman-portfolio`.
5. Run `strapi/scripts/meili-sync.js` to repopulate search indexes.

## Monitoring

- **Systemd**: `systemctl status podman-portfolio`
- **Podman**: `podman ps`, `podman logs portfolio-frontend`
- **Nginx**: `/var/log/nginx/marcuspauli-error.log`
- **Strapi health**: `curl http://127.0.0.1:8201/_health`

Consider integrating with Prometheus/Grafana if available.

## Updates

1. Pull latest repository changes.
2. Run `podman-compose pull` to refresh base images.
3. Rebuild Strapi/Frontend:

   ```bash
   podman-compose run --rm strapi npm ci && npm run build
   podman-compose run --rm frontend npm ci && npm run build
   ```

4. Restart stack (`podman-compose up -d`).

## Security

- Rotate secrets in `config/*.env` regularly.
- Ensure TLS certificates are renewed (nginx includes assume existing Certbot automation).
- Keep Podman images current.
- Restrict SSH access; use firewalld/iptables to expose only HTTPS.

## Incident Response

1. Check systemd and container logs.
2. Validate Strapi availability and database connectivity.
3. If Meilisearch degraded, reapply `scripts/meili-config.sh` and `strapi/scripts/meili-sync.js`.
4. For frontend regressions, roll back by redeploying previous Git commit.
