#/bin/bash!

cd ../backend

#set Mail Host
#if not set mail host would be "localhost"
export MAIL_HOST=mHealth.mb-media.net

mvn -Dtest=TestDatenEinspieler test