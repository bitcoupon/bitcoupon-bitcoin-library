class ApplicationController < ActionController::Base
  # Prevent CSRF attacks by raising an exception.
  # For APIs, you may want to use :null_session instead.
  protect_from_forgery with: :exception

  def api
    "http://localhost:3002/backend"
  end

  def pubkey
    "sdgkj32pidklj23lkjd"
  end
end
