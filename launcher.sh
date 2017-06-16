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
  sh initSwarm.sh
  sh build.sh 2
  sh buildService.sh
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
