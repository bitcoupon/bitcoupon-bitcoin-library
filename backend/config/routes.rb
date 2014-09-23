Rails.application.routes.draw do
  namespace :backend do
    post "/new_coupons", to: "coupons#create"
  end

  root "admin/coupons#index"
end
