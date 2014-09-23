Rails.application.routes.draw do
  resources :users
  namespace :admin do
    get "coupons", to: "coupons#index"
    get "coupon/:id", to: "coupons#show"
  end

  root "users#index"
end
