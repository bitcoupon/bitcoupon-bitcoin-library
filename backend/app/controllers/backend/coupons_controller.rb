module Backend
  class CouponsController < ApplicationController
    def create
      coupons = [
        "12345678",
        "87654321",
      ]

      respond_to do |format|
        format.json { render json: coupons }
      end
    end

    def index
      @coupons = [
        "sdlkfj",
        "lsdfj",
        "lkjsdf",
      ]

      respond_to do |format|
        format.html
        format.json { render json: @coupons }
      end
    end
  end
end