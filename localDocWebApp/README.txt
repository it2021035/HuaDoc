docker run --name ds-lab-pg --rm \
-e POSTGRES_PASSWORD=pass123 \
-e POSTGRES_USER=dbuser \
-e POSTGRES_DB=appdb \
-d --net=host \
-v ds-lab-vol:/var/lib/postgresql/data \
postgres:14


***windows***
docker run --name ds-lab-pg --rm -p 5432:5432 -e POSTGRES_PASSWORD=pass123  -e POSTGRES_USER=dbuser  -e POSTGRES_DB=appdb   -v ds-lab-vol:/var/lib/postgresql/data  postgres:14

