module Admin
  class CouponsController < ApplicationController
    def index
      uri = URI.parse(api + "/coupons")
      req = Net::HTTP::Get.new(uri)

      req.add_field "token", pubkey

      result = Net::HTTP.start(uri.hostname, uri.port) {|http|
        http.request(req)
      }

      token = result.header["token"]

      body = JSON.parse(result.body)
      @coupons = body["coupons"]
      @pubkey_result  = body["pubkey"]
      @pubkey_response = token
    end

    def show
      id = params[:id]
      result = Net::HTTP.get(URI.parse(api + "/coupon/#{id}"))
      @coupon = JSON.parse(result)
    end
  end
end
