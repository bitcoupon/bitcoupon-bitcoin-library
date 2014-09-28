#!/usr/bin/env ruby
require 'rubygems'
require 'socket'

i = 0
server = TCPServer.new 2000

def json id
  '{"id":' + id.to_s + ', "name":"coupon"}'
end

loop do
  Thread.start(server.accept) do |client|
      client.puts json(i += 1)
      puts "Server: #{client.gets}"

      client.close
  end
end
