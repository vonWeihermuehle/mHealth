#/bin/bash!

WORKDIR="$(dirname "$(readlink -f "$0")")"
TARGETDIR=$WORKDIR/target
DOCKERDIR=$WORKDIR/../docker_files

NOBUILD=0

RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;36m'
NC='\033[0m' # No Color

cd $WORKDIR


args=( )
while (( $# )); do
  case $1 in
    --nobuild)  NOBUILD=1 ;;
    --test) echo "test";;
    -*)        printf 'Unknown option: %q\n\n' "$1" ;;
    *)         args+=( "$1" ) ;;
  esac
  shift
done




check_dependencies(){
    echo -e "${BLUE}check dependencies${NC}"

#check if java installed
    if java -version 2>&1 >/dev/null | grep "java version\|openjdk version" ; then
        echo ""
    else
        echo -e "${RED}JAVA is not installed${NC}"
        exit 1
    fi

#check if maven installed
    if mvn -v | grep Maven ; then
        echo ""
    else
        echo -e "${RED}Maven is not installed${NC}"
        exit 1
    fi
#check if npm installed
    if npm | grep Usage ; then
        echo ""
    else
        echo -e "${RED}NPM is not installed${NC}"
        exit 1
    fi
#check if angular and node installed
    if ng version | grep Node ; then
        echo ""
    else
        echo -e "${RED}Angular is not installed${NC}"
        exit 1
    fi
#check if ionic installed
    if ionic info | grep Ionic ; then
        echo ""
    else
        echo -e "${RED}IONIC is not installed${NC}"
        exit 1
    fi

}

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

check_dependencies

if [ $NOBUILD = 1 ]; then
    echo "NOBUILD Flag was set"
else
    build_backend
    build_frontend
fi
clean_target_dir
copy_builds_to_target


sh ./02_deploy.sh