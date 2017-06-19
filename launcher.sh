echo "****************************************************"
echo "****************************************************"
echo "** running test as containers or swarm services ? **"
echo "** 1                                   containers **"
echo "** 2                               swarm services **"
echo "****************************************************"
echo "****************************************************"
read rep

if [ $rep = 2 ]
then
  if uname -s | grep MINGW
  then
    sh initSwarm.sh
    sh build.sh 2
    sh buildService.sh
  else
    echo "***********************************************************************************************************"
    echo "** the following process you are attempting to run needs an existing swarm, did you initialize a swarm ? **"
	echo "**                                                                                           1       yes **"
	echo "**                                                                                           2        no **"
	echo "***********************************************************************************************************"
	read ans
	if docker service ls | grep registry
	then
	  echo "there is already a registry running"
	else
	  sh initSwarm.sh
	fi
	if [ $ans = 1 ]
	then
	  sh build.sh 2
	  sh buildService.sh
	else
	  echo "please initialize a swarm"
	fi
  fi
elif [ $rep = 1 ]
then
  sh build.sh 1
  sh buildCont.sh
else
  echo " "
  echo "****************************************************"
  echo "** your answer did not match with proposed choice **"
  echo "****************************************************"
  echo " "
fi
