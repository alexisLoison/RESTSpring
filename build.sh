echo "*******************************************************"
echo "*******************************************************"
echo "** running mongo as a container or a swarm service ? **"
echo "** 1                                       container **"
echo "** 2                                   swarm service **"
echo "*******************************************************"
echo "*******************************************************"
read rep
if [ $rep = "1" ]
then
  docker network create --driver=bridge demo-net
  docker run -p 27017:27017 -d --network demo-net --name mongodb mongo
  sleep 1
  port=$(docker container ls | grep mongo | cut -d ':' -f 2 | cut -d '-' -f 1)
  echo " "
  echo "****************************************************************"
  echo "****************************************************************"
  echo "**                                                            **"
  echo "** exposed port for mongo container: " $port "                  **"
  echo "** You will be connected to mongo container,                  **"
  echo "** please create a database collection following those tasks: **"
  echo "** mongo                                                      **"
  echo "** use microserviceblog                                       **"
  echo "** db.createCollection('testCollection')                      **"
  echo "** then you just have to leave container by typing exit       **"
  echo "**                                                            **"
  echo "****************************************************************"
  echo "****************************************************************"
  echo " "
  docker exec -it mongodb sh
elif [ $rep = "2" ]
then
  leaderName=$(docker node ls | grep '*' | awk '{print $3}')
  echo "leaderName: " $leaderName
  docker network create -d overlay --subnet=192.168.204.0/26 demoSpring-net
  docker service create --name mongodb --network demoSpring-net --publish 27017:27017/tcp --constraint=node.hostname==$leaderName mongo
  until test $(docker service ls | grep mongodb | cut -d '/' -f 1 | tail -c 2) == 1	
  do
    sleep 1
    echo " "
    echo "***************************************************"
    echo "** waiting for mongodb service to be in up state **"
    echo "***************************************************"
    echo " "
  done
  port=$(docker container ls | grep mongo | cut -d '/' -f 1 | tail -c 6)
  echo " "
  echo " "
  echo "****************************************************************"
  echo "****************************************************************"
  echo "**                                                            **"
  echo "** exposed port for mongo container: " $port "                  **"
  echo "** You will be connected to mongo container,                  **"
  echo "** please create a database collection following those tasks: **"
  echo "** mongo                                                      **"
  echo "** use microserviceblog                                       **"
  echo "** db.createCollection('testCollection')                      **"
  echo "** then you just have to leave container by typing exit       **"
  echo "**                                                            **"
  echo "****************************************************************"
  echo "****************************************************************"
  echo " "
  docker exec -it $(docker container ls --no-trunc | grep mongodb | awk '{print $1}') sh
else
  echo " "
  echo "****************************************************"
  echo "** your answer did not match with proposed choice **"
  echo "****************************************************"
  echo " "
fi

