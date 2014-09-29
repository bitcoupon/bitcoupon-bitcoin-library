require 'test_helper'

class ApiTest < ActionDispatch::IntegrationTest
  setup do
    @coupons =  "{\"coupons\":" +
                  "[{\"title\":\"Dummy Coupon 1\"," +
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
                  "}]" +
                "}"
    @api = "http://localhost:3002/backend/coupons"
    @pubkey = "sdflkj3209ikldjf23kljsd"
  end

  test "should get correct response from api" do
    uri = URI.parse(@api)
    req = Net::HTTP::Get.new(uri)

    req.add_field "token", @pubkey

    result = Net::HTTP.start(uri.hostname, uri.port) do |http|
      http.request(req)
    end

    body = JSON.parse(result.body)
    result = body["coupons"]

    assert body["pubkey"].eql?(@pubkey)
    result = "{\"coupons\":#{result.to_json}}"
    assert result.eql?(@coupons)
  end
end
