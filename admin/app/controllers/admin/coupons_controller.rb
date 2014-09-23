module Admin
  class CouponsController < ApplicationController
    def index
      result = Net::HTTP.get(URI.parse(api + '/coupons'))
      @coupons = JSON.parse(result)
    end

    def show
      id = params[:id]
      result = Net::HTTP.get(URI.parse(api + "/coupon/#{id}"))
      @coupon = JSON.parse(result)
    end
  end
end
