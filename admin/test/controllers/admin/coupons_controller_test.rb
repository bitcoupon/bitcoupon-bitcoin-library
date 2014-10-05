require 'test_helper'
require 'minitest/mock'

class Admin::CouponsControllerTest < ActionController::TestCase
  test "should get index" do
    skip("Not working")
    uri = URI.parse("http://localhost:3002/backend/coupons")
    request = Net::HTTP::Get.new(uri.path)
    request.add_field "token", "sdgkj32pidklj23lkjd"

    @http = Minitest::Mock.new
    result = Minitest::Mock.new
    result.expect(:header, "anything")
    @http.expect :request, result, [ Net::HTTP::Get ]
    @http.expect :request, result, [ Net::HTTP::Get ]

    Net::HTTP.stub :start, @coupons, @http do
      get :index
      assert_response :success
      assert_not_nil assigns(:result)
    end
  end
end
