Rails.application.routes.draw do
  resources :users
  namespace :admin do
    get "coupons", to: "coupons#index"
    get "coupon/:id", to: "coupons#show", as: "coupon"
    get "new_coupon", to: "coupons#new", as: "new_coupon"
    post "coupons", to: "coupons#create"
    delete "coupon/:id", to: "coupons#destroy"
  end

  get "generate_creation_transaction", to: "transactions#generate_creation"

  root "admin/coupons#index"
end
