module Admin
  class CouponsController < ApplicationController
    def create
      coupons = [
        "12345678",
        "87654321",
      ]

      respond_to do |format|
        format.json { coupons.to_json }
      end
    end

    def index
      @coupons = [
        "sdlkfj",
        "lsdfj",
        "lkjsdf",
      ]
    end
  end
end