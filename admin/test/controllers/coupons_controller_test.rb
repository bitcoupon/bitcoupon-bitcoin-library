require 'test_helper'
require 'minitest/mock'

class CouponsControllerTest < ActionController::TestCase
  setup do
    @coupons = "[\"sdlkfj\",\"lsdfj\",\"lkjsdf\"]"
    @api = "http://localhost:3000/backend/coupons.json"
  end

  test "should consume api" do
    Net::HTTP.stub :get, @coupons do
      get :consume
      assert_response :success
      assert_not_nil assigns(:coupons)
    end
  end
end
