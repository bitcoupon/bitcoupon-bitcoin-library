Rails.application.routes.draw do
  resources :users
  get "api_consume", to: "coupons#consume"

  root "users#index"
end
