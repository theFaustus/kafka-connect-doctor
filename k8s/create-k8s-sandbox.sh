#!/bin/sh

echo "📦 Initializing Kubernetes cluster..."

minikube start --cpus 2 --memory 4g --driver docker --profile k8s-sandbox

echo "🔌 Enabling NGINX Ingress Controller..."

minikube addons enable ingress --profile k8s-sandbox

sleep 15

echo "📦 Deploying platform services..."

kubectl apply -f services

sleep 5

echo "⌛ Waiting for PostgreSQL to be deployed..."

while [ $(kubectl get pod -l db=k8s-sandbox-postgres | wc -l) -eq 0 ] ; do
  sleep 5
done

echo "⌛ Waiting for PostgreSQL to be ready..."

sleep 5

echo "⌛ Waiting for Kafka to be deployed..."

while [ $(kubectl get pod -l broker=k8s-sandbox-kafka | wc -l) -eq 0 ] ; do
  sleep 5
done

echo "⌛ Waiting for Kafka to be ready..."

kubectl wait \
  --for=condition=ready pod \
  --selector=broker=k8s-sandbox-kafka \
  --timeout=180s

echo "⛵ Happy Sailing!"

sleep 10
