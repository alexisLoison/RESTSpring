if docker container ls -a | grep ago
then
  docker container stop $(docker container ls | grep zuul | awk '{print $1}')
  docker container rm $(docker container ls -a | grep zuul | awk '{print $1}')

  docker container stop $(docker container ls | grep student | awk '{print $1}')
  docker container rm $(docker container ls -a | grep student | awk '{print $1}')

  docker container stop $(docker container ls | grep teacher | awk '{print $1}')
  docker container rm $(docker container ls -a | grep teacher | awk '{print $1}')

  docker container stop $(docker container ls | grep eureka | awk '{print $1}')
  docker container rm $(docker container ls -a | grep eureka | awk '{print $1}')
  
  docker container stop $(docker container ls | grep rabbit | awk '{print $1}')
  docker container rm $(docker container ls -a | grep rabbit | awk '{print $1}')
else
  docker service rm zuul
  docker service rm eureka
  docker service rm teacher
  docker service rm student
fi
