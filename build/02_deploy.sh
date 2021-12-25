#/bin/bash!

cd target
docker-compose -p mHealth up -d

./03_run_testdataImporter.sh