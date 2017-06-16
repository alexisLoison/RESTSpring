docker-machine create --engine-env 'DOCKER_OPTS="-H unix:///var/run/docker.sock"' --driver virtualbox leader
eval "$(docker-machine env leader)"
echo " "
echo "******************************"
echo "** setting up the registry **"
echo "******************************"
echo " "
docker pull registry
docker run -d -p 5000:5000 --restart always --name registry registry:latest

ip_leader=$(docker-machine ip leader)

docker-machine create --engine-env 'DOCKER_OPTS="-H unix:///var/run/docker.sock"' --driver virtualbox --engine-insecure-registry $ip_leader:5000 worker1
docker-machine create --engine-env 'DOCKER_OPTS="-H unix:///var/run/docker.sock"' --driver virtualbox --engine-insecure-registry $ip_leader:5000 worker2



eval "$(docker-machine env leader)"
docker swarm init --listen-addr $ip_leader --advertise-addr $ip_leader

token=$(docker swarm join-token worker -q)

eval "$(docker-machine env worker1)"
docker swarm join --token $token $ip_leader:2377

eval "$(docker-machine env worker2)"
docker swarm join --token $token $ip_leader:2377