class CreateOutputs < ActiveRecord::Migration
  def change
    create_table :outputs do |t|
      #id implicit
      t.references :transaction
      t.string :coupon_type
      t.integer :amount
      t.string :address
      t.references :input

      t.timestamps
    end
  end
end
