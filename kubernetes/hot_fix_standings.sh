#!/bin/bash

kubectl edit deployment/standings
kubectl rollout status deployment/standings