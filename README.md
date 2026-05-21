# final_his

HIS backend microservices, Next.js frontend, Oracle XE dump, and Docker Compose runtime.

## Quick Start

Prerequisites:

- Docker Desktop
- Git

Run everything with Compose:

```powershell
git clone https://github.com/babo99con/final_his.git
cd final_his
Copy-Item .env.example .env
```

Edit `.env` and set `TOSS_SECRET_KEY` if Toss payment approval is needed. Then run:

```powershell
docker compose up --build
```

Open:

```text
http://localhost:3001
```

If another local Oracle/Kafka/Redis/MinIO is already running, change the host ports in `.env` before `docker compose up --build`. On a clean machine, keep the defaults.

Login:

```text
ID: 26-6001
PW: 1111
```

## What Compose Starts

- Oracle XE 21c on `localhost:1521`
- HOSPITAL schema restore from `HOSPITAL_EXPORT/hospital_xe_20260521_212722.dmp`
- Kafka on `localhost:9092`
- Redis on `localhost:6379`
- MinIO on `localhost:9000`, console on `localhost:9001`
- Backend services:
  - auth-service `8586`
  - patient-service `8182`
  - reception-service `8283`
  - staff-service `8023`
  - clinical-service `8090`
  - clinic-support-service `8189`
  - billing-service `8081`
- frontend `3001`

All host ports above can be overridden in `.env` without changing the internal service-to-service ports.

## Reset Everything

To remove containers and database volume:

```powershell
docker compose down -v
```

Then run again:

```powershell
docker compose up --build
```

## Important Notes

- The app uses Oracle service name `XE`, not `XEPDB1`.
- The HOSPITAL database account is `HOSPITAL / 1111`.
- Kafka consumer groups are:
  - `clinical-billing`
  - `clinical-support`
  - `support-billing`
  - `support-clinical`
- Do not commit real secret keys. `.env` is ignored.
- Public API keys are not required for the current local test data. Drug/diagnosis/procedure search currently reads local `HOSPITAL.CODE_DETAIL`, which is empty in the dump.

More detailed manual setup notes are in `LOCAL_SETUP_README.md`.
