#/bin/bash!


WORKDIR="$(dirname "$(readlink -f "$0")")"
TARGETDIR=$WORKDIR/target
DOCKERDIR=$WORKDIR/../docker_files


build_backend(){
    echo -e "${BLUE}build backend${NC}"
    cd ../backend
    mvn clean install -DskipTests -q
    cd $WORKDIR
}

build_frontend(){
    echo -e "${BLUE}build frontend${NC}"
    cd ../mHealth
    npm install --silent 
    #ng build --prod --progress=false
    ionic capacitor build browser

    cd $WORKDIR
}

clean_target_dir(){
    echo -e "${BLUE}clean targetdir${NC}"
    rm -rf $TARGETDIR

    mkdir $TARGETDIR
    mkdir $TARGETDIR/backend
    mkdir $TARGETDIR/db
}

copy_builds_to_target(){
    echo -e "${BLUE}add backend to target"
    cp ../backend/target/backend-0.0.1-SNAPSHOT.jar $TARGETDIR/backend/backend.jar
    cp -r ../backend/target/libs $TARGETDIR/backend/libs

    echo "add frontend to target"
    cp -r ../mHealth/www $TARGETDIR/frontend

    echo "add db - createTables - to target"
    cp ../DB/createTables.sql $TARGETDIR/db/dump.sql

    echo "add docker-compose.yml to target"
    cp $DOCKERDIR/docker-compose.yml $TARGETDIR/docker-compose.yml

    echo -e "add nginx.conf to target${NC}"
    cp $DOCKERDIR/nginx.conf $TARGETDIR/nginx.conf
}


build_backend
build_frontend

clean_target_dir
copy_builds_to_target