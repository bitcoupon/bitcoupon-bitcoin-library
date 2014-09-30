class CreateInputs < ActiveRecord::Migration
  def change
    create_table :inputs do |t|
      #id implicit
      t.references :outputs
      t.string :signature

      t.timestamps
    end
  end
end
