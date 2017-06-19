if uname -s | grep MINGW
then
  if docker-machine ls | grep leader
  then
    echo "machines are already ready"
  else
    docker-machine create --engine-env 'DOCKER_OPTS="-H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock"' --driver virtualbox leader
    eval "$(docker-machine env leader)"
    echo " "
    echo "******************************"
    echo "** setting up the registry **"
    echo "******************************"
    echo " "
    eval "$(docker-machine env leader)"
  

    ip_leader=$(docker-machine ip leader)
    echo "swarm init..."
    docker swarm init --listen-addr $ip_leader --advertise-addr $ip_leader
    sleep 1
    leaderName=$(docker node ls | grep '*' | awk '{print $3}')
    echo "leader name: " $leaderName
    echo "leader IP: " $ip_leader
    echo "building registry service..."
    docker service create --constraint=node.hostname==$leaderName --name registry --publish 5000:5000 registry:2
  
    until docker service ls | grep registry
    do
      echo "registry failed to start..."
	  sleep 1
    done

    echo "building worker1 machine..."
    docker-machine create --engine-env 'DOCKER_OPTS="-H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock"' --driver virtualbox --engine-insecure-registry $ip_leader:5000 worker1
    echo "building worker2 machine..."
    docker-machine create --engine-env 'DOCKER_OPTS="-H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock"' --driver virtualbox --engine-insecure-registry $ip_leader:5000 worker2



    eval "$(docker-machine env leader)"

    token=$(docker swarm join-token worker -q)
    echo "token: " $token

    eval "$(docker-machine env worker1)"
    docker swarm join --token $token $ip_leader:2377
    echo "worker1 joined the swarm"

    eval "$(docker-machine env worker2)"
    docker swarm join --token $token $ip_leader:2377
    echo "worker2 joined the swarm"
  fi
else
  ip_leader=$(docker node inspect $(docker node ls | grep '*' | awk '{print $3}') | jq -r .[].Status.Addr)
  leaderName=$(docker node ls | grep '*' | awk '{print $3}')
  echo "leader name: " $leaderName
  echo "leader IP: " $ip_leader
  echo "building registry service..."
  docker service create --constraint=node.hostname==$leaderName --name registry --publish 5000:5000 registry:2
  until docker service ls | grep registry
  do
    echo "registry failed to start..."
	sleep 1
  done
fi
