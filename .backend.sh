#!/bin/bash
cd backend
bundle install
echo -e "\n\nSTARTING BITCOUPON BACKEND\n\n"
rails server --port=3002
