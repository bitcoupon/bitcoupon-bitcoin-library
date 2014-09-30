#!/bin/bash
cd admin
bundle install
rake db:migrate
echo -e "\n\nSTARTING BITCOUPON ADMIN\n\n"
rails server --port=3001
