Rails.application.routes.draw do
  namespace :backend do
    post "coupons", to: "coupons#create"
    get "coupon", to: "coupons#new"
    get "coupons", to: "coupons#index"
    get "coupon/:id", to: "coupons#show"
    delete "coupon/:id", to: "coupons#destroy"

    post "verify_transaction", to: "transactions#verify"
    get "transaction_history", to: "transactions#history"
    get "creator_public_key", to: "transactions#creator_public_key"
  end

  root "backend/coupons#index"
end
