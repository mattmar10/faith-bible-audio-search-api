docker run --name postgres-9.5.4 -p 5432:5432 -e POSTGRES_USER=fbc_svc_user -e POSTGRES_DB=fbc_media_data -e POSTGRES_PASSWORD=devpassword -d postgres:9.5.4

docker run -p 80:80 \
-e "PGADMIN_DEFAULT_EMAIL=user@domain.com" \
-e "PGADMIN_DEFAULT_PASSWORD=SuperSecret" \
-d dpage/pgadmin4