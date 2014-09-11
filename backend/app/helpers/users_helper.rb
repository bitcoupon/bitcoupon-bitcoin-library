module UsersHelper
  def user_password_message user
    if user.password?
      "Password set"
    else
      "Password empty"
    end
  end
end
