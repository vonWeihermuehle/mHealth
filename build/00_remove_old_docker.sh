#/bin/bash!

docker rm -f $(docker ps -a -q)
docker volume rm $(docker volume ls -q)

rm -rf target/

sh ./01_build.sh