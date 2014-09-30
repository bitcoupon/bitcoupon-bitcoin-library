require_dependency 'bitcoupon/api/backend_request'

module Admin
  class CouponsController < ApplicationController
    def index
      request = Admin::Bitcoupon::Api::BackendRequest.new "/coupons"
      @result = request.start
    end

    def show
      id = params[:id]
      api = "http://localhost:3002/backend"
      result = Net::HTTP.get(URI.parse(api + "/coupon/#{id}"))
      @coupon = JSON.parse(result)
    end

    def new
      @coupon = Coupon.new
    end

    def create
      api = "http://localhost:3002/backend"
      uri = URI.parse(api + '/coupons')

      request = Net::HTTP::Post.new(uri.path)
      #binding.pry
      request.content_type = "application/json"
      request.body = {coupon: coupon_params}.to_json

      request.add_field "token", "lulz"

      result = Net::HTTP.start(uri.hostname, uri.port) {|http|
        http.request(request)
      }

      token = result.header["token"]
      #binding.pry
      id = result.header["id"].to_i
      redirect_to admin_coupon_path(id)
    end

    def destroy
      api = "http://localhost:3002/backend"
      uri = URI.parse(api + '/coupon' + '/' + params["id"])

      request = Net::HTTP::Delete.new(uri.path)

      request.add_field "token", "lulz"

      result = Net::HTTP.start(uri.hostname, uri.port) {|http|
        http.request(request)
      }

      token = result.header["token"]
      #binding.pry
      redirect_to admin_coupons_path
    end

    private
      def coupon_params
        #binding.pry
        #params[:coupon] = JSON.parse params[:coupon]
        params.require(:coupon).permit(:title, :description)
      end
  end
end
