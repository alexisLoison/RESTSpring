if docker service ls | grep mongo 
then
  echo " "
  echo "****************************"
  echo "** removing mongo service **"
  echo "****************************"
  echo " "
  docker service rm mongodb
  docker network rm demoSpring-net
else
  echo " "
  echo "******************************"
  echo "** removing mongo container **"
  echo "******************************"
  echo " "
  docker container stop $(docker container ls | grep mongodb | awk '{print $1}')
  docker container rm $(docker container ls -a | grep mongodb | awk '{print $1}')
fi
