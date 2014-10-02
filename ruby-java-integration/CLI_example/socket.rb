#!/usr/bin/env ruby

def json id
  "\"{\"id\":#{id.to_s}, \"name\":\"coupon\"}\""
end

i = 0
2.times do
  send = json(i += 1)
  puts "Ruby sending: " + send
  received = %x( java Json #{send})
  puts "Ruby received: " + received
end
