class CreateTransactions < ActiveRecord::Migration
  def change
    create_table :transactions do |t|
      # id implicit
      t.timestamps
    end
  end
end
