module Backend
  class TransactionsController < ApplicationController
    before_filter :set_stuff

    def generate_creation
      @output = `#{@command} #{@method_name} #{@private_key}`
      #binding.pry

      @parsed_json = JSON.parse(@json)
      @parsed_output = JSON.parse(@output)

      #render json: "#{@output}\n\n\n#{@json}"
    end

    private
      def set_stuff
        @json = '{"transactionId":0,"creations":[{"creationId":0,"creatorAddress":"1LfXmYtDHyCM8fHMhrMX2EtfECGjzmw3BW","amount":1,"signature":"EsykzEva7gBWpyZbpBXvyWiZb3Dwta18T6uEg4E39jdpkh3ouaNhaGyfg3rVfij4bY38pnuyedT6Ab63wyBzY2z6WUUy4P5v1QqDx 3agqkP4xN8q573oKCJTNz3nb1y4euvuAP2qeuaLRhpvGCRoJ3godvpEDKqAEGP6GyeYLw9hRtgczD9NPv5drmE7Q5DF8dd"}],"inputs":[],"outputs":[{"outputId":0,"creatorAddress":"1LfXmYtDHyCM8fHMhrMX2EtfECGjzmw3BW","amount":1,"address":"1LfXmYtDHyCM8fHMhrMX2EtfECGjzmw3BW","inputId":0}]}'
        @command = '/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/bin/java -Didea.launcher.port=7536 "-Didea.launcher.bin.path=/Applications/Android Studio.app/bin" -Dfile.encoding=UTF-8 -classpath "/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/lib/ant-javafx.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/lib/dt.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/lib/javafx-doclet.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/lib/javafx-mx.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/lib/jconsole.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/lib/sa-jdi.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/lib/tools.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/deploy.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/htmlconverter.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/javaws.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/jfxrt.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/plugin.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/ext/sunec.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/ext/zipfs.jar:/Users/krisvage/Programmering/bitcoin/bitcoin-coupon/bitcoin/build/classes/main:/Users/krisvage/.gradle/caches/modules-2/files-2.1/commons-codec/commons-codec/1.9/9ce04e34240f674bc72680f8b843b1457383161a/commons-codec-1.9.jar:/Users/krisvage/.gradle/caches/modules-2/files-2.1/com.google.code.gson/gson/2.3/5fc52c41ef0239d1093a1eb7c3697036183677ce/gson-2.3.jar:/Users/krisvage/.gradle/caches/modules-2/files-2.1/com.madgag.spongycastle/core/1.51.0.0/f642963312ea0e615ad65f28adc5a5b3a2a0862/core-1.51.0.0.jar:/Applications/Android Studio.app/lib/idea_rt.jar" com.intellij.rt.execution.application.AppMain bitcoupon.Main'

        @method_name = 'generateCreationTransaction'
        @private_key = '5Kf9gd8faKhhq9jZTsNhq2MtViHA1dWdhRg9k4ovszTKz5DCeBT'
      end
  end
end
