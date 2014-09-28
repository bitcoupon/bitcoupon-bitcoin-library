#!/bin/bash
if [ "$ADMIN" == true ]; then
  echo "pid - $pid"
  echo "rails pid - $RAILSPID"
  kill -s INT $RAILSPID
else
  echo "Not killing pid"
fi
