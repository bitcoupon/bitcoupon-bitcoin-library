class RenameColumnInOutputs < ActiveRecord::Migration
  def change
    rename_column :outputs, :coupon_type, :creator_address
  end
end
