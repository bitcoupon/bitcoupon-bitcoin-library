class Creation < ActiveRecord::Base
  belongs_to :bitcoin_transaction, foreign_key: "transaction_id", class_name: "Transaction"
  has_one :owner
end
