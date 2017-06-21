eval "$(docker-machine env leader)"

leaderName=$(docker node ls | grep '*' | awk '{print $3}')
registryPort=5000
registryIP=$(docker node inspect $(docker node ls | grep '*' | awk '{print $3}') | jq -r .[].Status.Addr)

echo "leaderName: " $leaderName
echo "registryIP: " $registryIP
echo "registryPort: " $registryPort

echo " "
echo "*******************************"
echo "** building rabbitmq service **"
echo "*******************************"
echo " "
docker service create --name rabbitmq --publish 15672:15672 --network demoSpring-net rabbitmq:3-management

echo " "
echo "*******************************"
echo "** building zookeeper service **"
echo "*******************************"
echo " "
#docker service create --name zookeeper --network demoSpring-net --constraint=node.role==manager --publish 2181:2181 wurstmeister/zookeeper

echo " "
echo "*******************************"
echo "** building kafka service **"
echo "*******************************"
echo " "
#docker service create --name kafka --mode global -e 'KAFKA_PORT=9092' -e 'KAFKA_ADVERTISED_PORT=9092' -e 'KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092' -e 'KAFKA_ZOOKEEPER_CONNECT=tasks.zookeeper:2181' -e "HOSTNAME_COMMAND=ip r | awk '{ ip[\$3] = \$NF} END { print ( ip[\"eth0\"] ) }'" --publish 9092:9092 --network demoSpring-net wurstmeister/kafka

echo " "
echo "*******************************************"
echo "** waiting for registry to be available **"
echo "*******************************************"
echo " "
until docker service ls | grep registry
do
  sleep 1
  echo "waiting..."
done
  
echo " "
echo "******************************"
echo "** building Eureka service **"
echo "******************************"
echo " "
gradle -b Eureka/build.gradle build
docker build -t eureka Eureka/
if docker service ls | grep eureka
then
  echo " "
  echo "***************************************"
  echo "** removing existing Eureka service **"
  echo "***************************************"
  echo " "
  docker service rm eureka
  sleep 1
fi
echo " "
echo "*******************************************"
echo "** waiting for eureka image to be built **"
echo "*******************************************"
echo " "
until docker images | grep eureka
do
  sleep 1
  echo "waiting..."
done
docker tag eureka localhost:$registryPort/eureka:latest
docker push localhost:$registryPort/eureka:latest
echo " "
echo "*******************************"
echo "** deploying Eureka service **"
echo "*******************************"
echo " "
docker service create --name eureka --publish 8761:8761 --network demoSpring-net localhost:$registryPort/eureka:latest


echo " "
echo "******************************"
echo "** building student service **"
echo "******************************"
echo " "
gradle -b Students/build.gradle build
docker build -t student Students/
if docker service ls | grep student
then
  echo " "
  echo "***************************************"
  echo "** removing existing student service **"
  echo "***************************************"
  echo " "
  docker service rm student
  sleep 1
fi
echo " "
echo "*******************************************"
echo "** waiting for student image to be built **"
echo "*******************************************"
echo " "
until docker images | grep student
do
  sleep 1
  echo "waiting..."
done
docker tag student localhost:$registryPort/student:latest
docker push localhost:$registryPort/student:latest
echo " "
echo "*******************************"
echo "** deploying student service **"
echo "*******************************"
echo " "
docker service create --publish 1111:1111 --name student --env SPRING_PROFILES_ACTIVE=docker --network demoSpring-net --constraint=node.hostname==$leaderName localhost:$registryPort/student:latest


echo " "
echo "******************************"
echo "** building teacher service **"
echo "******************************"
echo " "
gradle -b Teachers/build.gradle build
docker build -t teacher Teachers/
if docker service ls | grep teacher
then
  echo " "
  echo "***************************************"
  echo "** removing existing teacher service **"
  echo "***************************************"
  echo " "
  docker service rm teacher
  sleep 1
fi
echo " "
echo "*******************************************"
echo "** waiting for teacher image to be built **"
echo "*******************************************"
echo " "
until docker images | grep teacher
do
  sleep 1
  echo "waiting..."
done
docker tag teacher localhost:$registryPort/teacher:latest
docker push localhost:$registryPort/teacher:latest
echo " "
echo "*******************************"
echo "** deploying teacher service **"
echo "*******************************"
echo " "
docker service create --publish 2222:2222 --name teacher --env SPRING_PROFILES_ACTIVE=docker --constraint=node.hostname==$leaderName --network demoSpring-net localhost:$registryPort/teacher:latest


echo " "
echo "***************************"
echo "** building zuul service **"
echo "***************************"
echo " "
gradle -b Zuul/build.gradle build
docker build -t zuul Zuul/
if docker service ls | grep zuul
then
  echo " "
  echo "************************************"
  echo "** removing existing zuul service **"
  echo "************************************"
  echo " "
  docker service rm zuul
  sleep 1
fi
echo " "
echo "*******************************************"
echo "** waiting for zuul image to be built **"
echo "*******************************************"
echo " "
until docker images | grep zuul
do
  sleep 1
  echo "waiting..."
done
docker tag zuul localhost:$registryPort/zuul:latest
docker push localhost:$registryPort/zuul:latest
echo " "
echo "****************************"
echo "** deploying zuul service **"
echo "****************************"
echo " "
docker service create --publish 8765:8765 --name zuul --env SPRING_PROFILES_ACTIVE=docker --constraint=node.hostname==$leaderName --network demoSpring-net localhost:$registryPort/zuul:latest

sleep 1

echo " "
echo "**************************************************"
echo "**************************************************"
echo "** reaching port for eureka service:    " $(docker service ls | grep eureka | awk '{print $11}' | cut -d '-' -f 1 | cut -d ':' -f 2) " **"
echo "** reaching port for zuul service:      " $(docker service ls | grep zuul | awk '{print $11}' | cut -d '-' -f 1 | cut -d ':' -f 2) " **"
echo "** reaching port for teacher service: " $(docker service ls | grep teacher | awk '{print $11}' | cut -d '-' -f 1 | cut -d ':' -f 2) " **"
echo "** reaching port for student service: " $(docker service ls | grep student | awk '{print $11}' | cut -d '-' -f 1 | cut -d ':' -f 2) " **"
echo "**************************************************"
echo "**************************************************"
echo " "
