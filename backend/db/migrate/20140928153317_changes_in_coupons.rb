class ChangesInCoupons < ActiveRecord::Migration
  def change
    rename_column :coupons, :name, :title
    add_column :coupons, :description, :text
    add_column :coupons, :user_id, :integer
  end
end
