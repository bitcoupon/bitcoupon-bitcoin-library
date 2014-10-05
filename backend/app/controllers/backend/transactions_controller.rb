require 'shellwords'
require 'open3'

module Backend
  class TransactionsController < ApplicationController
    skip_before_filter :verify_authenticity_token, :only => [:verify]

    def verify
      #{"creations"=>nil, "inputs"=>[{"signature"=>"5fRnXJUWBE3TtiWjDNjD2se3QXrX9D9yxKoxSbagUWwcjMFWAWa39iHPKZD2t3dfbaGSk4yt1JqVAcd37t55QPXpC3bNb4dU8toEFPUE 3Q4aB3XMvzGpj27JNk31cWaTVMp82YokPdXYZeYu3YdxzJU4aisesNHUuT2waDCpjUPwko7ruEQQ75zcRHHyEQ8PhcVFWm", "outputId"=>7, "inputId"=>0}], "outputs"=>[{"address"=>"1Kau4L6BM1h6QzLYubq1qWrQSjWdZFQgMb", "creatorAddress"=>"138u97o2Sv5qUmucSasmeNf5CAb3B1CmD6", "outputId"=>0, "inputId"=>0, "amount"=>1}], "transactionId"=>0, "transaction"=>{}}
      transaction = params[:transaction]

      result = verify_transaction transaction, transaction_history

      if save_transaction transaction
        response.headers["id"] = @transaction.id.to_s
        render json: @transaction
      else
        render json: '{"error":"TRANSACTION NOT SAVED"}', status: 401
      end
    end

    def history
      render json: transaction_history
    end

    def creator_public_key
      command = "java -jar ../bitcoin/bitcoin-1.0.jar"
      method = "getCreatorAddresses"
      arg_one = "5JAy2V6vCJLQnD8rdvB2pF8S6bFZuhEzQ43D95k6wjdVQ4ipMYu"
      arg_two = Shellwords.escape transaction_history
      #output = %x{ #{command} #{method} #{arg_one} #{arg_two} }
      output = ""
      errors = ""


      cmd = "#{command} #{method} #{arg_one} #{arg_two}"
      Open3.popen3(cmd) {|stdin, stdout, stderr, wait_thr|
        pid = wait_thr.pid # pid of the started process.
        output = stdout.read
        errors = stderr.read
        exit_status = wait_thr.value # Process::Status object returned.
      }

      if !output.blank?
        render json: output
      elsif !errors.blank?
        render json: '{"error":"Exception thrown"}'
      end
    end

    private

    def verify_transaction transaction_json, transaction_history_json
      set_stuff
      method = "verifyTransaction"
      arg_one = Shellwords.escape transaction_json.chomp
      arg_two = Shellwords.escape transaction_history_json
      output = %x{ #{@command} #{method} #{arg_one} #{arg_two} }

      if output.chomp.eql? "true"
        true
      elsif output.chomp.eql? "false"
        false
      else
        throw "Something went wrong"
      end
    end

    def set_stuff
      @command = "java -jar ../bitcoin/bitcoin-1.0.jar"

      @method_name = 'generateCreationTransaction'
    end

    def transaction_history
      ts = Transaction.all.to_a

      transaction_hash = {transactionList: []}

      ts.each do |t|
        t_hash = {transactionId: t.id}

        c = t.creations.first
        if c.blank?
          t_hash["creations"] = []
        else
          c_hash = {
            creationId: c.id,
            creatorAddress: c.creator_address,
            amount: c.amount,
            signature: c.signature,
          }

          t_hash["creations"] = [c_hash]
        end

        i = t.inputs.first
        if i.blank?
          t_hash["inputs"] = []
        else
          i_hash = {
            inputId: i.id,
            outputId: i.output_id.to_i,
            signature: i.signature,
          }

          t_hash["inputs"] = [i_hash]
        end

        o = t.outputs.first
        if o.blank?
          t_hash["outputs"] = []
        else
          o_hash = {
            outputId: o.id,
            creatorAddress: o.creator_address,
            amount: o.amount,
            address: o.address,
            inputId: o.input_id,
          }

          t_hash["outputs"] = [o_hash]
        end

        transaction_hash[:transactionList] << t_hash
      end

      transaction_hash.to_json
    end

    def save_transaction transaction_json

      parsed_transaction = JSON.parse(transaction_json)

      creation_json = parsed_transaction["creations"]
      input_json = parsed_transaction["inputs"]
      output_json = parsed_transaction["outputs"]

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
        creation.save
      end

      unless input_json.blank?
        input_json = input_json.first

        input.output_id = input_json["outputId"].to_i
        input.signature = input_json["signature"]
        input.save

        o = Output.find(input.output_id)
        o.input_id = input.id
        o.save
        #input.save
      end

      unless output_json.blank?
        # Only one creation for now
        output_json = output_json.first
        # TODO Make loop

        output.creator_address = output_json["creatorAddress"]
        output.amount = output_json["amount"].to_i
        output.address = output_json["address"]
        output.input_id = 0
        # TODO Add input når relevant
        output.save
      end

      transaction.creations << creation if creation.id
      transaction.inputs << input if input.id
      transaction.outputs << output if output.id
      @transaction = transaction
      @transaction.save
    end
  end
end
