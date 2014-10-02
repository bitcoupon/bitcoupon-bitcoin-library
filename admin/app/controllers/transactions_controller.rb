class TransactionsController < ApplicationController
  before_filter :set_stuff

  def generate_creation
    #binding.pry
    @output = `#{@command} #{@method_name} #{@private_key}`
    #binding.pry

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

    transaction = Transaction.create
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
      creation.save
    end

    unless inputs_json.blank?
      # TODO senere
      input.save
    end

    unless output_json.blank?
      # Only one creation for now
      output_json = output_json.first
      # TODO Make loop

      output.creator_address = output_json["creatorAddress"]
      output.amount = output_json["amount"].to_i
      output.address = output_json["address"]
      # TODO Add input når relevant
      output.save
    end

    #render json: "#{@output}\n\n\n#{@json}"
  end

  private
    def set_stuff
      @json = '{"transactionId":0,"creations":[{"creationId":0,"creatorAddress":"1LfXmYtDHyCM8fHMhrMX2EtfECGjzmw3BW","amount":1,"signature":"EsykzEva7gBWpyZbpBXvyWiZb3Dwta18T6uEg4E39jdpkh3ouaNhaGyfg3rVfij4bY38pnuyedT6Ab63wyBzY2z6WUUy4P5v1QqDx 3agqkP4xN8q573oKCJTNz3nb1y4euvuAP2qeuaLRhpvGCRoJ3godvpEDKqAEGP6GyeYLw9hRtgczD9NPv5drmE7Q5DF8dd"}],"inputs":[],"outputs":[{"outputId":0,"creatorAddress":"1LfXmYtDHyCM8fHMhrMX2EtfECGjzmw3BW","amount":1,"address":"1LfXmYtDHyCM8fHMhrMX2EtfECGjzmw3BW","inputId":0}]}'
      @command = "java -jar ../bitcoin/bitcoin.jar"

      @method_name = 'generateCreationTransaction'
      @private_key = '5Kf9gd8faKhhq9jZTsNhq2MtViHA1dWdhRg9k4ovszTKz5DCeBT'
    end
end
