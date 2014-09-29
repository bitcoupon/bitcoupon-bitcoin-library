require 'test_helper'
require 'minitest/mock'

class Admin::CouponsControllerTest < ActionController::TestCase
  setup do
    @coupons =  "[{\"title\":\"Dummy Coupon 1\"," +
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
                "}]"
  end

  test "should get index" do
    uri = URI.parse("http://localhost:3002/backend/coupons")
    request = Net::HTTP::Get.new(uri.path)
    request.add_field "token", "sdgkj32pidklj23lkjd"

    @http = Minitest::Mock.new
    @http.expect :request, @coupons, [ Net::HTTP::Get ]

    Net::HTTP.stub :start, @coupons, @http do
      get :index
      assert_response :success
      assert_not_nil assigns(:result)
    end
  end
end
