#/bin/bash!

cd ../backend

#set Mail Host
#if not set mail host would be "localhost"
export MAIL_HOST=mailcatcher

mvn -Dtest=TestDatenEinspieler test