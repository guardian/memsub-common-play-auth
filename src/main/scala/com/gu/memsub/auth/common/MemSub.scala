package com.gu.memsub.auth.common

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.auth.{AWSCredentialsProviderChain, InstanceProfileCredentialsProvider}
import com.amazonaws.services.s3.model.S3ObjectId
import com.gu.googleauth.{GoogleAuthConfig, GoogleGroupChecker, GoogleServiceAccount}
import com.typesafe.config.Config

object MemSub {

  object AWSCredentialsProvider {
    val Dev = new ProfileCredentialsProvider("membership")
    val Prod = InstanceProfileCredentialsProvider.getInstance
    val Chain = new AWSCredentialsProviderChain(Dev, Prod)
  }

  object Google {
    val GuardianAppsDomain = "guardian.co.uk"

    object ServiceAccount {
      val PrivateKeyLocation = new S3ObjectId("membership-private", "membership_directory_cert.p12")

      lazy val PrivateKey = new S3PrivateKeyService(AWSCredentialsProvider.Chain).loadPrivateKey(PrivateKeyLocation)
    }

    def googleAuthConfigFor(config: Config): GoogleAuthConfig = {
      val c = config.getConfig("google.oauth")
      GoogleAuthConfig(
        c.getString("client.id"),
        c.getString("client.secret"),
        c.getString("callback"),
        GuardianAppsDomain        // Google App domain to restrict login
      )
    }

    def googleGroupCheckerFor(config: Config): GoogleGroupChecker = {
      val con = config.getConfig("google.directory.service_account")
      new GoogleGroupChecker(directoryServiceAccount = GoogleServiceAccount(
        email = con.getString("id"),
        privateKey = ServiceAccount.PrivateKey,
        impersonatedUser = con.getString("email")
      ))
    }
  }

}
