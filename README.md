## Payment Portfolio Backend

### Supabase config per environment

This project loads configuration in this order:

- `application.properties`
- `application-${APP_ENV}.properties` (optional; e.g. `application-dev.properties`)
- Environment variables override everything (`SUPABASE_URL`, `SUPABASE_KEY`, `DB_URL`, ...)

### Run (dev example)

With OS env var:

```bash
APP_ENV=dev SUPABASE_URL="https://xyz.supabase.co" SUPABASE_KEY="..." \
DB_URL="jdbc:postgresql://db.xyz.supabase.co:5432/postgres?user=postgres&password=..." \
mvn -q -DskipTests package
java -cp target/classes:target/dependency/* org.example.Main
```

With JVM system property:

```bash
mvn -q -DskipTests package
java -Dapp.env=dev -cp target/classes:target/dependency/* org.example.Main
```

See `.env.example` for the full list of supported variables.

