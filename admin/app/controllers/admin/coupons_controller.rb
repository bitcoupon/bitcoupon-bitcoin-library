module Admin
  class CouponsController < ApplicationController
    def index
      result = Net::HTTP.get(URI.parse(api + '/coupons.json'))
      @coupons = JSON.parse(result)
    end

    def show
      id = params[:id]
      result = Net::HTTP.get(URI.parse(api + "/coupon/#{id}.json"))
      @coupon = JSON.parse(result)
    end
  end
end
