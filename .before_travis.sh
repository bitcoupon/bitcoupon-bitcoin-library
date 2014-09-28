#!/bin/bash
if [ "$ADMIN" == true ]; then
  cd backend
  bundle exec bin/rails s -p 3002 &
  pid=$!
  export RAILSPID=$pid
  echo "pid - $pid"
  echo "rails pid - $RAILSPID"
  cd ..
  sleep 5
  echo "Started rails server for backend";
else
  echo "Did not start backend server";
fi

if [ "$ADMIN" == true ]; then
  cd admin
  echo "cd into admin";
else
  echo "not cd into admin";
fi

if [ "$BACKEND" == true ]; then
  cd backend
  echo "cd into backend";
else
  echo "not cd into backend";
fi
