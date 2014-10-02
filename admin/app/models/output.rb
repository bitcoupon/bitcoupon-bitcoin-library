class Output
  include ActiveModel::Validations
  include ActiveModel::Conversion
  extend ActiveModel::Naming
  
  attr_accessor :id, :transaction_id, :creator_address, :amount, :address, :input
  
  # Rails has method named transaction, can't use that again here.
  #belongs_to :bitcoin_transaction, foreign_key: "transaction_id", class_name: "Transaction"

  validates_presence_of :title
  #validates_format_of :email, :with => /^[-a-z0-9_+\.]+\@([-a-z0-9]+\.)+[a-z0-9]{2,4}$/i
  #validates_length_of :content, :maximum => 500
  
  def initialize(attributes = {})
    attributes.each do |name, value|
      send("#{name}=", value)
    end
  end
  
  def persisted?
    false
  end
end
