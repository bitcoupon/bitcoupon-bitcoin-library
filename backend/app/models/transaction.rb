class Transaction < ActiveRecord::Base
  has_many :creations
  has_many :inputs
  has_many :outputs
end
