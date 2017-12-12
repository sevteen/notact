#!/bin/bash

kubectl create -f activemq/service.yml
kubectl create -f standings/service.yml
standings_url=`minikube service standings --url | sed 's#/#\\\/#g'`
sed -i "s/value: http:\/\/192.168.99.100:.*$/value: $standings_url/g" operations/service.yml
kubectl create -f operations/service.yml
echo "Operations URL: `minikube service operations --url`"
