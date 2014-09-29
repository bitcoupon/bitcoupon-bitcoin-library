require_dependency 'bitcoupon/coupon_result'

module Admin
  module Bitcoupon
    module Api
      class BackendRequest
        attr_accessor :path, :api, :pubkey

        def initialize path
          @path = path
          @api = if Rails.env.test?
            "http://localhost:8080/backend"
          else
            "http://bitcoupon.noip.me:8080/backend"
          end
          @pubkey = "sdgkj32pidklj23lkjd"
        end

        def start
          uri = URI.parse(api + path)
          request = Net::HTTP::Get.new(uri.path)

          request.add_field "token", pubkey

          result = Net::HTTP.start(uri.hostname, uri.port) {|http|
            http.request(request)
          }

          if Rails.env.test?
            return Admin::Bitcoupon::CouponResult.new JSON.parse("[{\"title\":\"Dummy Coupon 1\"," +
                  "\"description\":\"This is the dummy coupons\\ndescription!\"," +
                  "\"id\":\"2\"," +
                  "\"modified\":\"1311495190384\"," +
                  "\"created\":\"1311499999999\"" +
                "}" +
                ",{\"title\":\"Dummy Coupon 2\"," +
                  "\"description\":\"This is the dummy coupons\\ndescription 2!\"," +
                  "\"id\":\"3\"," +
                  "\"modified\":\"1311495190384\"," +
                  "\"created\":\"1311999999999\"" +
                "}]"), "lol", "lol"
          end

          token = result.header["token"]
          body = JSON.parse(result.body)

          Admin::Bitcoupon::CouponResult.new body["coupons"], body["pubkey"], token
        end
      end
    end
  end
end
