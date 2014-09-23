module Admin
  class CouponsController < ApplicationController
    def index
      uri = URI.parse(api + "/coupons/#{pubkey}")
      req = Net::HTTP::Get.new(uri)

      #req.headers["Token"] = pubkey

      result = Net::HTTP.start(uri.hostname, uri.port) {|http|
        http.request(req)
      }

      @coupons = JSON.parse(result)
    end

    def show
      id = params[:id]
      result = Net::HTTP.get(URI.parse(api + "/coupon/#{pubkey}/#{id}"))
      @coupon = JSON.parse(result)
    end
  end
end
