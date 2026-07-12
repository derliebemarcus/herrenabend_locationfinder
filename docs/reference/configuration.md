# Configuration

- `config/postgres.env` defines database credentials.
- `config/strapi.env` defines CMS, database, application, and admin secrets.
- `config/meili.env` defines search configuration and the master key.
- `config/frontend.env` defines internal and public endpoints and optional media URLs.

Secrets are referenced from protected runtime stores and must never be committed.
