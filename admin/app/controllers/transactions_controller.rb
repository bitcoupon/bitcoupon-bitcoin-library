require 'shellwords'

class TransactionsController < ApplicationController
  before_filter :set_stuff

  def generate_creation
    @output = `#{@command} #{@method_name} #{@private_key}`
    if @output.blank?
      render text: "Something went wrong" and return
    end

    @id = verify_transaction @output

    @parsed_json = JSON.parse(@json)
    @parsed_output = JSON.parse(@output)

    creation_json = @parsed_output["creations"]
    input_json = @parsed_output["inputs"]
    output_json = @parsed_output["outputs"]
    %#
{"transactionId"=>0,
 "creations"=>
  [{"creationId"=>0,
    "creatorAddress"=>"1LfXmYtDHyCM8fHMhrMX2EtfECGjzmw3BW",
    "amount"=>1,
    "signature"=>
     "EsykzF7P9LpLSFNiR6PTBJH5LrxKQLAn6bSnKwXUVgTiMQE9kGfvd3UQuYvxhKR6uFvRDBFqsRbkWzRS3Ki9AR4mMNYDLumNN1ZXn 3agqkP4xN8q573oKCJTNz3nb1y4euvuAP2qeuaLRhpvGCRoJ3godvpEDKqAEGP6GyeYLw9hRtgczD9NPv5drmE7Q5DF8dd"}],
 "inputs"=>[],
 "outputs"=>
  [{"outputId"=>0,
    "creatorAddress"=>"1LfXmYtDHyCM8fHMhrMX2EtfECGjzmw3BW",
    "amount"=>1,
    "address"=>"1LfXmYtDHyCM8fHMhrMX2EtfECGjzmw3BW",
    "inputId"=>0}]}
%#

    transaction = Transaction.new
    creation = Creation.new
    input = Input.new
    output = Output.new

    unless creation_json.blank?
      # Only one creation for now
      creation_json = creation_json.first
      # TODO Make loop

      creation.creator_address = creation_json["creatorAddress"]
      creation.amount = creation_json["amount"].to_i
      creation.signature = creation_json["signature"]
      #creation.save
    end

    unless input_json.blank?
      # TODO senere
      #input.save
    end

    unless output_json.blank?
      # Only one creation for now
      output_json = output_json.first
      # TODO Make loop

      output.creator_address = output_json["creatorAddress"]
      output.amount = output_json["amount"].to_i
      output.address = output_json["address"]
      # TODO Add input når relevant
      #output.save
    end

    transaction.creation = creation
    transaction.input = input
    transaction.output = output
    @transaction = transaction
    #render json: "#{@output}\n\n\n#{@json}"
  end

  def generate_send
    #params

    #binding.pry

    private_key = "5JAy2V6vCJLQnD8rdvB2pF8S6bFZuhEzQ43D95k6wjdVQ4ipMYu"
    creator_public_key = params["public_key"] # "138u97o2Sv5qUmucSasmeNf5CAb3B1CmD6"

    #binding.pry
    transaction_history = Shellwords.escape get_transaction_history.to_s

    receiver_address = params["receiver_address"] # 1Kau4L6BM1h6QzLYubq1qWrQSjWdZFQgMb

    command = "java -jar ../bitcoin/bitcoin-1.0.jar"
    method = "generateSendTransaction"

    output = %x{ #{command} #{method} #{private_key} #{creator_public_key} #{transaction_history} #{receiver_address} }

    if output.blank?
      render text: "Something went wrong" and return
    end

    id = verify_transaction output

    # Name: generateSendTransaction - Argumentss: String privateKey, String creatorPublicKey
    #                             , String transactionHistoryJson, String receiverAddress
    #binding.pry
    redirect_to root_path
  end

  private

  def set_stuff
    @json = '{"transactionId":0,"creations":[{"creationId":0,"creatorAddress":"1LfXmYtDHyCM8fHMhrMX2EtfECGjzmw3BW","amount":1,"signature":"EsykzEva7gBWpyZbpBXvyWiZb3Dwta18T6uEg4E39jdpkh3ouaNhaGyfg3rVfij4bY38pnuyedT6Ab63wyBzY2z6WUUy4P5v1QqDx 3agqkP4xN8q573oKCJTNz3nb1y4euvuAP2qeuaLRhpvGCRoJ3godvpEDKqAEGP6GyeYLw9hRtgczD9NPv5drmE7Q5DF8dd"}],"inputs":[],"outputs":[{"outputId":0,"creatorAddress":"1LfXmYtDHyCM8fHMhrMX2EtfECGjzmw3BW","amount":1,"address":"1LfXmYtDHyCM8fHMhrMX2EtfECGjzmw3BW","inputId":0}]}'
    @command = "java -jar ../bitcoin/bitcoin-1.0.jar"

    @method_name = 'generateCreationTransaction'
    @private_key = '5JAy2V6vCJLQnD8rdvB2pF8S6bFZuhEzQ43D95k6wjdVQ4ipMYu'
  end

  def get_transaction_history
    api = "http://localhost:3002/backend"
    path = "/transaction_history"
    uri = URI.parse(api + path)
    request = Net::HTTP::Get.new(uri.path)

    result = Net::HTTP.start(uri.hostname, uri.port) {|http|
      http.request(request)
    }

    token = result.header["token"]
    @transactions = JSON.parse(result.body)
  end

  def verify_transaction output
    api = "http://localhost:3002/backend"
    uri = URI.parse(api + '/verify_transaction')

    request = Net::HTTP::Post.new(uri.path)
    #binding.pry
    request.content_type = "application/json"
    request.body = {transaction: output}.to_json

    request.add_field "token", "lulz"

    result = Net::HTTP.start(uri.hostname, uri.port) {|http|
      http.request(request)
    }

    token = result.header["token"]
    #binding.pry
    id = result.header["id"].to_i
    id
  end
end
