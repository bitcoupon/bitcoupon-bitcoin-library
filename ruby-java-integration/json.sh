#!/bin/sh

./socket.rb &
pid=$!
javac Json.java
for i in `seq 1 $1`
do
  java Json
done
rm Json.class
kill $pid
