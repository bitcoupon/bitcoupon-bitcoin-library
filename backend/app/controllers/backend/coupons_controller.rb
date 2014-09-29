module Backend
  class CouponsController < ApplicationController
    before_action :set_public_key, only: [:create, :index, :show]
    skip_before_filter :verify_authenticity_token, :only => [:create, :destroy]

    def create
      #binding.pry
      coupon = Coupon.new(coupon_params)

      #binding.pry

      if coupon.save
        response.headers["id"] = coupon.id.to_s
        render json: coupon
      else
        render json: '{"error":"Invalid coupon"}'
      end
    end

    def destroy
      Coupon.find(params["id"]).destroy

      render json: { head: :no_content }
    end

    def new
      @coupon = Coupon.new
    end

    def index
      response.headers["token"] = "Your token: #{@public_key}"
      #binding.pry
      @coupons_all = Coupon.all
      @coupons = {
        pubkey: @public_key,
        coupons: @coupons_all
      }

      if @public_key.nil?
        render json: '{"error":"NO PUBLIC KEY PROVIDED"}', status: 401
      else
        render json: @coupons
      end
    end

    def show
      @public_key = request.headers["TOKEN"]
      #response.headers["TOKEN"] = "Your token: #{@public_key}"
      id = params[:id]
      @coupon = Coupon.find(id)
      %#@coupon = {
        title: "Dummy Coupon 1",
        description: "This is the dummy coupons\ndescription!",
        id: id,
        modified: "1311495190384",
        created:  "1311499999999",
      }
      %#
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
      #c = JSON.parse params["coupon"]
      #params["coupon"] = c
      params.require(:coupon).permit(:title, :description, :user_id)
    end
  end
end
