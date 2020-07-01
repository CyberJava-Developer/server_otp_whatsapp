package com.gsmnetindo.server_otp.controller

import com.gsmnetindo.server_otp.service.OtpService
import com.nexmo.client.NexmoClient
import com.nexmo.client.sms.SmsSubmissionResponse
import com.nexmo.client.sms.messages.TextMessage
import okhttp3.*
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/otp")
class OtpController {

    @Autowired
    private lateinit var otpService: OtpService

    @PostMapping("/send")
    fun sendOtp(@RequestParam phoneNumber: String): ResponseEntity<Map<String, Any>> {
        val codeOtp = otpService.createOtp()

        val client = NexmoClient.Builder()
                .apiKey("5835e4a1")
                .apiSecret("GGWWADl6NTXgCHZs")
                .build()
        val formRequestBody = FormBody.Builder()
                .add("apikey", "mzJGmrngJKo-R8XegOoHmLtnugJRSQzRxJSi99Lcws")
                .add("message", codeOtp)
                .add("sender", "System")
                .add("numbers", phoneNumber)
                .build()
        val request = Request.Builder()
                .url("https://api.txtlocal.com/send/?")
                .post(formRequestBody)
                .build()

        val call = OkHttpClient().newCall(request)
        val response = call.execute()
        val responseData = HashMap<String, Any>()
        responseData["time_server"] = System.currentTimeMillis()

        val isSuccessful = response.isSuccessful
        val responseCode = response.code()
        val jsonObjectResponseFromServer = JSONObject(response.body().string())
        val responseStatusMessage = jsonObjectResponseFromServer.getString("status")
        println("responseDataFromServer: $jsonObjectResponseFromServer")

        //api wa

        val client12 = OkHttpClient().newBuilder()
                .build()
        val body2: RequestBody = FormBody.Builder()
                .add("phone",phoneNumber)
                .add("body", codeOtp)
                .build()
        val request12: Request = Request.Builder()
                .url("https://eu148.chat-api.com/instance144986/sendMessage?token=yhylhr9cegk61axx")
                .method("POST", body2)
                .build()
        val response112 = client12.newCall(request12).execute()
        for (responseMessage in response112.message()) {
            print(responseMessage)
        }


//        //d7networkapi
//        val client1 = OkHttpClient().newBuilder()
//                .build()
//        val mediaType = MediaType.parse("text/plain")
//        val body: RequestBody = FormBody.Builder()
//                .build()
//        val request1: Request = Request.Builder()
//                .url("https://http-api.d7networks.com/send?username=iujm2060&password=ukXijlID&dlr-method=POST&dlr-url=https://4ba60af1.ngrok.io/receive&dlr=yes&dlr-level=3&from=smsinfo&content="+codeOtp+" &to="+phoneNumber+"")
//                .method("POST", body)
//                .build()
//        val response11 = client1.newCall(request1).execute()
//        for (responseMessage in response11.message()) {
//            println(responseMessage)
//        }
//
//        //twilio api
//
//        val client2 = OkHttpClient()
//
//        val request2: Request = Request.Builder()
//                .url("https://twilio-sms.p.rapidapi.com/2010-04-01/Account")
//                .get()
//                .addHeader("x-rapidapi-host", "twilio-sms.p.rapidapi.com")
//                .addHeader("x-rapidapi-key", "b934fb0ad9msh66b889460ee9facp129f2ejsn0a4d0b8e6a1b")
//                .build()
//
//        val response2 = client2.newCall(request2).execute()


//        val text = codeOtp.toString()
//        val message = TextMessage("Vonage APIs", phoneNumber, text)
//        val response1: SmsSubmissionResponse = client.smsClient.submitMessage(message)
//        for (responseMessage in response1.messages) {
//            println(responseMessage)
//        }

        if (isSuccessful && responseCode == 200 && responseStatusMessage == "success") {
            responseData["success"] = true
        } else {
            responseData["success"] = false
            otpService.deleteOtp(codeOtp)
        }
        return ResponseEntity(responseData, HttpStatus.OK)
    }

    @PostMapping("/update")
    fun updateOtp(@RequestParam codeOtp: String): ResponseEntity<Map<String, Any>> {
        val responseData = HashMap<String, Any>()
        responseData["time_server"] = System.currentTimeMillis()
        responseData["success"] = otpService.updateOtp(codeOtp)
        return ResponseEntity(responseData, HttpStatus.OK)
    }

}
