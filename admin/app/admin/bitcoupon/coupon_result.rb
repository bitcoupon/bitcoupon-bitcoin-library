module Admin
  module Bitcoupon
    class CouponResult
      attr_reader :coupons, :pubkey_json, :pubkey_header

      def initialize coupons, pubkey_json, pubkey_header
        @coupons = coupons
        @pubkey_json = pubkey_json
        @pubkey_header = pubkey_header
      end
    end
  end
end
