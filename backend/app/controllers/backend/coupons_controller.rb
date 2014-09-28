module Backend
  class CouponsController < ApplicationController
    before_action :set_public_key, only: [:create, :index, :show]

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
      response.headers["token"] = "Your token: #{@public_key}"
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

      if @public_key.nil?
        render json: '{"error":"NO PUBLIC KEY PROVIDED"}'
      else
        render json: @coupons
      end
    end

    def show
      @public_key = request.headers["TOKEN"]
      #response.headers["TOKEN"] = "Your token: #{@public_key}"
      id = params[:id]
      @coupon = {
        title: "Dummy Coupon 1",
        description: "This is the dummy coupons\ndescription!",
        id: id,
        modified: "1311495190384",
        created:  "1311499999999",
      }

      render json: @coupon
    end

  private
    def set_public_key
      @public_key = request.headers["token"]
      if @public_key.nil?
        if params[:token]
          @public_key = params[:token]
        end
      end
    end

    def coupon_params
      params.require(:coupon).permit(:title, :description, :user_id)
    end
  end
end
