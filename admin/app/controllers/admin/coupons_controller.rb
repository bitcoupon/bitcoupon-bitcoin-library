require_dependency 'bitcoupon/api/backend_request'
require 'shellwords'

module Admin
  class CouponsController < ApplicationController
    def index
      request = Admin::Bitcoupon::Api::BackendRequest.new "/coupons"
      @result = request.start
      
      api = "http://localhost:3002/backend"
      path = "/transaction_history"
      uri = URI.parse(api + path)
      request = Net::HTTP::Get.new(uri.path)

      result = Net::HTTP.start(uri.hostname, uri.port) {|http|
        http.request(request)
      }
      token = result.header["token"]

      transactions = JSON.parse(result.body)

      #Name: getCreatorPublicKeys - Argumentss: String privateKey, String transactionHistoryJson

      private_key = "5JAy2V6vCJLQnD8rdvB2pF8S6bFZuhEzQ43D95k6wjdVQ4ipMYu"
      transaction_history_json = transactions.to_s

      #binding.pry
      transaction_history = Shellwords.escape transaction_history_json

      command = "java -jar ../bitcoin/bitcoin-1.0.jar"
      method = "getCreatorPublicKeys"

      output = %x{ #{command} #{method} #{private_key} #{transaction_history} }

      #binding.pry
      if output.blank?
        render text: "Something went wrong" and return
      end
      @transactions = output.split("\n")
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
