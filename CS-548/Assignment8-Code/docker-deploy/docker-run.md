docker build --no-cache -t cs548/database .

docker run -d --name cs548db --network cs548-network -p 5432:5432 -v /data:/var/lib/postgresql/data -e POSTGRES_PASSWORD=XXXXXX -e PGDATA=/var/lib/postgresql/data/pgdata -e DATABASE_PASSWORD=YYYYYY cs548/database

----------
docker build --no-cache -t cs548/clinic-domain .

docker run -d --name clinic-domain --network cs548-network -p 5050:8080 -e DATABASE_PASSWORD=YYYYYY cs548/clinic-domain

------------
docker build --no-cache -t cs548/clinic-webapp .

docker run -d --name clinic-webapp --network cs548-network -p 8080:8080 -p 8181:8181 cs548/clinic-webapp

-------------

docker build --no-cache -t cs548/clinic-rest .

docker run -d --name clinic-rest --network cs548-network -p 9090:8080 -p 9191:8181 cs548/clinic-rest
