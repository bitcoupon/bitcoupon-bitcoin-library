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
  end

  test "should get correct response from api" do
    result = Net::HTTP.get(URI.parse(@api))
    #binding.pry
    assert result.eql?(@coupons)
  end
end