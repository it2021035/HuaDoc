docker run --name ds-lab-pg --rm \
-e POSTGRES_PASSWORD=pass123 \
-e POSTGRES_USER=dbuser \
-e POSTGRES_DB=appdb \
-d --net=host \
-v ds-lab-vol:/var/lib/postgresql/data \
postgres:14
lo