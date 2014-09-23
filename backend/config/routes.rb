Rails.application.routes.draw do
  namespace :backend do
    post "/new_coupons", to: "coupons#create"
    get "coupons/:pubkey", to: "coupons#index"
    get "coupon/:pubkey/:id", to: "coupons#show"
  end

  root "backend/coupons#index"
end
