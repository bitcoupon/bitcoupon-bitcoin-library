class Input < ActiveRecord::Base
  # Rails has method named transaction, can't use that again here.
  belongs_to :bitcoin_transaction, foreign_key: "transaction_id", class_name: "Transaction"
end
