echo " "
echo "******************************"
echo "** building Eureka service **"
echo "******************************"
echo " "
gradle -b Eureka/build.gradle build
docker build -t microservicedemo/eureka Eureka/
if docker container ls -a | grep eureka
then
  echo " "
  echo "***************************************"
  echo "** removing existing Eureka service **"
  echo "***************************************"
  echo " "
  docker container stop eureka
  docker container rm eureka
  sleep 1
fi
echo " "
echo "*******************************"
echo "** deploying Eureka service **"
echo "*******************************"
echo " "
#docker run -p 8761:8761 -d --name eureka microservicedemo/eureka
echo " "
echo "******************************"
echo "** building student service **"
echo "******************************"
echo " "
gradle -b Students/build.gradle build
docker build -t microservicedemo/student Students/
if docker container ls -a | grep student
then
  echo " "
  echo "***************************************"
  echo "** removing existing student service **"
  echo "***************************************"
  echo " "
  docker container stop student
  docker container rm student
  sleep 1
fi
echo " "
echo "*******************************"
echo "** deploying student service **"
echo "*******************************"
echo " "
#docker run -p 1111:1111 -d --name student --link mongodb microservicedemo/student
echo " "
echo "******************************"
echo "** building teacher service **"
echo "******************************"
echo " "
gradle -b Teachers/build.gradle build
docker build -t microservicedemo/teacher Teachers/
if docker container ls -a | grep teacher
then
  echo " "
  echo "***************************************"
  echo "** removing existing teacher service **"
  echo "***************************************"
  echo " "
  docker container stop teacher
  docker container rm teacher
  sleep 1
fi
echo " "
echo "*******************************"
echo "** deploying teacher service **"
echo "*******************************"
echo " "
#docker run -p 2222:2222 -d --name teacher --link mongodb microservicedemo/teacher
echo " "
echo "***************************"
echo "** building zuul service **"
echo "***************************"
echo " "
gradle -b Zuul/build.gradle build
docker build -t microservicedemo/zuul Zuul/
if docker container ls -a | grep zuul
then
  echo " "
  echo "************************************"
  echo "** removing existing zuul service **"
  echo "************************************"
  echo " "
  docker container stop zuul
  docker container rm zuul
  sleep 1
fi
echo " "
echo "****************************"
echo "** deploying zuul service **"
echo "****************************"
echo " "
#docker run -p 8765:8765 -d --name zuul microservicedemo/zuul
until (docker images | grep zuul) && (docker images | grep teacher) && (docker images | grep student) && (docker images | grep eureka)
do
  sleep 1
  echo "waiting for images to be up..."
done
echo "images are up..."
docker-compose up -d
until docker container ls  | grep eureka | grep Up
do
  sleep 1
  echo "waiting for eureka container..."
done
sleep 1
docker run -p 1111:1111 -d --network demo-net --name student --link mongodb:mongodb -e "SPRING_PROFILES_ACTIVE=docker" microservicedemo/student
docker run -p 2222:2222 -d --network demo-net --name teacher --link mongodb:mongodb -e "SPRING_PROFILES_ACTIVE=docker" microservicedemo/teacher
sleep 1
echo " "
echo "**************************************************"
echo "**************************************************"
echo "** reaching port for eureka service:    " $(docker container ls | grep eureka | awk '{print $11}' | cut -d '-' -f 1 | cut -d ':' -f 2) " **"
echo "** reaching port for zuul service:      " $(docker container ls | grep zuul | awk '{print $11}' | cut -d '-' -f 1 | cut -d ':' -f 2) " **"
echo "** reaching port for teacher container: " $(docker container ls | grep teacher | awk '{print $11}' | cut -d '-' -f 1 | cut -d ':' -f 2) " **"
echo "** reaching port for student container: " $(docker container ls | grep student | awk '{print $11}' | cut -d '-' -f 1 | cut -d ':' -f 2) " **"
echo "**************************************************"
echo "**************************************************"
echo " "
