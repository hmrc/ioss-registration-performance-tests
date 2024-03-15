/*
 * Copyright 2024 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.perftests.registration

import io.gatling.core.Predef._
import io.gatling.core.session.Expression
import io.gatling.http.Predef._
import uk.gov.hmrc.performance.conf.ServicesConfiguration

object RegistrationRequests extends ServicesConfiguration {

  val baseUrl: String = baseUrlFor("ioss-registration-frontend")
  val route: String   = "/pay-vat-on-goods-sold-to-eu/register-for-import-one-stop-shop"

  val loginUrl = baseUrlFor("auth-login-stub")

  def inputSelectorByName(name: String): Expression[String] = s"input[name='$name']"

  def getAlreadyRegisteredForIOSS =
    http("Get Already Registered for IOSS page")
      .get(s"$baseUrl$route/ioss-registered")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postAlreadyRegisteredForIOSS =
    http("Post Already Registered for IOSS page")
      .post(s"$baseUrl$route/ioss-registered")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", false)
      .check(status.in(303))
      .check(header("Location").is(s"$route/selling-goods-outside-single-market"))

  def getSellingGoodsOutsideSingleMarket =
    http("Get Selling Goods Outside Single Market page")
      .get(s"$baseUrl$route/selling-goods-outside-single-market")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postSellingGoodsOutsideSingleMarket =
    http("Post Selling Goods Outside Single Market page")
      .post(s"$baseUrl$route/selling-goods-outside-single-market")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", true)
      .check(status.in(303))
      .check(header("Location").is(s"$route/goods-value"))

  def getGoodsValue =
    http("Get Goods Value page")
      .get(s"$baseUrl$route/goods-value")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postGoodsValue =
    http("Post Goods Value page")
      .post(s"$baseUrl$route/goods-value")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", true)
      .check(status.in(303))
      .check(header("Location").is(s"$route/registered-for-vat-in-uk"))

  def getRegisteredForUKVAT =
    http("Get Registered for VAT in UK page")
      .get(s"$baseUrl$route/registered-for-vat-in-uk")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postRegisteredForUKVAT =
    http("Post Registered for VAT in UK page")
      .post(s"$baseUrl$route/registered-for-vat-in-uk")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", true)
      .check(status.in(303))
      .check(header("Location").is(s"$route/ni-based"))

  def getNIBusiness =
    http("Get Northern Ireland Business page")
      .get(s"$baseUrl$route/ni-based")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postNIBusiness =
    http("Post Northern Ireland Business page")
      .post(s"$baseUrl$route/ni-based")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", true)
      .check(status.in(303))
      .check(header("Location").is(s"$route/register-to-use-service"))

  def getRegisterToUseService =
    http("Get Register to Use Service page")
      .get(s"$baseUrl$route/register-to-use-service")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postRegisterToUseService =
    http("Post Register to Use Service page")
      .post(s"$baseUrl$route/register-to-use-service")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.in(303))
      .check(header("Location").is(s"$route/on-sign-in"))

  def getAuthorityWizard =
    http("Get Authority Wizard page")
      .get(loginUrl + s"/auth-login-stub/gg-sign-in")
      .check(status.in(200, 303))

  def postAuthorityWizard =
    http("Enter Auth login credentials ")
      .post(loginUrl + s"/auth-login-stub/gg-sign-in")
      .formParam("authorityId", "")
      .formParam("gatewayToken", "")
      .formParam("credentialStrength", "strong")
      .formParam("confidenceLevel", "50")
      .formParam("affinityGroup", "Organisation")
      .formParam("email", "user@test.com")
      .formParam("credentialRole", "User")
      .formParam("redirectionUrl", baseUrl + route)
      .formParam("enrolment[0].name", "HMRC-MTD-VAT")
      .formParam("enrolment[0].taxIdentifier[0].name", "VRN")
      .formParam("enrolment[0].taxIdentifier[0].value", "${vrn}")
      .formParam("enrolment[0].state", "Activated")
      .check(status.in(200, 303))
      .check(headerRegex("Set-Cookie", """mdtp=(.*)""").saveAs("mdtpCookie"))

  def postAuthorityWizardWithIOSSEnrolment(iossNumber: String) =
    http("Enter Auth login credentials ")
      .post(loginUrl + s"/auth-login-stub/gg-sign-in")
      .formParam("authorityId", "")
      .formParam("gatewayToken", "")
      .formParam("credentialStrength", "strong")
      .formParam("confidenceLevel", "50")
      .formParam("affinityGroup", "Organisation")
      .formParam("email", "user@test.com")
      .formParam("credentialRole", "User")
      .formParam("redirectionUrl", baseUrl + route)
      .formParam("enrolment[0].name", "HMRC-MTD-VAT")
      .formParam("enrolment[0].taxIdentifier[0].name", "VRN")
      .formParam("enrolment[0].taxIdentifier[0].value", "${vrn}")
      .formParam("enrolment[0].state", "Activated")
      .formParam("enrolment[1].name", "HMRC-IOSS-ORG")
      .formParam("enrolment[1].taxIdentifier[0].name", "IOSSNumber")
      .formParam("enrolment[1].taxIdentifier[0].value", iossNumber)
      .formParam("enrolment[1].state", "Activated")
      .check(status.in(200, 303))
      .check(headerRegex("Set-Cookie", """mdtp=(.*)""").saveAs("mdtpCookie"))

  def postAuthorityWizardWithMultipleIOSSEnrolments =
    http("Enter Auth login credentials for multiple IOSS Numbers")
      .post(loginUrl + s"/auth-login-stub/gg-sign-in")
      .formParam("authorityId", "")
      .formParam("gatewayToken", "")
      .formParam("credentialStrength", "strong")
      .formParam("confidenceLevel", "50")
      .formParam("affinityGroup", "Organisation")
      .formParam("email", "user@test.com")
      .formParam("credentialRole", "User")
      .formParam("redirectionUrl", baseUrl + route)
      .formParam("enrolment[0].name", "HMRC-MTD-VAT")
      .formParam("enrolment[0].taxIdentifier[0].name", "VRN")
      .formParam("enrolment[0].taxIdentifier[0].value", "${vrn}")
      .formParam("enrolment[0].state", "Activated")
      .formParam("enrolment[1].name", "HMRC-IOSS-ORG")
      .formParam("enrolment[1].taxIdentifier[0].name", "IOSSNumber")
      .formParam("enrolment[1].taxIdentifier[0].value", "IM9007230003")
      .formParam("enrolment[1].state", "Activated")
      .formParam("enrolment[2].name", "HMRC-IOSS-ORG")
      .formParam("enrolment[2].taxIdentifier[0].name", "IOSSNumber")
      .formParam("enrolment[2].taxIdentifier[0].value", "IM9007230002")
      .formParam("enrolment[2].state", "Activated")
      .formParam("enrolment[3].name", "HMRC-IOSS-ORG")
      .formParam("enrolment[3].taxIdentifier[0].name", "IOSSNumber")
      .formParam("enrolment[3].taxIdentifier[0].value", "IM9007230001")
      .formParam("enrolment[3].state", "Activated")
      .check(status.in(200, 303))
      .check(headerRegex("Set-Cookie", """mdtp=(.*)""").saveAs("mdtpCookie"))

  def resumeJourney =
    http("Resume journey")
      .get(s"$baseUrl$route/on-sign-in")
      .check(status.in(303))

  def getConfirmVatDetails =
    http("Get Confirm VAT Details page")
      .get(s"$baseUrl$route/confirm-vat-details")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postConfirmVatDetails =
    http("Confirm VAT Details")
      .post(s"$baseUrl$route/confirm-vat-details")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", "yes")
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/have-uk-trading-name"))

  def getHasTradingName =
    http("Get Has Trading Name page")
      .get(s"$baseUrl$route/have-uk-trading-name")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postHasTradingName =
    http("Post Has Trading Name")
      .post(s"$baseUrl$route/have-uk-trading-name")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", "true")
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/uk-trading-name/1"))

  def getTradingName(index: Int) =
    http("Get Trading Name page")
      .get(s"$baseUrl$route/uk-trading-name/$index")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postTradingName(index: Int, tradingName: String) =
    http("Enter Trading Name")
      .post(s"$baseUrl$route/uk-trading-name/$index")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", tradingName)
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/add-uk-trading-name"))

  def getAddTradingName =
    http("Get Add Trading Name page")
      .get(s"$baseUrl$route/add-uk-trading-name")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def testAddTradingName(answer: Boolean) =
    http("Add Trading Name")
      .post(s"$baseUrl$route/add-uk-trading-name")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", answer)
      .check(status.in(200, 303))

  def postAddTradingName(answer: Boolean, index: Option[Int]) =
    if (answer) {
      testAddTradingName(answer)
        .check(header("Location").is(s"$route/uk-trading-name/${index.get}"))
    } else {
      testAddTradingName(answer)
        .check(header("Location").is(s"$route/previous-oss"))
    }

  def getBusinessContactDetails =
    http("Get Business Contact Details page")
      .get(s"$baseUrl$route/business-contact-details")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postBusinessContactDetails =
    http("Enter Business Contact Details")
      .post(s"$baseUrl$route/business-contact-details")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("fullName", "Trader Name")
      .formParam("telephoneNumber", "012301230123")
      .formParam("emailAddress", "trader@testemail.com")
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/bank-details"))

  def getBankDetails =
    http("Get Bank Details page")
      .get(s"$baseUrl$route/bank-details")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postBankDetails =
    http("Enter Bank Details")
      .post(s"$baseUrl$route/bank-details")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("accountName", "Trader name")
      .formParam("bic", "ABCDEF2A")
      .formParam("iban", "GB33BUKB20201555555555")
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/check-your-answers"))

  def getPreviousOss =
    http("Get Is Previous Oss page")
      .get(s"$baseUrl$route/previous-oss")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postPreviousOss(index: Int) =
    http("Answer Previous Oss Page")
      .post(s"$baseUrl$route/previous-oss")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", "true")
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/previous-country/$index"))

  def getPreviousCountry(index: Int) =
    http("Get previous country page")
      .get(s"$baseUrl$route/previous-country/$index")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postPreviousCountry(countryIndex: Int, schemeIndex: Int, countryCode: String) =
    http("Enter previous country")
      .post(s"$baseUrl$route/previous-country/$countryIndex")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", countryCode)
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/previous-scheme/$countryIndex/$schemeIndex"))

  def getPreviousScheme(countryIndex: Int, schemeIndex: Int) =
    http("Get Previous Scheme page")
      .get(s"$baseUrl$route/previous-scheme/$countryIndex/$schemeIndex")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def testPreviousScheme(countryIndex: Int, schemeIndex: Int, schemeType: String) =
    http("Answer Previous Scheme")
      .post(s"$baseUrl$route/previous-scheme/$countryIndex/$schemeIndex")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", schemeType)
      .check(status.in(200, 303))

  def postPreviousScheme(countryIndex: Int, schemeIndex: Int, schemeType: String) =
    if (schemeType == "oss") {
      testPreviousScheme(countryIndex, schemeIndex, schemeType)
        .check(header("Location").is(s"$route/previous-oss-scheme-number/$countryIndex/$schemeIndex"))
    } else {
      testPreviousScheme(countryIndex, schemeIndex, schemeType)
        .check(header("Location").is(s"$route/previous-ioss-scheme/$countryIndex/$schemeIndex"))
    }

  def getPreviousOssSchemeNumber(countryIndex: Int, schemeIndex: Int) =
    http("Get Previous Oss Scheme number page")
      .get(s"$baseUrl$route/previous-oss-scheme-number/$countryIndex/$schemeIndex")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postPreviousOssSchemeNumber(countryIndex: Int, schemeIndex: Int, registrationNumber: String) =
    http("Enter Previous Oss Scheme Number")
      .post(s"$baseUrl$route/previous-oss-scheme-number/$countryIndex/$schemeIndex")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", registrationNumber)
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/previous-scheme-answers/$countryIndex"))

  def getPreviousSchemeAnswers(index: Int) =
    http("Get Previous Scheme Answers page")
      .get(s"$baseUrl$route/previous-scheme-answers/$index")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postPreviousSchemeAnswers(index: Int, answer: Boolean) =
    http("Post Previous Scheme Answers page")
      .post(s"$baseUrl$route/previous-scheme-answers/$index")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", answer)
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/previous-schemes-overview"))

  def getPreviousSchemesOverview =
    http("Get Previous Schemes Overview page")
      .get(s"$baseUrl$route/previous-schemes-overview")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def testPreviousSchemesOverview(answer: Boolean) =
    http("Previous Schemes Overview")
      .post(s"$baseUrl$route/previous-schemes-overview?incompletePromptShown=false")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", answer)
      .check(status.in(200, 303))

  def postPreviousSchemesOverview(answer: Boolean, index: Option[Int]) =
    if (answer) {
      testPreviousSchemesOverview(answer)
        .check(header("Location").is(s"$route/previous-country/${index.get}"))
    } else {
      testPreviousSchemesOverview(answer)
        .check(header("Location").is(s"$route/tax-in-eu"))
    }

  def getPreviousIossScheme(countryIndex: Int, schemeIndex: Int) =
    http("Get Previous IOSS Scheme page")
      .get(s"$baseUrl$route/previous-ioss-scheme/$countryIndex/$schemeIndex")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postPreviousIossScheme(countryIndex: Int, schemeIndex: Int, answer: Boolean) =
    http("Previous IOSS Scheme")
      .post(s"$baseUrl$route/previous-ioss-scheme/$countryIndex/$schemeIndex")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", answer)
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/previous-ioss-number/$countryIndex/$schemeIndex"))

  def getPreviousIossNumber(countryIndex: Int, schemeIndex: Int) =
    http("Get Previous IOSS number page")
      .get(s"$baseUrl$route/previous-ioss-number/$countryIndex/$schemeIndex")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postPreviousIossNumber(countryIndex: Int, schemeIndex: Int, iossNumber: String, intermediaryNumber: String) =
    http("Previous IOSS Number")
      .post(s"$baseUrl$route/previous-ioss-number/$countryIndex/$schemeIndex")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("previousSchemeNumber", iossNumber)
      .formParam("previousIntermediaryNumber", intermediaryNumber)
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/previous-scheme-answers/$countryIndex"))

  def getIsTaxRegisteredInEu =
    http("Get Is Tax Registered in EU page")
      .get(s"$baseUrl$route/tax-in-eu")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postIsTaxRegisteredInEu(index: Int) =
    http("Answer Is Tax Registered in EU")
      .post(s"$baseUrl$route/tax-in-eu")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", "true")
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/eu-tax/$index"))

  def getVatRegisteredInEuMemberState(index: Int) =
    http("Get Tax Registered in EU Member State page")
      .get(s"$baseUrl$route/eu-tax/$index")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postVatRegisteredInEuMemberState(index: Int, countryCode: String) =
    http("Enter Tax Registered in EU Member State")
      .post(s"$baseUrl$route/eu-tax/$index")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", countryCode)
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/how-do-you-operate/$index"))

  def getHowDoYouOperate(index: Int) =
    http("Get How Do You Operate page")
      .get(s"$baseUrl$route/how-do-you-operate/$index")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postHowDoYouOperate(index: Int) =
    http("Answer How Do You Operate Page")
      .post(s"$baseUrl$route/how-do-you-operate/$index")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", true)
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/registration-type/$index"))

  def getRegistrationType(index: Int) =
    http("Get Registration Type page")
      .get(s"$baseUrl$route/registration-type/$index")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def testRegistrationType(index: Int, registrationType: String) =
    http("Answer Registration Type Page")
      .post(s"$baseUrl$route/registration-type/$index")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", registrationType)
      .check(status.in(200, 303))

  def postRegistrationType(index: Int, registrationType: String) =
    if (registrationType == "vatNumber") {
      testRegistrationType(index, registrationType)
        .check(header("Location").is(s"$route/eu-vat-number/$index"))
    } else {
      testRegistrationType(index, registrationType)
        .check(header("Location").is(s"$route/eu-tax-number/$index"))
    }

  def getEuVatNumber(index: Int) =
    http("Get EU VAT Number page")
      .get(s"$baseUrl$route/eu-vat-number/$index")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postEuVatNumber(index: Int, euVatNumber: String) =
    http("Enter EU VAT Number")
      .post(s"$baseUrl$route/eu-vat-number/$index")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", euVatNumber)
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/eu-trading-name/$index"))

  def getFixedEuTradingName(index: Int) =
    http("Get Fixed Establishment Trading Name page")
      .get(s"$baseUrl$route/eu-trading-name/$index")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postFixedEuTradingName(index: Int, tradingName: String) =
    http("Enter Fixed Eu Trading Name")
      .post(s"$baseUrl$route/eu-trading-name/$index")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", tradingName)
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/eu-fixed-establishment-address/$index"))

  def getFixedEstablishmentAddress(index: Int) =
    http("Get Fixed Establishment Address page")
      .get(s"$baseUrl$route/eu-fixed-establishment-address/$index")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postFixedEstablishmentAddress(index: Int) =
    http("Enter Fixed Establishment Address")
      .post(s"$baseUrl$route/eu-fixed-establishment-address/$index")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("line1", "line1")
      .formParam("line2", "line2")
      .formParam("townOrCity", "townOrCity")
      .formParam("postCode", "ABC")
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/check-tax-details/$index"))

  def getCheckTaxDetails(index: Int) =
    http("Get Check Tax Details page")
      .get(s"$baseUrl$route/check-tax-details/$index")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postCheckTaxDetails(index: Int) =
    http("Submit Check EU VAT Details")
      .post(s"$baseUrl$route/check-tax-details/$index?incompletePromptShown=false")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/add-tax-details"))

  def getAddTaxDetails =
    http("Get Add VAT Details page")
      .get(s"$baseUrl$route/add-tax-details")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def testAddTaxDetails(answer: Boolean) =
    http("Answer Add EU VAT Details")
      .post(s"$baseUrl$route/add-tax-details?incompletePromptShown=false")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", answer)
      .check(status.in(200, 303))

  def postAddTaxDetails(answer: Boolean, index: Option[Int]) =
    if (answer) {
      testAddTaxDetails(answer)
        .check(header("Location").is(s"$route/eu-tax/${index.get}"))
    } else {
      testAddTaxDetails(answer)
        .check(header("Location").is(s"$route/website-address/${index.get}"))
    }

  def getEuTaxReference(index: Int) =
    http("Get EU Tax Reference page")
      .get(s"$baseUrl$route/eu-tax-number/$index")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postEuTaxReference(index: Int, taxReference: String) =
    http("Enter EU Tax Reference")
      .post(s"$baseUrl$route/eu-tax-number/$index")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", taxReference)
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/eu-trading-name/$index"))

  def getWebsite(index: Int) =
    http(s"Get Website page $index")
      .get(s"$baseUrl$route/website-address/$index")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postWebsite(index: Int, website: String) =
    http(s"Enter website $index")
      .post(s"$baseUrl$route/website-address/$index")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", website)
      .check(status.in(303))
      .check(header("Location").is(s"$route/add-website-address"))

  def getAddWebsite =
    http("Get Add Website page")
      .get(s"$baseUrl$route/add-website-address")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def testAddWebsite(answer: Boolean) =
    http("Add Website")
      .post(s"$baseUrl$route/add-website-address")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", answer)
      .check(status.in(200, 303))

  def postAddWebsite(answer: Boolean, index: Option[Int]) =
    if (answer) {
      testAddWebsite(answer)
        .check(header("Location").is(s"$route/website-address/${index.get}"))
    } else {
      testAddWebsite(answer)
        .check(header("Location").is(s"$route/business-contact-details"))
    }

  def getCheckYourAnswers =
    http("Get Check Your Answers page")
      .get(s"$baseUrl$route/check-your-answers")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postCheckYourAnswers =
    http("Post Check Your Answers page")
      .post(s"$baseUrl$route/check-your-answers/false")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/successful"))

  def getRegistrationSuccessful =
    http("Get Registration Successful page")
      .get(s"$baseUrl$route/successful")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(status.in(200))

  def getAmendJourney =
    http("Get Amend Registration Journey")
      .get(s"$baseUrl$route/start-amend-journey")
      .check(status.in(303))
      .check(header("Location").is(s"$route/change-your-registration"))

  def getAmendAddTradingName =
    http("Get Amend Add Trading Name page")
      .get(s"$baseUrl$route/add-uk-trading-name?waypoints=change-your-registration")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def testAmendAddTradingName(answer: Boolean) =
    http("Add Trading Name")
      .post(s"$baseUrl$route/add-uk-trading-name?waypoints=change-your-registration")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", answer)
      .check(status.in(200, 303))

  def postAmendAddTradingName(answer: Boolean) =
    if (answer) {
      testAmendAddTradingName(answer)
        .check(
          header("Location").is(s"$route/uk-trading-name/3?waypoints=add-uk-trading-name%2Cchange-your-registration")
        )
    } else {
      testAmendAddTradingName(answer)
        .check(header("Location").is(s"$route/change-your-registration"))
    }

  def getAmendTradingName(index: Int) =
    http("Get Trading Name page")
      .get(s"$baseUrl$route/uk-trading-name/3?waypoints=change-add-uk-trading-name%2Cchange-your-registration")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postAmendTradingName(index: Int, tradingName: String) =
    http("Enter Trading Name")
      .post(s"$baseUrl$route/uk-trading-name/3?waypoints=change-add-uk-trading-name%2Cchange-your-registration")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", tradingName)
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/add-uk-trading-name?waypoints=change-your-registration"))

  def getAmendBusinessContactDetails =
    http("Get Amend Business Contact Details page")
      .get(s"$baseUrl$route/business-contact-details?waypoints=change-your-registration")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postAmendBusinessContactDetails =
    http("Enter Amend Business Contact Details")
      .post(s"$baseUrl$route/business-contact-details?waypoints=change-your-registration")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("fullName", "Amended Trader Name")
      .formParam("telephoneNumber", "012301230123")
      .formParam("emailAddress", "amendedtrader@testemail.com")
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/change-your-registration"))

  def getAmendBankDetails =
    http("Get Amend Bank Details page")
      .get(s"$baseUrl$route/bank-details?waypoints=change-your-registration")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postAmendBankDetails =
    http("Enter Amend Bank Details")
      .post(s"$baseUrl$route/bank-details?waypoints=change-your-registration")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("accountName", "Amended trader name")
      .formParam("bic", "ABCDEF2A")
      .formParam("iban", "GB33BUKB20201555555555")
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/change-your-registration"))

  def getChangeYourRegistration =
    http("Get Change Your Registration page")
      .get(s"$baseUrl$route/change-your-registration")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postChangeYourRegistration =
    http("Post Change Your Registration page")
      .post(s"$baseUrl$route/change-your-registration?incompletePrompt=false")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/successful-amend"))

  def getSuccessfulAmend =
    http("Get Successful Amend page")
      .get(s"$baseUrl$route/successful-amend")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(status.in(200))

  def getRejoinJourney =
    http("Get Rejoin Registration Journey")
      .get(s"$baseUrl$route/start-rejoin-journey")
      .check(status.in(303))
      .check(header("Location").is(s"$route/rejoin-registration"))

  def getRejoinAddTradingName =
    http("Get Rejoin Add Trading Name page")
      .get(s"$baseUrl$route/add-uk-trading-name?waypoints=rejoin-registration")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def testRejoinAddTradingName(answer: Boolean) =
    http("Rejoin Add Trading Name")
      .post(s"$baseUrl$route/add-uk-trading-name?waypoints=rejoin-registration")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", answer)
      .check(status.in(200, 303))

  def postRejoinAddTradingName(answer: Boolean) =
    if (answer) {
      testRejoinAddTradingName(answer)
        .check(
          header("Location").is(s"$route/uk-trading-name/3?waypoints=add-uk-trading-name%2Crejoin-registration")
        )
    } else {
      testRejoinAddTradingName(answer)
        .check(header("Location").is(s"$route/rejoin-registration"))
    }

  def getRejoinTradingName(index: Int) =
    http("Get Rejoin Trading Name page")
      .get(s"$baseUrl$route/uk-trading-name/3?waypoints=change-add-uk-trading-name%2Crejoin-registration")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postRejoinTradingName(index: Int, tradingName: String) =
    http("Rejoin Enter Trading Name")
      .post(s"$baseUrl$route/uk-trading-name/3?waypoints=change-add-uk-trading-name%2Crejoin-registration")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", tradingName)
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/add-uk-trading-name?waypoints=rejoin-registration"))

  def getRejoinRegistration =
    http("Get Rejoin Registration page")
      .get(s"$baseUrl$route/rejoin-registration")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postRejoinRegistration =
    http("Post Rejoin Registration page")
      .post(s"$baseUrl$route/rejoin-registration?incompletePrompt=false")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/successful-rejoin"))

  def getSuccessfulRejoin =
    http("Get Successful Rejoin page")
      .get(s"$baseUrl$route/successful-rejoin")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(status.in(200))

  def getChangePreviousRegistrations =
    http("Get Change Previous Registrations page")
      .get(s"$baseUrl$route/change-your-previous-registrations?waypoints=change-your-registration")
      .header("Cookie", "mdtp=${mdtpCookie}")
      .check(css(inputSelectorByName("csrfToken"), "value").saveAs("csrfToken"))
      .check(status.in(200))

  def postChangePreviousRegistrations(selection: String) =
    http("Answer Change Previous Registrations Page")
      .post(s"$baseUrl$route/change-your-previous-registrations?waypoints=change-your-registration")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", selection)
      .check(status.in(200, 303))
      .check(header("Location").is(s"$route/start-amend-previous-journey/?waypoints=change-your-registration"))

}
