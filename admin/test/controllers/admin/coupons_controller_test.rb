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
    Net::HTTP.stub :get, @coupons do
      get :index
      assert_response :success
      assert_not_nil assigns(:coupons)
    end
  end
end
