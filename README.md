# Spring Boot Vault Sample

A Spring Boot 4 sample CRUD application that stores product data in an in-memory
H2 database and reads application secrets from a HashiCorp Vault server.

- **Vault server:** `https://vault.persis.or.id:8270`
- **Database:** H2 (in-memory) with console at `/h2-console`
- **Java:** 25
- **Build:** Maven (wrapper included)

## Requirements

- JDK 25 (if running locally)
- Docker & Docker Compose
- A Vault token with read access to `persislabs-dev/testing`

## Configuration

### 1. Set Up Secrets in Vault
Ensure the following secrets exist in your Vault server at path `persislabs-dev/testing`:

```bash
vault kv put persislabs-dev/testing \
  db.username="yu71" \
  db.password="53cret" \
  api.key="super-secret-key" \
  username="hendisantika"
```

### 2. Create App Token (Security Best Practice)
Avoid using the `root` token. Create a limited token for this app:

```bash
# Write policy
vault policy write spring-app-policy - <<EOF
path "persislabs-dev/data/testing" {
  capabilities = ["read"]
}
EOF

# Create token
vault token create -policy="spring-app-policy" -period=24h
```

### 3. Environment Variables
Create a `.env` file in the root directory and add your token:

```text
VAULT_TOKEN=hvs.xxxxxxxxxxxxxxxx
```

## Run Application

### Using Docker (Recommended)
```bash
docker-compose up --build
```

### Running Locally
```bash
export VAULT_TOKEN="hvs.xxxxxxxxxxxxxxxx"
./mvnw spring-boot:run
```

The app listens on `http://localhost:8080`.

## REST API

Base path: `/api/v1/products`

| Method | Path            | Description                          |
|--------|-----------------|--------------------------------------|
| GET    | `/`             | List all or `?name=` filter by name  |
| GET    | `/{id}`         | Get one product                      |
| POST   | `/`             | Create a product                     |
| PUT    | `/{id}`         | Update a product                     |
| DELETE | `/{id}`         | Delete a product                     |

### cURL Examples

```bash
# List
curl http://localhost:8080/api/v1/products

# Search
curl "http://localhost:8080/api/v1/products?name=ipad"
```

## H2 Console

Open `http://localhost:8080/h2-console`.

| Field    | Value                   |
|----------|-------------------------|
| JDBC URL | `jdbc:h2:mem:vaultdb`   |
| User     | `sa` (or from Vault)    |
| Password | (or from Vault)         |

## Vault

On startup, `VaultSecretReader` reads the KV v2 secret at `persislabs-dev/data/testing` and logs each key with a masked value.

## Author

Hendi Santika — [s.id/hendisantika](https://s.id/hendisantika)
