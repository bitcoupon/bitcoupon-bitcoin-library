#!/bin/bash
cd backend
bundle install
rake db:migrate
echo -e "\n\nSTARTING BITCOUPON BACKEND\n\n"
rails server --port=3002
