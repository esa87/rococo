#!/bin/bash
source ./docker.properties
export PROFILE=docker
export PREFIX="${IMAGE_PREFIX}"
export FRONT_VERSION="2.1.0"
export ARCH=$(uname -m)

echo '### Java version ###'
java --version

export FRONT="rococo-client"

docker compose down

docker_containers=$(docker ps -a -q)
docker_images=$(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'rococo')

if [ ! -z "$docker_containers" ]; then
  echo "### Stop containers: $docker_containers ###"
  docker stop $docker_containers
  docker rm $docker_containers
fi

if [ ! -z "$docker_images" ]; then
  echo "### Remove images: $docker_images ###"
  docker rmi $docker_images
fi

bash ./gradlew clean
if [ "$1" = "push" ] || [ "$2" = "push" ]; then
  echo "### Build & push images ###"
  bash ./gradlew jib -x :rococo-e2e:test
  docker compose push frontend.rococo.dc
else
  echo "### Build images ###"
  bash ./gradlew jibDockerBuild -x :rococo-e2e:test
fi

docker compose up --pull never -d
docker ps -a
