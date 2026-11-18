FROM gvenzl/oracle-xe:21-slim
COPY HOSPITAL_EXPORT/ /seed-dump/
COPY infra/oracle/create_hospital_user.sql /restore/create_hospital_user.sql
COPY infra/oracle/seed_reception_demo.sql /restore/seed_reception_demo.sql
COPY infra/oracle/migrate_bill_item.sql /restore/migrate_bill_item.sql
COPY infra/oracle/migrate_kakaopay.sql /restore/migrate_kakaopay.sql
