#/bin/bash!

cd target
docker-compose -p mHealth up -d
cd ..

./03_run_testdataImporter.sh