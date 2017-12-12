#!/bin/bash

kubectl scale --replicas=3 deployment/standings
kubectl rollout status deployment/standings