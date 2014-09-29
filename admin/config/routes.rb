Rails.application.routes.draw do
  resources :users
  namespace :admin do
    get "coupons", to: "coupons#index"
    get "coupon/:id", to: "coupons#show", as: "coupon"
    get "new_coupon", to: "coupons#new", as: "new_coupon"
    post "coupons", to: "coupons#create"
  end

  root "users#index"
end
