require 'test_helper'

class CouponTest < ActiveSupport::TestCase
  test "coupon should have name" do
    coupon = coupons(:one)
    assert coupon.name.length > 0
  end
end
