Rails.application.routes.draw do
  namespace :backend do
    post "/new_coupons", to: "coupons#create"
    get "coupons", to: "coupons#index"
  end

  root "backend/coupons#index"
end
