#/bin/bash!

docker rm -f $(docker ps -a -q)
docker volume rm $(docker volume ls -q)

cd target
docker-compose -p mHealth up -d
cd ..

./03_run_testdataImporter.sh