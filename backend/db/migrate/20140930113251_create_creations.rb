class CreateCreations < ActiveRecord::Migration
  def change
    create_table :creations do |t|
      #id implicit
      t.references :transactions # transaction_id
      t.string :creator_public_key
      t.string :sub_type
      t.integer :amount, default: 0
      t.string :signature

      t.timestamps
    end
  end
end
