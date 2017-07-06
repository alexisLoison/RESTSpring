if uname -s | grep MINGW
then
  if docker-machine ls | grep leader 
  then
    echo " "
    echo "****************************"
    echo "** removing mongo service **"
    echo "****************************"
    echo " "
    docker service rm student
    docker service rm teacher
    docker service rm eureka
    docker service rm zuul
    docker service rm rabbitmq
    docker service rm kafka
    docker service rm mongodb
	docker service rm config
    docker network rm demoSpring-net
    echo "**********************************************************"
    echo "**********************************************************"
    echo "** Do you want to delete the whole swarm and machines ? **"
    echo "** 1                                                yes **"
    echo "** 2                                                 no **"
    echo "**********************************************************"
    echo "**********************************************************"
    read rep

    if [ $rep = 1 ]
    then
      eval "$(docker-machine env leader)"
      docker service rm registry
      docker-machine stop worker1 worker2 leader
      docker-machine rm worker1 worker2 leader
    fi
  
  else
    sh resetTnScont.sh
    echo " "
    echo "******************************"
    echo "** removing mongo container **"
    echo "******************************"
    echo " "
    docker container stop $(docker container ls | grep mongodb | awk '{print $1}')
    docker container rm $(docker container ls -a | grep mongodb | awk '{print $1}')
    while docker container ls -a | grep mongodb
    do
      sleep 1
	  echo "waiting for mongodb to be deleted..."
    done
    docker network rm demo-net
  fi
else
  if docker service ls | grep student
  then
    echo " "
    echo "****************************"
    echo "** removing mongo service **"
    echo "****************************"
    echo " "
    docker service rm student
    docker service rm teacher
    docker service rm eureka
    docker service rm zuul
    docker service rm rabbitmq
    docker service rm kafka
    docker service rm mongodb
	docker service rm config
    docker network rm demoSpring-net
    echo "******************************************"
    echo "******************************************"
    echo "** Do you want to delete the registry ? **"
    echo "** 1                                yes **"
    echo "** 2                                 no **"
    echo "******************************************"
    echo "******************************************"
    read rep

    if [ $rep = 1 ]
    then
      docker service rm registry
	fi
  else
    sh resetTnScont.sh
    echo " "
    echo "******************************"
    echo "** removing mongo container **"
    echo "******************************"
    echo " "
    docker container stop $(docker container ls | grep mongodb | awk '{print $1}')
    docker container rm $(docker container ls -a | grep mongodb | awk '{print $1}')
    while docker container ls -a | grep mongodb
    do
      sleep 1
	  echo "waiting for mongodb to be deleted..."
    done
    docker network rm demo-net
  fi
fi	