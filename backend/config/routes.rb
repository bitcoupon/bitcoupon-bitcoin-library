Rails.application.routes.draw do
  namespace :backend do
    post "coupons", to: "coupons#create"
    get "coupon", to: "coupons#new"
    get "coupons", to: "coupons#index"
    get "coupon/:id", to: "coupons#show"
  end

  root "backend/coupons#index"
end
