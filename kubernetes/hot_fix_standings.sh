#!/bin/bash

set -e

kubectl edit deployment/standings
kubectl rollout status deployment/standings