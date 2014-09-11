## Setup

#### Download rvm (Ruby Version Manager).
    $ '\curl -sSL https://get.rvm.io | bash -s stable'

#### Install latest version of ruby
    'rvm install ruby-2.1.2'
    'rvm use 2.1.2 --default'

#### Install Ruby on Rails
    'gem install rails'

    'cd #{project_path}/bitcoin_coupon/backend'
    'bundle install'

    'rake db:setup'
    'rake db:migrate'
    'rake db:seed'
    => Do not use seeds in production :)

#### Run tests

    'rake test'
