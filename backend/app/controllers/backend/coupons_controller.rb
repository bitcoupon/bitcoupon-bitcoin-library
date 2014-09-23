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
      #token = request.headers["Token"]
      #binding.pry
      @public_key = params[:pubkey]
      @coupons = {
        pubkey: @public_key,
        coupons:
          [
            {
              title: "Dummy Coupon 1",
              description: "This is the dummy coupons\ndescription!",
              id: "2",
              modified: "1311495190384",
              created:  "1311499999999",
            },
            {
              title: "Dummy Coupon 2",
              description: "This is the dummy coupons\ndescription 2!",
              id: "3",
              modified: "1311495190384",
              created:  "1311999999999",
            },
          ]
      }

      #10.times do |i|
      #  coupon = @coupons[:coupons][0].clone
      #  #binding.pry
      #  coupon[:id] = (coupon[:id].to_i + 10 + i).to_s
      #  @coupons[:coupons] << coupon
      #end

      #binding.pry

      render json: @coupons
    end

    def show
      @public_key = params[:pubkey]
      id = params[:id]
      @coupon = {
        pubkey: @public_key,
        title: "Dummy Coupon 1",
        description: "This is the dummy coupons\ndescription!",
        id: id,
        modified: "1311495190384",
        created:  "1311499999999",
      }

      render json: @coupon
    end
  end
end