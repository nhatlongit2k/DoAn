package com.example.drivinglicenceadmin.ui.amazon_client

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest
import java.io.File
import java.util.*


class AmazonClient {


//    s3client = AmazonS3Client(credentials)

    private val endpointUrl: String = "s3.us-west-2.amazonaws.com"

    private val bucketName: String = "drivinglicence"

    private val accessKey: String = "AKIAZ4CCLUCVQJ2JUO7C"
    private val secretKey: String = "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"

    val credentials: AWSCredentials = BasicAWSCredentials(accessKey, secretKey)
    private var s3client: AmazonS3? = AmazonS3Client(credentials)

//    private fun initializeAmazon() {
//        val credentials: AWSCredentials = BasicAWSCredentials(accessKey, secretKey)
//        s3client = AmazonS3Client(credentials)
//    }

//    init {
//        val credentials: AWSCredentials = BasicAWSCredentials(accessKey, secretKey)
//        s3client = AmazonS3Client(credentials)
//    }

//    private fun convertMultiPartToFile(file: MultipartFile): File {
//        val convFile = File(file.getOriginalFilename())
//        val fos = FileOutputStream(convFile)
//        fos.write(file.getBytes())
//        fos.close()
//        return convFile
//    }

//    private fun generateFileName(multiPart: MultipartFile): String {
//        return Date().getTime().toString() + "-" + multiPart.getOriginalFilename().replace(" ", "_")
//    }
//
//    private fun uploadFileTos3bucket(fileName: String, file: File) {
//        s3client!!.putObject(
//            PutObjectRequest(bucketName, fileName, file)
//                .withCannedAcl(CannedAccessControlList.PublicRead)
//        )
//    }
//
//    fun uploadFile(multipartFile: MultipartFile): String? {
//        var fileUrl = ""
//        try {
//            val file: File = convertMultiPartToFile(multipartFile)
//            val fileName = generateFileName(multipartFile)
//            fileUrl = "https://$bucketName.$endpointUrl/$fileName"
//            uploadFileTos3bucket(fileName, file)
//            file.delete()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return fileUrl
//    }


    fun uploadFile(file: File): String? {
        var fileUrl = ""
        try {
//            val file: File = convertMultiPartToFile(file)
            val fileName = generateFileName(file)
            fileUrl = "https://$bucketName.$endpointUrl/$fileName"
            uploadFileTos3bucket(fileName, file)
            file.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return fileUrl
    }

    private fun generateFileName(file: File): String {
        return Date().getTime().toString() + "-" + file.name.replace(" ", "_")
    }
    private fun uploadFileTos3bucket(fileName: String, file: File) {
        s3client!!.putObject(
            PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead)
        )
    }
}