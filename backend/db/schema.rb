# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20140930113305) do

  create_table "coupons", force: true do |t|
    t.string   "title"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.text     "description"
    t.integer  "user_id"
  end

  create_table "creations", force: true do |t|
    t.integer  "transactions_id"
    t.string   "creator_public_key"
    t.string   "sub_type"
    t.integer  "amount",             default: 0
    t.string   "signature"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "inputs", force: true do |t|
    t.integer  "outputs_id"
    t.string   "signature"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "outputs", force: true do |t|
    t.string   "coupon_type"
    t.integer  "amount"
    t.string   "address"
    t.integer  "inputs_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "transactions", force: true do |t|
    t.datetime "created_at"
    t.datetime "updated_at"
  end

end
