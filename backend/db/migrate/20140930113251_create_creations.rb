class CreateCreations < ActiveRecord::Migration
  def change
    create_table :creations do |t|
      #id implicit
      t.references :transaction
      t.string :creator_address
      t.integer :amount, default: 0
      t.string :signature

      t.timestamps
    end
  end
end
