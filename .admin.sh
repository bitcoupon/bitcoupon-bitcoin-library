#!/bin/bash
cd admin
bundle install
echo -e "\n\nSTARTING BITCOUPON ADMIN\n\n"
rails server --port=3001
