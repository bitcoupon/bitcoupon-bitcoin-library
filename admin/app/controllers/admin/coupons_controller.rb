require_dependency 'bitcoupon/api/backend_request'

module Admin
  class CouponsController < ApplicationController
    def index
      request = Admin::Bitcoupon::Api::BackendRequest.new "/coupons"
      @result = request.start
    end

    def show
      id = params[:id]
      api = "http://bitcoupon.no-ip.org:3002/backend"
      result = Net::HTTP.get(URI.parse(api + "/coupon/#{id}"))
      @coupon = JSON.parse(result)
    end
  end
end
