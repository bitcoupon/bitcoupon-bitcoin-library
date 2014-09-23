class CouponsController < ApplicationController
  def consume
    result = Net::HTTP.get(URI.parse('http://localhost:3002/backend/coupons.json'))
    @coupons = JSON.parse(result)
  end
end