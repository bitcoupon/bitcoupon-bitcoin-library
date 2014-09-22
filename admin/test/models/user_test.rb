require 'test_helper'

class UserTest < ActiveSupport::TestCase
  test "should not save user with short password" do
    user = users(:short_password)
    user.password = "short"

    refute user.save, "saved user with short password"
    assert user.errors.messages[:password].first
               .eql?("is too short (minimum is 8 characters)"), "wrong error"
  end
end
