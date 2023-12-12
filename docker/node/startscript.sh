#!/bin/bash
set -e

echo "Printing environment variables"
echo $NODE_ID
echo "Printing all environment variables"

printenv

java -jar /usr/bin/bigo-raft.jar