leaderName=$(docker node ls | grep '*' | awk '{print $3}')
registryPort=5000
registryIP=$(hostname - I | awk '{print $1}')

echo " "
echo "******************************"
echo "** setting up the registry **"
echo "******************************"
echo " "
docker pull registry
docker run -d -p 5000:5000 --restart always --name registry registry:latest
echo " "
echo "*******************************************"
echo "** waiting for registry to be available **"
echo "*******************************************"
echo " "
until docker container ls | grep registry
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
docker build -t microservicedemo/eureka Eureka/
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
docker tag microservicedemo/eureka $(registryIP):$(registryPort)/microservicedemo/eureka
docker push $(registryIP):$(registryPort)/microservicedemo/eureka
echo " "
echo "*******************************"
echo "** deploying Eureka service **"
echo "*******************************"
echo " "
docker service create --name eureka --publish 8761:8761 --network demoSpring-net microservicedemo/eureka


echo " "
echo "******************************"
echo "** building student service **"
echo "******************************"
echo " "
gradle -b Students/build.gradle build
docker build -t microservicedemo/student Students/
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
docker tag microservicedemo/student $(registryIP):$(registryPort)/microservicedemo/student
docker push $(registryIP):$(registryPort)/microservicedemo/student
echo " "
echo "*******************************"
echo "** deploying student service **"
echo "*******************************"
echo " "
docker service create --publish 1111:1111 --name student --network demoSpring-net microservicedemo/student


echo " "
echo "******************************"
echo "** building teacher service **"
echo "******************************"
echo " "
gradle -b Teachers/build.gradle build
docker build -t microservicedemo/teacher Teachers/
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
docker tag microservicedemo/teacher $(registryIP):$(registryPort)/microservicedemo/teacher
docker push $(registryIP):$(registryPort)/microservicedemo/teacher
echo " "
echo "*******************************"
echo "** deploying teacher service **"
echo "*******************************"
echo " "
docker service create --publish 2222:2222 --name teacher --network demoSpring-net microservicedemo/teacher


echo " "
echo "***************************"
echo "** building zuul service **"
echo "***************************"
echo " "
gradle -b Zuul/build.gradle build
docker build -t microservicedemo/zuul Zuul/
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
docker tag microservicedemo/zuul $(registryIP):$(registryPort)/microservicedemo/zuul
docker push $(registryIP):$(registryPort)/microservicedemo/zuul
echo " "
echo "****************************"
echo "** deploying zuul service **"
echo "****************************"
echo " "
docker service create --publish 8765:8765 --name zuul --constraint=node.hostname==$leaderName --network demoSpring-net microservicedemo/zuul

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
