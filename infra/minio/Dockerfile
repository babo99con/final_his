FROM golang:1.24-bookworm AS build

ENV CGO_ENABLED=0
RUN go install github.com/minio/minio@7aac2a2c5b7c882e68c1ce017d8256be2feea27f \
    && go install github.com/minio/mc@77f82e18b5401a65958f1619df6ebb994634bd88

FROM debian:bookworm-slim
RUN apt-get update && apt-get install -y --no-install-recommends ca-certificates \
    && rm -rf /var/lib/apt/lists/*
COPY --from=build /go/bin/minio /usr/local/bin/minio
COPY --from=build /go/bin/mc /usr/local/bin/mc
ENTRYPOINT ["minio"]
CMD ["server", "/data", "--console-address", ":9001"]
